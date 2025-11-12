@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonDialog
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityProfileBinding
import com.student.Compass_Abroad.databinding.DialogCommonDialogBinding
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange
import java.util.Objects
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation.findNavController
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.Scout.activities.BankDetails
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.databinding.LangLayBinding
import com.student.Compass_Abroad.fragments.PaymentDetailFragment
import com.student.Compass_Abroad.fragments.PrivacyPolicyFragment
import com.student.Compass_Abroad.fragments.TermsAndConditionsFragment
import com.student.Compass_Abroad.modal.logoutUser.Logout

class ProfileActivity : AppCompatActivity() {
    var binding: ActivityProfileBinding? = null
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    private val alertDialog: AlertDialog? = null
    var dialogLang: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // System UI colors
        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Set window insets manually
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        // Button listener
        binding!!.editProfileUser.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
        }


        binding!!.changePassword.setOnClickListener {

            startActivity(Intent(this@ProfileActivity, ChangePasswordActivity::class.java))
        }

        val versionCode = BuildConfig.VERSION_NAME.takeIf { it?.isNotEmpty() == true } ?: "N/A"
        binding?.version?.text = "App Version  $versionCode"

        binding!!.backBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }

        binding!!.logout.setOnClickListener {

            showLogoutDialog()

        }

        binding?.tvSettings?.setOnClickListener {

            startActivity(Intent(this@ProfileActivity, SettingActivity::class.java))

        }
        binding?.tvBankDetails?.setOnClickListener {

            showCancelDialog(this@ProfileActivity)

        }

        binding!!.contactUs.setOnClickListener {

            startActivity(Intent(this@ProfileActivity, ContactUsActivity::class.java))

        }
        binding!!.terms.setOnClickListener {

            val fragment = TermsAndConditionsFragment()
            fragment.show(
                supportFragmentManager,
                PrivacyPolicyFragment::class.java.simpleName
            )
        }


        if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
            binding?.changePreferences?.visibility = View.GONE
            binding?.tvDocuments?.visibility = View.GONE
            binding?.viewDocuments?.visibility = View.GONE
            binding?.tvBankDetails?.visibility = View.VISIBLE

        } else {
            binding?.changePreferences?.visibility = View.VISIBLE
            binding?.tvDocuments?.visibility = View.VISIBLE
            binding?.viewDocuments?.visibility = View.VISIBLE
            binding?.tvBankDetails?.visibility = View.GONE
        }



        binding?.tvmyTransaction?.setOnClickListener {
            PaymentDetailFragment.data = ""
            startActivity(Intent(this, PaymentDetailFragment::class.java))

        }

        binding?.tvBankDetails?.setOnClickListener {
            val intent = Intent(this, BankDetails::class.java)
            startActivity(intent)
        }

        binding!!.changePreferences.setOnClickListener {
            var intent = Intent(this, SetPreferencesActivity::class.java)
            startActivity(intent)
        }

        binding!!.changeLanguange.setOnClickListener {

            langAlert()


        }


    }

    private fun showLogoutDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.logout_confirmation))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            callLogoutApi()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->

            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun callLogoutApi() {
        ViewModalClass().logoutLiveData(
            this,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(this) { createCounsellingModel: Logout? ->
            createCounsellingModel?.let { nonNullEditPostModal ->
                App.singleton?.SHOW_PASSCODE_SECTION = false
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                sharedPre?.clearPreferences()
            }
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }


    fun langAlert() {
        val dialogLang = Dialog(this)
        val langBinding = LangLayBinding.inflate(layoutInflater)
        dialogLang.setContentView(langBinding.root)

        dialogLang.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialogLang.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialogLang.window?.attributes = layoutParams


        langBinding.txtEn.setOnClickListener {
            SharedPrefs.setLang(this, "en")
            dialogLang.dismiss()
            refresh()
        }


        langBinding.txtTurkish.setOnClickListener {
            SharedPrefs.setLang(this, "tr")
            dialogLang.dismiss()
            refresh()
        }

        langBinding.txtArbic.setOnClickListener {
            SharedPrefs.setLang(this, "ar")
            dialogLang.dismiss()
            refresh()
        }

        dialogLang.show()
    }

    fun refresh() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()

        hitApiUserDetails()
    }

    private fun showCancelDialog(profileActivity: ProfileActivity) {

        val bindingDialog = DialogCommonDialogBinding.inflate(layoutInflater)
        val commonDialog = CommonDialog(profileActivity, bindingDialog)

        bindingDialog.noText.setOnClickListener {
            commonDialog.dismiss()
        }

        commonDialog.show()
    }

    private fun hitApiUserDetails() {
        ViewModalClass().getStaffProfileData(
            this, AppConstants.fiClientNumber, sharedPre?.getString(
                AppConstants.Device_IDENTIFIER, ""
            )!!, "Bearer " + CommonUtils.accessToken
        ).observe(this) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {
                    setUserData(staffData)
                    binding?.tvBasicInformation?.setOnClickListener {
                        val intent =
                            Intent(this@ProfileActivity, BasicInformationActivity::class.java)
                        intent.putExtra("userInfo", staffData.data?.userInfo)
                        startActivity(intent)
                    }

                    binding?.tvDocuments?.setOnClickListener {

                        val intent = Intent(this@ProfileActivity, MyDocumentActivity::class.java)
                        intent.putExtra(
                            "identifier",
                            staffData.data?.studentProfileInfo?.identifier
                        )
                        startActivity(intent)
                    }
                } else {
                    CommonUtils.toast(this, nonNullForgetModal.message ?: "Failed")
                }
            }
        }
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


            binding?.tvMyProfileRole?.text =
                "Student ID: " + staffData.data.studentProfileInfo?.student_id.toString() ?: "----"
            binding?.tvMyProfileContact?.text = contact ?: "----"
            binding?.tvMyProfileEmail?.text = userInfo.email ?: "----"

            val dob = userInfo.birthday?.toString() ?: ""
            if (dob.equals("0000-00-00")) {
                binding?.tvMyProfileDob?.text = "------"

            } else {
                binding?.tvMyProfileDob?.text = userInfo.birthday ?: "----"
            }
            binding?.gender?.text = userInfo.gender ?: "----"
            binding?.maritalStatus?.text = userInfo.marital_status ?: "----"

            sharedPre!!.saveString(AppConstants.FIRST_NAME, firstName)
            sharedPre!!.saveString(AppConstants.LAST_NAME, lastName)
            sharedPre!!.saveString(AppConstants.DOB, userInfo.birthday)
            sharedPre!!.saveString(AppConstants.USER_IMAGE, userInfo.profile_picture_url)
            sharedPre?.saveString(AppConstants.GENDER, userInfo.gender)
            sharedPre?.saveString(AppConstants.MARITAL_STATUS, userInfo.marital_status)


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