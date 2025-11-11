@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.ChatMessageModels
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.AppState
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterApplicationDetailConversation
import com.student.Compass_Abroad.adaptor.AdapterUploadDocumentsChats
import com.student.Compass_Abroad.chatModal.SingleChatViewModel
import com.student.Compass_Abroad.databinding.BottomSheetUploadDocumentsBinding
import com.student.Compass_Abroad.databinding.FragmentAgentChatBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.widgets.Path
import com.student.Compass_Abroad.modal.getChatResponse.Record
import com.student.Compass_Abroad.modal.getChatResponse.getChatResponse
import com.student.Compass_Abroad.modal.uploadDocuments.Data
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.retrofit.ViewModalClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import java.io.File
import java.util.concurrent.Callable

class FragmentAgentChat : BaseFragment() {
    private lateinit var binding: FragmentAgentChatBinding
    private lateinit var adapterApplicationDetailConversation: AdapterApplicationDetailConversation
    private var chatRecords = mutableListOf<Record>()
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private var totalPages = 1
    private var chatIdentifier: String? = null
    private var entity: String? = null
    private lateinit var viewModel: SingleChatViewModel
    private var isAtBottom = true
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var selectedImagesList = ArrayList<File>()
    private var selectedIdentifierList = ArrayList<Data>()
    private var adaptorUploadDocuments: AdapterUploadDocumentsChats? = null
    private var chatMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAgentChatBinding.inflate(inflater, container, false)
        chatIdentifier = App.singleton?.applicationIdentifierChat
        entity = App.singleton?.idetity
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)


        setupRecyclerView()
        setupChatAdapter()
        fetchChatMessagesAndMetaData()
        setRecyclerView()

        val status = arguments?.getString("status")

        if (status.equals("ambassador")) {

            binding.attachment.visibility = View.GONE

        } else {
            binding.attachment.visibility = View.VISIBLE
        }


        binding.messageBox.setOnClickListener {
            binding.rvMessages.postDelayed({
                binding.rvMessages.scrollToPosition(adapterApplicationDetailConversation.itemCount - 1)
            }, 300)
        }

        binding.messageBox.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.rvMessages.postDelayed({
                    binding.rvMessages.scrollToPosition(adapterApplicationDetailConversation.itemCount - 1)
                }, 300)

                binding.root.requestFocus()
                binding.messageBox.postDelayed({
                    binding.messageBox.requestFocus()
                }, 350)
            }
        }

        binding.messageBox.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.rvMessages.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.rvMessages.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        binding.rvMessages.post {
                            binding.rvMessages.scrollToPosition(adapterApplicationDetailConversation.itemCount - 1)
                        }
                    }
                })
            }
        }


        binding.attachment.setOnClickListener {

            openBottomSheetDocument()

        }


        binding.sendBtn.setOnClickListener {

            chatMessage = binding.messageBox.text.toString()
            binding.messageBox.text.clear()

            if (chatMessage.isEmpty()) {
                CommonUtils.toast(requireActivity(), "Please enter a message")
                return@setOnClickListener
            } else {

                val has_attachments = if (selectedImagesList.isEmpty()) "no" else "yes"

                Log.d("postChatMessage", selectedImagesList.toString())

                postChatMessage("test", chatMessage.trim(), selectedIdentifierList, has_attachments)

            }

        }

        binding.fabFadBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun setRecyclerView() {
        adaptorUploadDocuments = AdapterUploadDocumentsChats(
            requireActivity(),
            selectedImagesList,
            binding.rvUploadDocument,
            selectedImagesList // Pass the reference
        )

        binding.rvUploadDocument.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUploadDocument.adapter = adaptorUploadDocuments

        Log.d("postChatMessage", selectedImagesList.toString())

        binding.rvUploadDocument.visibility =
            if (selectedImagesList.isEmpty()) View.GONE else View.VISIBLE
    }


    private fun postChatMessage(
        title: String,
        chatMessage: String,
        selectedImagesLists: ArrayList<Data>,
        has_attachments: String
    ) {


        val filesList = selectedImagesLists.map {
            ChatMessageModels.FileData(it.fileInfo.identifier ?: "", it.thumbInfo.identifier ?: "")
        }

        CommonUtils.accessToken?.let { accessToken ->
            val viewModel = ViewModalClass()
            viewModel.CreateChatMessageLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer $accessToken",
                App.singleton?.idetity ?: "",
                App.singleton?.applicationIdentifierChat ?: "",
                title,
                chatMessage,
                "public",
                has_attachments,
                filesList
            ).observe(viewLifecycleOwner) { chatMessageResponse ->
                chatMessageResponse?.let { response ->
                    if (response.statusCode == 201) {
                        selectedImagesList.clear()

                        Log.d("postChatMessage", selectedImagesList.toString())
                        adaptorUploadDocuments?.notifyDataSetChanged()
                        binding.rvUploadDocument.visibility = View.GONE
                    } else {

                        CommonUtils.toast(requireActivity(), response.message ?: "Message Failed")

                    }

                }
            }
        } ?: run {

            CommonUtils.toast(requireActivity(), "Access token is missing")
        }
    }


    private fun openBottomSheetDocument() {
        val bottomSheetBinding =
            BottomSheetUploadDocumentsBinding.inflate(layoutInflater, binding.root, false)
            bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheet2).apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
            bottomSheetBinding.llBsUdUseCamera.setOnClickListener {
                ImagePicker.with(this@FragmentAgentChat)
                    .cameraOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .crop()
                    .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    .start(101)


                dismiss()
            }

            bottomSheetBinding.llBsUdGallery.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R || CommonUtils.hasReadStoragePermission(
                        requireActivity()
                    )
                ) {
                    openSingleFileChooser()

                } else {
                    askStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            show()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                101 -> {
                    val imageUri = data!!.data
                    val imageFile = File(Path.getRealPathFromCamera(requireActivity(), imageUri))
                    if (imageFile.exists()) {
                        val fileSize = imageFile.length()
                        if (fileSize < 4 * 1024 * 1024) {
                            binding.pbLoading.visibility = View.GONE
                            postDocuments(requireActivity(), imageFile.absolutePath)
                        } else {
                            CommonUtils.toast(
                                requireActivity(),
                                "File size should be less than 4MB"
                            )
                        }
                    } else {
                        CommonUtils.toast(requireActivity(), "File does not exist")
                    }
                }

                102 -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        val imageFile =
                            File(Path.getRealPathFromGallery(requireActivity(), imageUri))
                        val fileSize = imageFile.length()
                        if (fileSize < 4 * 1024 * 1024) {
                            postDocuments(requireActivity(), imageFile.absolutePath)
                        } else {
                            CommonUtils.toast(
                                requireActivity(),
                                "File size should be less than 4MB"
                            )
                        }
                    } else {
                        CommonUtils.toast(requireActivity(), "Image URI is null")
                    }
                }

                2296 -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            openSingleFileChooser()
                        } else {
                            CommonUtils.toast(requireActivity(), "Permission denied.")
                        }
                    }
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            binding.pbLoading.visibility = View.GONE
            Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openSingleFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.setType("*/*")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, true)
        }
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*"))
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 102)
    }

    var askStoragePermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                selectedImagesList.clear()
                openSingleFileChooser()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                CommonUtils.alertDialog(
                    requireActivity(),
                    "storage",
                    "Storage access denied",
                    "Last time when we were asking 'Storage' permission from you. Then you clicked on Don't ask again. Now we have no right to ask permission again. But don't worry, We have an alternate method to do this. Press Open settings -> Permissions -> Storage ->  and then click 'Allow' to grant permission. Thanks",
                    false,
                    "Open settings",
                    "Cancel",
                    Callable<Void?> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        openAppSettingFromGalleryIntentLauncher.launch(intent)
                        null
                    },
                    null
                )
            }
        }

    // Launcher to open app settings
    var openAppSettingFromGalleryIntentLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result: ActivityResult ->
                if (CommonUtils.hasReadStoragePermission(requireActivity())) {
                    openSingleFileChooser()
                }
            }
        )


    private fun postDocuments(requireActivity: FragmentActivity, imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            val reqFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val part: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileUpload", file.name, reqFile)

            ViewModalClass().postApplicationUploadDocumentsLiveData(
                requireActivity,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer " + CommonUtils.accessToken,
                part, "documents"
            ).observe(requireActivity) { uploadDocuments: uploadDocuments? ->
                if (uploadDocuments != null && uploadDocuments.statusCode == 201) {
                    bottomSheetDialog?.dismiss()
                    uploadDocuments.data?.let { selectedIdentifierList.add(it) }
                    uploadDocuments.data?.let {
                        selectedImagesList.add(file)

                        Log.d("setRecyclerView", selectedImagesList.toString())

                        if (selectedImagesList.isNotEmpty()) {
                            binding.rvUploadDocument.visibility = View.VISIBLE
                        } else {
                            binding.rvUploadDocument.visibility = View.GONE
                        }

                        adaptorUploadDocuments?.notifyDataSetChanged()
                    } ?: run {
                        CommonUtils.toast(
                            requireActivity(),
                            "Failed to retrieve testScore. Please try again."
                        )
                    }
                } else {

                    CommonUtils.toast(requireActivity(), uploadDocuments?.message ?: "Failed")
                }
            }
        } else {
            CommonUtils.toast(requireActivity(), "File does not exist")
        }
    }


    private fun setupChatAdapter() {
        viewModel = ViewModelProvider(this)[SingleChatViewModel::class.java]
        adapterApplicationDetailConversation =
            AdapterApplicationDetailConversation(requireActivity(), chatRecords, entity)
        binding.rvMessages.adapter = adapterApplicationDetailConversation

        viewModel.connect()

        try {

            viewModel.joinChat(chatIdentifier ?: "")

        } catch (e: JSONException) {

            e.printStackTrace()
        }

        viewModel.newMessageLiveData.observe(viewLifecycleOwner) { newMessages ->
            newMessages?.let {
                Log.i("FragmentAgentChat", "Received new messages: $it")
                appendNewMessages(it)
            }
        }
    }

    private fun appendNewMessages(newMessages: List<Record>) {
        Log.d("appendNewMessages", newMessages.toString())
        val newMessagesWithoutDuplicates = newMessages.filter { newMessage ->
            chatRecords.none { it.identifier == newMessage.identifier }
        }

        if (newMessagesWithoutDuplicates.isNotEmpty()) {
            val oldSize = chatRecords.size
            chatRecords.addAll(newMessagesWithoutDuplicates)
            adapterApplicationDetailConversation.notifyItemRangeInserted(
                oldSize,
                newMessagesWithoutDuplicates.size
            )
            if (isAtBottom) {
                binding.rvMessages.post {
                    binding.rvMessages.smoothScrollToPosition(chatRecords.size - 1)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvMessages.layoutManager = layoutManager

        binding.rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val itemCount = layoutManager.itemCount

                // Check if we are at the bottom
                isAtBottom = (lastVisibleItemPosition == itemCount - 1)

                if (dy > 0 && !isLoading && lastVisibleItemPosition == chatRecords.size - 1 && currentPage < totalPages) {
                    currentPage--
                    fetchChatMessagesAndMetaData()
                }
                if (dy < 0 && firstVisibleItemPosition == 0 && !isLoading && currentPage > 1) {
                    currentPage++
                    fetchChatMessagesAndMetaData()
                }
            }
        })
    }

    private fun fetchChatMessagesAndMetaData() {
        isLoading = true

        Log.d("fetchChatMessagesAndMetaData", chatIdentifier ?: "")
        App.sharedPre!!.getString(AppConstants.User_IDENTIFIER, "")
            ?.let { Log.d("fetchChatMessagesAndMetaData", it) }

        viewModel.getChatResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            chatIdentifier ?: "",
            entity ?: "",
            currentPage.toString(),
            "desc"
        ).observe(viewLifecycleOwner) { getChatResponse: getChatResponse? ->
            isLoading = false

            getChatResponse?.let { response ->
                if (response.statusCode == 200 && response.success) {
                    val metaInfo = response.data?.metaInfo
                    val records = response.data?.records

                    binding.noChatFound.isVisible = records.isNullOrEmpty()

                    metaInfo?.let {
                        totalPages = it.lastPage
                        currentPage = it.page
                    }

                    records?.let {
                        if (currentPage == 1) {
                            chatRecords.clear()
                            adapterApplicationDetailConversation.notifyDataSetChanged()
                        }
                        chatRecords.addAll(it.reversed())
                        viewModel.newMessageLiveData.postValue(viewModel.arrayList)

                        adapterApplicationDetailConversation.notifyItemRangeInserted(
                            chatRecords.size - it.size,
                            it.size
                        )

                        isLastPage = currentPage == totalPages

                        binding.rvMessages.post {
                            if (currentPage == 1 || currentPage == totalPages) {
                                binding.rvMessages.scrollToPosition(chatRecords.size - 1)
                            }
                        }
                    }
                } else {
                    CommonUtils.toast(requireActivity(), response.message ?: "Failed")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AppState.isInChatScreen = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)

        if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

            ScoutMainActivity.bottomNav!!.isVisible = false

        } else {

            MainActivity.bottomNav!!.isVisible = false

        }

        viewModel.connect()
    }

    override fun onPause() {
        super.onPause()
        AppState.isInChatScreen = false
        viewModel.disconnect()
    }
}