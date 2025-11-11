package com.student.Compass_Abroad.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityEditProfileBinding
import com.student.Compass_Abroad.fragments.widgets.Path
import com.student.Compass_Abroad.modal.editProfile.EditProfile
import com.student.Compass_Abroad.modal.uploadDocuments.Data
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity() {
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    private lateinit var binding: ActivityEditProfileBinding
    var first_name: String = ""
    var last_name: String = ""
    var gender: String = ""
    var status: String = ""
    var dob: String = ""
    private var bottomSheetDialog: BottomSheetDialog? = null
    var STORAGE_PERMISSION_REQUEST_CODE: Int = 1378
    private var selectedImagesList = ArrayList<File>()
    private var selectedIdentifierList = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        onClick()


    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    private fun setUserData() {

        binding.etFirstName.setText(App.sharedPre!!.getString(AppConstants.FIRST_NAME, ""))

        binding.etLastName.setText(App.sharedPre!!.getString(AppConstants.LAST_NAME, ""))


        val birthday = App.sharedPre?.getString(AppConstants.DOB, "") ?: ""

        binding.dateOfBirth.text = when {
            birthday.isEmpty() || birthday == "0000-00-00" -> "select"
            else -> birthday
        }
    }


    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        setUserData()

        val imageUrl = App.sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')

        Glide.with(this).load(imageUrl).error(R.drawable.test_image).into(binding.profileImage)

    }

    private fun askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            } else {

                bottomSheetDialog?.hide()

            }

        } else {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            } else {
                bottomSheetDialog?.hide()
            }
        }
    }

    private fun onClick() {
        binding.editProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R || CommonUtils.hasReadStoragePermission(this@EditProfileActivity)) {
                ImagePicker.with(this@EditProfileActivity)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    .start(101)

            } else {
                askPermissions()
            }
        }


        binding.fabBiBack.setOnClickListener {


            onBackPressedDispatcher.onBackPressed()

        }

        genderSpinner(binding.gender)

        maritalSpinner(binding.maritalStatus)

        binding.dateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = format.format(calendar.time)
                    binding.dateOfBirth.text = formattedDate
                    dob = formattedDate.toString()

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        binding.update.setOnClickListener {

            first_name = binding.etFirstName.text.toString()
            last_name = binding.etLastName.text.toString()

            if (first_name.isEmpty()) {

                CommonUtils.toast(this, "Please enter first name")

            } else if (gender == "Select Gender") {

                CommonUtils.toast(this, "Please select gender")

            } else if (status == "Select Marital Status") {

                CommonUtils.toast(this, "Please select marital status")

            } else if (dob == null) {

                CommonUtils.toast(this, "Please select date of birth")

            } else {

                if (dob.isEmpty()) {

                    App.sharedPre!!.getString(AppConstants.DOB, "")
                        ?.let { it1 -> hitApi(first_name, last_name, gender, status, it1) }

                } else {

                    hitApi(first_name, last_name, gender, status, dob)
                }

            }

        }

    }

    private fun updateProfile(uploadImageUrl: String) {
        CommonUtils.accessToken?.let { token ->
            ViewModalClass().postProfileImagesLiveData(
                this,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer $token",
                uploadImageUrl
            ).observe(this) { postReminderResponse ->
                postReminderResponse?.let { response ->
                    when (response.statusCode) {
                        200 -> {
                            Log.d(
                                "updateProfileUrl",
                                postReminderResponse.data!!.profile_picture_url
                            )

                            App.sharedPre!!.saveString(
                                AppConstants.USER_IMAGE,
                                postReminderResponse.data.profile_picture_url
                            )
                            onBackPressedDispatcher.onBackPressed()

                            bottomSheetDialog?.dismiss()
                        }

                        409 -> {
                            CommonUtils.toast(
                                this,
                                response.message ?: "Profile picture already exists."
                            )
                            bottomSheetDialog?.dismiss()
                        }

                        else -> {
                            CommonUtils.toast(this, response.message ?: "Profile update failed.")
                        }
                    }
                } ?: run {
                    CommonUtils.toast(this, "Failed to update profile.")
                }
            }
        } ?: run {
            CommonUtils.toast(this, "Access token is missing.")
        }
    }

    private fun hitApi(
        firstName: String,
        lastName: String,
        gender: String,
        status: String,
        dob: String

    ) {
        ViewModalClass().editProfileLiveData(
            this,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            firstName, lastName, gender, status, dob
        ).observe(this) { editProfile: EditProfile? ->
            editProfile?.let {
                App.sharedPre!!.saveString(
                    AppConstants.USER_NAME,
                    "${editProfile.data?.updatedUserData?.first_name} ${editProfile.data?.updatedUserData?.last_name}"
                )

                CommonUtils.toast(this, "Profile  updated successfully.")

                onBackPressedDispatcher.onBackPressed()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun maritalSpinner(maritalStatusSpinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.marital_status,
            R.layout.custom_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        maritalStatusSpinner.adapter = adapter
        val savedMaritalStatus = App.sharedPre!!.getString(AppConstants.MARITAL_STATUS, "")

        val maritalStatusPosition = adapter.getPosition(savedMaritalStatus)
        if (maritalStatusPosition >= 0) {
            maritalStatusSpinner.setSelection(maritalStatusPosition)
        }
        maritalStatusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                status = maritalStatusSpinner.selectedItem.toString()
                val selectedGender = maritalStatusSpinner.selectedItem.toString()
                App.sharedPre?.saveString(AppConstants.MARITAL_STATUS, selectedGender)
                Log.d("Selected Status", status)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // If nothing is selected, set the default or first item
                maritalStatusSpinner.setSelection(0)
            }
        }
    }


    private fun genderSpinner(genderSpinner: Spinner) {
        val savedGender = App.sharedPre?.getString(AppConstants.GENDER, "") ?: ""


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_spinner,
            R.layout.custom_spinner_item
        ).also { spinnerAdapter ->

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        genderSpinner.adapter = adapter

        val genderPosition = adapter.getPosition(savedGender)

        if (genderPosition >= 0) {

            genderSpinner.setSelection(genderPosition)

        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                gender = genderSpinner.selectedItem.toString()
                App.sharedPre?.saveString(AppConstants.GENDER, gender)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optionally, set a default selection
                genderSpinner.setSelection(0)
            }
        }
    }

    private fun openSingleFileChooser() {
        ImagePicker.with(this@EditProfileActivity)
            .galleryOnly()
            .compress(512)
            .crop()
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
            .start(102)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    101 -> {
                        val imageUri = data!!.data
                        val imageFile = File(Path.getRealPathFromCamera(this, imageUri))
                        if (imageFile.exists()) {
                            val fileSize = imageFile.length()
                            if (fileSize < 1 * 1024 * 1024) {
                                postDocuments(this, imageFile.absolutePath)
                            } else {
                                CommonUtils.toast(
                                    this, "File size should be less than 1MB"
                                )
                            }
                        } else {
                            CommonUtils.toast(this, "File does not exist")
                        }
                    }

                    102 -> {
                        val imageUri = data?.data
                        if (imageUri != null) {
                            val imageFile =
                                File(Path.getRealPathFromGallery(this, imageUri))
                            val fileSize = imageFile.length()
                            if (fileSize < 1 * 1024 * 1024) {
                                postDocuments(this, imageFile.absolutePath)
                            } else {

                                CommonUtils.toast(this, "File size should be less than 1MB")
                            }
                        } else {
                            CommonUtils.toast(this, "Image URI is null")
                        }
                    }

                    2296 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                openSingleFileChooser()
                            } else {
                                CommonUtils.toast(this, "Permission denied.")
                            }
                        }
                    }
                }
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
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
                    updateProfile(uploadDocuments.data!!.fileInfo.view_page)
                    uploadDocuments.data?.let { selectedIdentifierList.add(it) }
                    uploadDocuments.data?.let {
                        selectedImagesList.add(file)
                    } ?: run {
                        CommonUtils.toast(
                            this,
                            "Failed to retrieve testScore. Please try again."
                        )
                    }
                } else {
                    CommonUtils.toast(this, uploadDocuments?.message ?: "Failed")
                }
            }
        } else {
            CommonUtils.toast(this, "File does not exist")
        }
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(neTWorkChange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(neTWorkChange)
        super.onStop()
    }
}