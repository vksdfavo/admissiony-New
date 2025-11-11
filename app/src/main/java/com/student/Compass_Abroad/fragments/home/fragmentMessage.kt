@file:Suppress("DEPRECATION")

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.ChatMessageModels
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterUploadDocuments
import com.student.Compass_Abroad.databinding.FragmentMessageBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.widgets.Path
import com.student.Compass_Abroad.modal.getDocumentTypes.getDocumentTypes
import com.student.Compass_Abroad.modal.uploadDocuments.Data
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.retrofit.ViewModalClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.Callable

class fragmentMessage : BaseFragment() {

    private lateinit var binding: FragmentMessageBinding
    private var chatMessage= ""
    private var entity:String?=null
    var chatIdentifier:String?=null
    private var adaptorUploadDocuments: AdapterUploadDocuments? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var selectedImagesList = ArrayList<File>()
    private var selectedIdentifierList = ArrayList<Data>()
    private  var has_attachments = "no"
    private var arrayList = ArrayList<com.student.Compass_Abroad.modal.getDocumentTypes.RecordsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        chatIdentifier = App.singleton!!.identifierChat
        entity=App.singleton!!.idetity



        setUpWebView()
        clickListeners()
        addAttachment()
        getCategories(requireActivity())
        setRecyclerView()


        return binding.root
    }

    private fun addAttachment() {

        binding.checkboxAttachment.setOnClickListener {
            if (binding.checkboxAttachment.isChecked) {
                has_attachments = "yes"
                binding.rr1.visibility = View.VISIBLE

            } else {

                has_attachments = "no"
                binding.rr1.visibility = View.GONE

            }
        }

        binding.rr1.setOnClickListener {

            openBottomSheet()
        }

    }

    private fun setRecyclerView() {
        adaptorUploadDocuments = AdapterUploadDocuments(requireActivity(), selectedImagesList)
        binding.rvUploadDocument.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUploadDocument.adapter = adaptorUploadDocuments

    }

    private fun getCategories(
        requireActivity: FragmentActivity,
    ) {

        ViewModalClass().getDocumentsLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { getDocumentTypes: getDocumentTypes? ->
            getDocumentTypes?.let { nonNullcategoriesResponse ->
                if (getDocumentTypes.statusCode == 200) {

                    if (getDocumentTypes.data != null) {
                        for (i in 0 until getDocumentTypes!!.data!!.recordsInfo.size) {
                            arrayList.add(getDocumentTypes.data!!.recordsInfo[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve testScore. Please try again."
                        );
                    }

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDocumentTypes.message ?: " Failed"
                    )
                }
            }
        }

    }

    private fun openBottomSheet() {
        val view: View = LayoutInflater.from(requireActivity())
            .inflate(R.layout.bottom_sheet_upload_documents, binding.root, false)
        if (bottomSheetDialog == null) {
            bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheet2)
            bottomSheetDialog!!.setContentView(view)
            bottomSheetDialog!!.setCancelable(true)
            bottomSheetDialog!!.findViewById<View>(R.id.llBsUd_useCamera)!!.setOnClickListener {

                ImagePicker.with(this)
                    .cameraOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .crop()
                    .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    .start(101)
                binding.pbLoading.visibility=View.VISIBLE

                bottomSheetDialog?.dismiss()
            }

            bottomSheetDialog!!.findViewById<View>(R.id.llBsUd_gallery)!!.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R || CommonUtils.hasReadStoragePermission(
                        requireActivity()
                    )
                ) {
                    openSingleFileChooser()
                } else {
                    askStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            bottomSheetDialog!!.show()
        } else {
            bottomSheetDialog = null

            openBottomSheet()

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

    // image upload

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    101 -> {
                        val imageUri = data!!.data
                        val imageFile = File(Path.getRealPathFromCamera(requireActivity(), imageUri))
                        if (imageFile.exists()) {
                            val fileSize = imageFile.length()
                            if (fileSize < 4 * 1024 * 1024) {
                                binding.pbLoading.visibility=View.GONE
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
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.pbLoading.visibility=View.GONE
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
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
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (CommonUtils.hasReadStoragePermission(requireActivity())) {
                openSingleFileChooser()
            }
        }

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

    private fun clickListeners() {
        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.sendButton.setOnClickListener {

            binding.quillViewer.evaluateJavascript("document.getElementById('sendButton').click();", null)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.quillViewer.settings.javaScriptEnabled = true
        binding.quillViewer.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.quillViewer.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
                }
                return super.onConsoleMessage(consoleMessage)
            }
        }

        binding.quillViewer.addJavascriptInterface(WebAppInterface(), "Android")
        binding.quillViewer.webViewClient = object : WebViewClient() {
        }
        binding.quillViewer.loadUrl("file:///android_asset/quill.html")
        binding.sendButton.setOnClickListener {
            binding.quillViewer.evaluateJavascript("document.getElementById('sendButton').click();") { result ->


            }

        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun getTextFromTextArea(content: String) {
            chatMessage = content
            val title = binding.etTitle.text.toString()
            if (title.isEmpty()) {
                CommonUtils.toast(requireActivity(), "Title cannot be empty")
            } else if (chatMessage.isEmpty() || chatMessage == "<p><br></p>") {
                CommonUtils.toast(requireActivity(), "Message cannot be empty")
            } else {
                //postChatMessage(title, chatMessage.trim(), selectedIdentifierList)
            }
        }

        @JavascriptInterface
        fun setQuillContent(htmlContent: String) {
            Log.d("WebView2222", "Text from textarea: $htmlContent")
        }
    }
}