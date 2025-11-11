package com.student.Compass_Abroad.Scout.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityScoutProfileBinding
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange

class ScoutProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoutProfileBinding
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoutProfileBinding.inflate(layoutInflater)

        setContentView(binding!!.getRoot())
        window.statusBarColor = getColor(R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(android.R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(android.R.color.white)
        }

        // Make sure the status bar and navigation bar icons are dark (to be visible on white background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        binding?.tvScoutBasicInformation?.setOnClickListener {
            val intent = Intent(this, BasicScoutInformationActivity::class.java)
            startActivity(intent)
        }

        binding?.tvBankDetails?.setOnClickListener {
            val intent = Intent(this, BankDetails::class.java)
            startActivity(intent)
        }
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

        hitApiUserDetails()

    }

    private fun hitApiUserDetails() {
        ViewModalClass().getStaffProfileData(
            this, AppConstants.fiClientNumber, App.sharedPre?.getString(
                AppConstants.Device_IDENTIFIER, ""
            )!!, "Bearer " + CommonUtils.accessToken
        ).observe(this) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {
                    setUserData(staffData)
                    binding?.tvScoutBasicInformation?.setOnClickListener {
                        val intent =
                            Intent(
                                this@ScoutProfileActivity,
                                BasicScoutInformationActivity::class.java
                            )
                        intent.putExtra("userInfo", staffData.data?.userInfo)
                        startActivity(intent)
                    }

                } else {
                    CommonUtils.toast(this, nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    private fun setUserData(staffData: StaffProfileModal) {
        staffData.data?.userInfo?.let { userInfo ->
            val firstName = staffData.data?.userInfo?.first_name ?: ""
            val lastName = staffData.data?.userInfo?.last_name ?: ""
            binding?.tvMyProfile?.text = "$firstName $lastName"

            val profilePictureUrl = staffData.data?.userInfo?.profile_picture_url
            if (!profilePictureUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(profilePictureUrl)
                    .into(binding!!.civMyProfile)
            } else {

                binding?.civMyProfile?.setImageResource(R.drawable.test_image)
            }

            val mobile = userInfo.mobile.toString() ?: ""
            val countryCode = userInfo.country_code.toString() ?: ""
            val contact = "+$countryCode $mobile"


            binding?.tvStaffId?.text = staffData.data.studentProfileInfo?.student_id.toString() ?: "----"
            binding?.tvMyProfileContact?.text = contact ?: "----"
            binding?.tvMyProfileEmail?.text = userInfo.email ?: "----"
            binding?.tvMyProfileDob?.text = userInfo.birthday ?: "----"

            App.sharedPre!!.saveString(AppConstants.FIRST_NAME, firstName)
            App.sharedPre!!.saveString(AppConstants.LAST_NAME, lastName)
            App.sharedPre!!.saveString(AppConstants.DOB, userInfo.birthday)
            App.sharedPre!!.saveString(AppConstants.USER_IMAGE, userInfo.profile_picture_url)
            App.sharedPre?.saveString(AppConstants.GENDER, userInfo.gender)
            App.sharedPre?.saveString(AppConstants.MARITAL_STATUS, userInfo.marital_status)


        }

        Log.d("setUserData: ", staffData.data?.studentProfileInfo?.identifier.toString())
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