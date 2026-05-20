package com.student.Compass_Abroad.fragments.login

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen

import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.activities.SetPreferencesActivity
import com.student.Compass_Abroad.databinding.FragmentPasswordBinding
import com.student.Compass_Abroad.encrytion.PasswordConverter
import com.student.Compass_Abroad.encrytion.encryptData

import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.PrivacyPolicyFragment
import com.student.Compass_Abroad.fragments.TermsAndConditionsFragment
import com.student.Compass_Abroad.modal.LoginResponseModel.LoginResponseModel
import com.student.Compass_Abroad.modal.TokenFcmData.TokenFcmData
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random

@Suppress("UNREACHABLE_CODE")
class PasswordFragment : BaseFragment() {
    var binding: FragmentPasswordBinding? = null
    private var num_password = 0
    var contentKey = ""
    var token=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentPasswordBinding.inflate(inflater, container, false)

        onClicks()

        binding!!.privacyPolicy.setOnClickListener {
            val fragment = PrivacyPolicyFragment()
            fragment.show(
                (requireActivity() as AppCompatActivity).supportFragmentManager,
                PrivacyPolicyFragment::class.java.simpleName
            )

        }

        binding!!.terms.setOnClickListener {

            val fragment = TermsAndConditionsFragment()
            fragment.show(
                (requireActivity() as AppCompatActivity).supportFragmentManager,
                PrivacyPolicyFragment::class.java.simpleName
            )

        }

        return binding!!.getRoot()

    }

    private fun onClicks() {
        val username = sharedPre?.getString(AppConstants.USER_NAME, "")
        if (username != null && username.isNotEmpty()) {
            val greetingMessage = "Hi! $username"
            binding?.tvTitleLogin?.text = greetingMessage
        } else {
            binding?.tvTitleLogin?.text = "Hi! Jenny"
        }

        val profileUrl = sharedPre?.getString(AppConstants.Profile_URL, "")

        if (profileUrl != null && profileUrl.isNotEmpty()) {
            Glide.with(this)
                .load(profileUrl)
                .placeholder(R.drawable.test_image2)
                .error(R.drawable.test_image2)
                .into(binding?.imgProfile!!)
        } else {
            binding?.imgProfile?.setImageResource(R.drawable.test_image2)
        }

        binding!!.ibShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.etPassword.selectionEnd
            if (num_password % 2 == 0) {
                binding!!.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.etPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_hide_password)
            }
            num_password++
            binding!!.etPassword.setSelection(cursorPosition)
        }

        binding!!.ibHidePasscode.setOnClickListener {
            binding!!.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.etPassword.setSelection(binding!!.etPassword.getText().length)
            binding!!.ibHidePasscode.setVisibility(View.GONE)
            binding!!.ibShowPasscode.setVisibility(View.VISIBLE)
        }

        binding!!.btLogin.setOnClickListener {

            loginUser()
        }

        binding!!.tvForgotPasscode.setOnClickListener { v: View? ->
            if (App.singleton?.email == "0") {
                checkUserApiForEmail()
            } else {
                checkUserApiForPhone()
            }
        }
    }

    private fun loginUser() {

        val password = binding?.etPassword?.text?.toString()?.trim()

        if (password.isNullOrEmpty()) {
            errorDialogOpen(requireActivity(), "Password is required")
            binding?.etPassword?.requestFocus()
            return
        }

        try {

            // 🔐 ENCRYPT PASSWORD
            val publicKey = generateRandomHexString(16)
            val privateKey = AppConstants.privateKey
            val md5Password = PasswordConverter().convertPasswordToMD5(password)

            Log.d("Encrypted Password:", "$publicKey^#^$privateKey^#^ $md5Password")

            val jsonObject = JSONObject().apply {
                put("username", sharedPre?.getString(AppConstants.User_IDENTIFIER, "") ?: "")
                put("password", md5Password)
            }

            val ivHexString = "$privateKey$publicKey"
            val encryptedString = encryptData(jsonObject.toString(), AppConstants.appSecret, ivHexString)

            if (encryptedString.isNullOrEmpty()) {

                errorDialogOpen(requireActivity(), "Encryption failed")
                return
            }

            val contentKeyPassword = "$publicKey^#^$encryptedString"



            // 🔹 LOGIN API
            LoginViewModal().loginModalLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                contentKeyPassword
            ).observe(viewLifecycleOwner) { loginResponse ->

                if (loginResponse?.statusCode != 200) {
                    errorDialogOpen(
                        requireActivity(),
                        loginResponse?.message ?: "Login failed"
                    )
                    return@observe
                }

                // 💾 SAVE LOGIN DATA
                with(sharedPre ?: return@observe) {

                    saveString(AppConstants.USER_EMAIL, loginResponse.data?.userInfo?.email)
                    saveString(
                        AppConstants.USER_NAME,
                        "${loginResponse.data?.userInfo?.first_name.orEmpty()} ${loginResponse.data?.userInfo?.last_name.orEmpty()}"
                    )

                    saveString(AppConstants.FIRST_NAME, loginResponse.data?.userInfo?.first_name)
                    saveString(AppConstants.LAST_NAME, loginResponse.data?.userInfo?.last_name)

                    saveString(AppConstants.ACCESS_TOKEN, loginResponse.data?.tokensInfo?.accessToken)
                    saveString(AppConstants.REFRESH_TOKEN, loginResponse.data?.tokensInfo?.refreshToken)

                    saveString(AppConstants.User_IDENTIFIER, loginResponse.data?.userInfo?.identifier)
                    saveString(
                        AppConstants.PHONE,
                        loginResponse.data?.userInfo?.mobile
                    )

                    saveString(
                        AppConstants.COUNTRY_CODE,
                        loginResponse.data?.userInfo?.country_code?.toString()
                    )

                    saveModel(
                        AppConstants.USER_ROLE,
                        loginResponse.data?.activeIdentityInfo?.identifier
                    )

                    saveModel(AppConstants.SAVE_MODAL, loginResponse.data)
                    saveString(AppConstants.ISLOggedIn, "true")
                }

                // 🔹 PREFERENCES API (FAIL SAFE)
                LoginViewModal().getPreferencesDataList(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer ${CommonUtils.accessToken}"
                ).observe(viewLifecycleOwner) { prefResponse ->

                    if (prefResponse?.statusCode == 200) {

                        val info = prefResponse.data?.preferencesInfo

                        sharedPre?.saveString(
                            AppConstants.USER_PREFERENCES,
                            (info?.destination_country)?.let { value ->
                                when (value) {
                                    is List<*> -> value.filterIsInstance<String>().joinToString(",")  // ✅ ["UK","USA"] → "UK,USA"
                                    is String  -> value                                                // ✅ "UK" → keep as-is
                                    else       -> ""
                                }
                            } ?: ""
                        )

                        sharedPre?.saveString(
                            AppConstants.STUDY_LEVEL,
                            (info?.preferred_study_level)?.let { value ->
                                when (value) {
                                    is List<*> -> value.filterIsInstance<String>().joinToString(",")  // ✅ ["UK","USA"] → "UK,USA"
                                    is String  -> value                                                // ✅ "UK" → keep as-is
                                    else       -> ""
                                }
                            } ?: ""
                        )

                        sharedPre?.saveString(
                            AppConstants.USER_DISCIPLINES,
                            (info?.disciplines ?: info?.discipline)?.let { value ->
                                when (value) {
                                    is List<*> -> value.filterIsInstance<String>().joinToString(",")  // ✅ ["Math","Science"] → "Math,Science"
                                    is String  -> value                                                // ✅ "Math,Science" → keep as-is
                                    else       -> ""
                                }
                            } ?: ""
                        )
                        Log.e("jdhjsjsj",prefResponse.data?.hasAllPreferencesSet.toString())

                        if (prefResponse.data?.hasAllPreferencesSet == false) {

                            startActivity(
                                Intent(requireActivity(), SetPreferencesActivity::class.java)
                            )

                        } else {

                            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    sendFcmToken(it.result, requireActivity())
                                }
                            }

                            startActivity(

                                Intent(requireActivity(), MainActivity::class.java)
                            )
                        }

                    } else {
                        // 🚀 Preferences failed → continue
//                            startActivity(
//                                Intent(requireActivity(), MainActivity::class.java)
//                            )
                    }



                    requireActivity().finish()
                }
            }

        } catch (e: Exception) {
            Log.e("LoginCrash", e.message ?: "Handled")
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun checkUserApiForEmail() {
        val email = sharedPre!!.getString(AppConstants.USER_EMAIL, "")
        val hexString = generateRandomHexString(16)
        var publicKey = hexString
        var privateKey = AppConstants.privateKey
        var app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        val formData = JSONObject()

        formData.put("request_from", "forgot")
        formData.put("data_type", "email")
        formData.put("username", email)
        formData.put("has_device_identifier", "yes")
        formData.put(
            "device_identifier",
            sharedPre!!.getString(AppConstants.Device_IDENTIFIER, "")
        ) // get from shared preference

        val formDataToBeEncrypted = formData.toString()
        val encryptedString = encryptData(formDataToBeEncrypted, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
        } else {
            println("Encryption failed.")
        }

        ViewModalClass().checkUserModelLiveData(requireActivity(), contentKey)
            .observe(requireActivity()) { loginModal: CheckUserModel? ->
                loginModal?.let { nonNullLoginModal ->
                    if (nonNullLoginModal.statusCode == 200) {

                        with(sharedPre ?: return@observe) {
                            saveString(AppConstants.LOGIN_STATUS, "0")


                            saveString(
                                AppConstants.User_IDENTIFIER,
                                nonNullLoginModal.data?.userInfo?.identifier
                            )
                            saveString(
                                AppConstants.Profile_URL,
                                nonNullLoginModal.data?.userInfo?.profile_picture_url
                            )
                            saveString(
                                AppConstants.USER_NAME,
                                nonNullLoginModal.data?.userInfo?.first_name
                            )

                            saveString(
                                AppConstants.Device_IDENTIFIER,
                                nonNullLoginModal.data?.userDeviceInfo?.identifier
                            )
                            saveString(
                                AppConstants.OTP_IDENTIFIER,
                                nonNullLoginModal.data?.oneTimePasswordInfo?.identifier
                            )

                            App.singleton!!.data =
                                "A 4 digit verification code has been sent to your email $email"

                            App.singleton?.OTP = nonNullLoginModal.data?.oneTimePasswordInfo?.otp

                            saveModel(AppConstants.SAVE_MODAL, nonNullLoginModal.data)
                        }

                        sharedPre!!.saveString(
                            AppConstants.OTP,
                            nonNullLoginModal.data?.oneTimePasswordInfo?.otp
                        )

                        sharedPre!!.saveString(
                            AppConstants.OTP_IDENTIFIER,
                            nonNullLoginModal.data?.oneTimePasswordInfo?.identifier
                        )
                        sharedPre!!.saveString(
                            AppConstants.User_IDENTIFIER,
                            nonNullLoginModal.data?.userInfo?.identifier
                        )
                        sharedPre!!.saveString(
                            AppConstants.Device_IDENTIFIER,
                            nonNullLoginModal.data?.userDeviceInfo?.identifier
                        )


                        val bundle = Bundle()
                        bundle.putString("request_from", "forgot")
                        bundle.putString("email", email)
                        Navigation.findNavController(binding!!.getRoot())
                            .navigate(R.id.verifyOtpFragment, bundle)


                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullLoginModal.message ?: "Forget Failed"
                        )
                    }
                }
            }

    }

    private fun checkUserApiForPhone() {
        val et_phone = sharedPre!!.getString(AppConstants.PHONE, "")
        val et_code = sharedPre!!.getString(AppConstants.COUNTRY_CODE, "")


        val hexString = generateRandomHexString(16)
        var publicKey = hexString
        var privateKey = AppConstants.privateKey
        var app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"


        //form data with email login code start
        val formData = JSONObject()

        formData.put("request_from", "forgot")
        formData.put("data_type", "phone")
        formData.put("username", et_phone)
        formData.put("country_code", et_code)
        formData.put("has_device_identifier", "yes")
        formData.put(
            "device_identifier",
            sharedPre!!.getString(AppConstants.Device_IDENTIFIER, "")
        ) // get from shared preference

        val formDataToBeEncrypted = formData.toString()
        val encryptedString = encryptData(formDataToBeEncrypted, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
        } else {
            println("Encryption failed.")
        }

        ViewModalClass().checkUserModelLiveData(requireActivity(), contentKey)
            .observe(requireActivity()) { loginModal: CheckUserModel? ->
                loginModal?.let { nonNullLoginModal ->
                    if (nonNullLoginModal.statusCode == 200) {

                        with(sharedPre ?: return@observe) {
                        }

                        Navigation.findNavController(binding!!.getRoot())
                            .navigate(R.id.verifyOtpFragment)


                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullLoginModal.message ?: "Forget Failed"
                        )
                    }
                }
            }
    }
}

private fun generateRandomHexString(length: Int): String {
    val hexChars = "0123456789abcdef"
    return (1..length)
        .map { hexChars[Random.nextInt(hexChars.length)] }
        .joinToString("")


}


private fun sendFcmToken(s: String?, activity: FragmentActivity?) {
    ViewModalClass().sendFcmTokenLiveData(
        activity,
        AppConstants.fiClientNumber,
        App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
        "Bearer " + CommonUtils.accessToken,
        s.toString()
    ).observe(activity!!) { createCounsellingModel: TokenFcmData? ->
        createCounsellingModel?.let { nonNullEditPostModal ->

        }
    }
}