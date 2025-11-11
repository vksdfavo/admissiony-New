package com.student.Compass_Abroad.fragments.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterDocumentSelector
import com.student.Compass_Abroad.adaptor.AdapterUploadDocuments
import com.student.Compass_Abroad.databinding.FragmentUploadDocumentsBinding
import com.student.Compass_Abroad.fragments.widgets.Path
import com.student.Compass_Abroad.modal.getDocumentTypes.getDocumentTypes
import com.student.Compass_Abroad.modal.saveApplicationDocuments.FileData
import com.student.Compass_Abroad.modal.saveApplicationDocuments.SaveDocumentsRequest
import com.student.Compass_Abroad.modal.saveApplicationDocuments.saveApplicationDocuments
import com.student.Compass_Abroad.modal.uploadDocuments.Data
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.fragments.BaseFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.Callable


class FragmentUploadDocuments : BaseFragment() {

    private lateinit var binding: FragmentUploadDocumentsBinding
    private var arrayList = ArrayList<com.student.Compass_Abroad.modal.getDocumentTypes.RecordsInfo>()
    private var selectedImagesList = ArrayList<File>()
    private var selectedIdentifierList = ArrayList<Data>()
    private var fragment: Fragment? = null
    private var xLocationOfView = 0
    private var yLocationOfView = 0
    private var valueIdentifier = ""
    private var selectedCategoryIdentifier = ""
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var adaptorUploadDocuments: AdapterUploadDocuments? = null
    private var selector: String? = null


    companion object {

        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUploadDocumentsBinding.inflate(inflater, container, false)
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
        
        getCategories(requireActivity())
        clickListener()
        setRecyclerView()
        return binding.root
    }

    private fun setRecyclerView() {
        adaptorUploadDocuments = AdapterUploadDocuments(requireActivity(), selectedImagesList)
        binding.rvUploadDocument.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUploadDocument.adapter = adaptorUploadDocuments
    }

    private fun clickListener() {
        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressed()

        }
        binding.rr1.setOnClickListener {
            openBottomSheet()
        }
        binding.btnSaveDocument.setOnClickListener {
            selector = binding.tvAcpCountrySelector.text.toString()

            if (selector.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Please select a category.", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedImagesList.isEmpty()) {
                Toast.makeText(requireActivity(), "Please select a Document.", Toast.LENGTH_SHORT)
                    .show()
            } else {

                saveDocuments(requireActivity(), valueIdentifier!!, selectedIdentifierList)


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

            // Click listener for camera option in bottom sheet
            bottomSheetDialog!!.findViewById<View>(R.id.llBsUd_useCamera)!!.setOnClickListener {

                ImagePicker.with(this)
                    .cameraOnly()
                    .compress(1024)
                    .crop()
                    .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    .start(101)
                binding.pbLoading.visibility = View.VISIBLE

                bottomSheetDialog?.dismiss()
            }

            // Click listener for gallery option in bottom sheet
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

    // Request storage permission launcher
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

    // Function to open single file chooser
    private fun openSingleFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.setType("*/*")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, true)
        }
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*"))
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 102)
    }

    // Function to get document categories
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
                            arrayList?.add(getDocumentTypes.data!!.recordsInfo[i])
                        }
                    } else {
                        // Handle the case where data is null from the API response
                        CommonUtils.toast(
                            requireActivity,
                            "Failed to retrieve testScore. Please try again."
                        );
                    }

                    setDropDownCategories()

                } else {
                    CommonUtils.toast(
                        requireActivity,
                        getDocumentTypes.message ?: " Failed"
                    )
                }
            }
        }

    }

    private fun setDropDownCategories() {
        binding?.tvAcpCountrySelector?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Category")

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = binding!!.tvAcpCountrySelector.width

            // Calculate popup window position based on fragment view (if within a fragment)
            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                binding?.tvAcpCountrySelector?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvAcpCountrySelector?.height!!
            } else {
                // If not in a fragment, use activity view for positioning
                view?.getLocationOnScreen(locationOnScreen)
                xLocationOfView = locationOnScreen[0]
                yLocationOfView = locationOnScreen[1] + binding?.tvAcpCountrySelector?.height!!
            }


            // Set recycler view adapter
            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = AdapterDocumentSelector(requireActivity(), arrayList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedCampus ->
                binding?.tvAcpCountrySelector?.text = selectedCampus.label
                valueIdentifier = selectedCampus.value


                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(binding?.tvAcpCountrySelector)


            //search edit text
            layout.requireViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val enteredText = s.toString()
                        adapter.getFilter().filter(enteredText)
                    }
                })
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


    private fun saveDocuments(
        requireActivity: FragmentActivity,
        valueIdentifier: String,
        selectedImagesList: ArrayList<Data>, ) {


        val filesList = selectedImagesList.map {
            FileData(it.fileInfo.identifier ?: "", it.thumbInfo.identifier ?: "")
        }

        val request = SaveDocumentsRequest(
            filesList,
            valueIdentifier // Replace with your actual document type identifier
        )


        ViewModalClass().saveApplicatonDocumentsLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            data?.identifier.toString(),
            request

        ).observe(requireActivity) { saveApplicationDocuments: saveApplicationDocuments? ->
            if (saveApplicationDocuments != null && saveApplicationDocuments.statusCode == 201) {
                bottomSheetDialog?.dismiss()
                saveApplicationDocuments.data?.let {
                    if (isAdded) {
                        // After successful upload, set the result and navigate back
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "uploadDocumentResult",
                            true
                        )
                        findNavController().navigateUp()
                    }
                } ?: run {
                    CommonUtils.toast(
                        requireActivity(),
                        "Failed to retrieve testScore. Please try again."
                    )
                }
            } else {
                CommonUtils.toast(requireActivity(), saveApplicationDocuments?.message ?: "Failed")
            }
        }
    }


}