package com.student.Compass_Abroad.fragments.login

import android.content.ContentValues
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentSignInPhoneNoBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.checkUserModel.PublicUserInfo
import org.json.JSONObject
import kotlin.random.Random
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.activities.SetPreferencesActivity
import com.student.Compass_Abroad.encrytion.PasswordConverter
import com.student.Compass_Abroad.fragments.PrivacyPolicyFragment
import com.student.Compass_Abroad.fragments.TermsAndConditionsFragment
import com.student.Compass_Abroad.modal.LoginResponseModel.LoginResponseModel
import com.student.Compass_Abroad.modal.TokenFcmData.TokenFcmData
import com.student.Compass_Abroad.retrofit.LoginViewModal
import kotlin.String


class SignInPhoneNoFragment : Fragment() {
    var binding:FragmentSignInPhoneNoBinding?=null
    var  hasDeviceIdentifier: String = ""
    var contentKey=""
    var contentKeyPassword = ""
    private var num_password = 0
    var token=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInPhoneNoBinding.inflate(inflater, container, false)

        onClick()
        setTexts()

        binding!!.tvForgotPasscode.setOnClickListener { v: View? ->
            if (App.singleton?.email == "0") {
                checkUserApiForEmail()
            } else {
                checkUserApiForPhone()
            }
        }

        return binding!!.getRoot()
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
            contentKeyPassword = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
        } else {
            println("Encryption failed.")
        }

        LoginViewModal().checkUserModelLiveData(requireActivity(), contentKeyPassword)
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
            contentKeyPassword = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
        } else {
            println("Encryption failed.")
        }

        LoginViewModal().checkUserModelLiveData(requireActivity(), contentKeyPassword)
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

    private fun setTexts() {
        val spannableText = SpannableStringBuilder("Don't have an account? Sign Up")
        val yellowColor = ContextCompat.getColor(requireActivity(), R.color.secondary_color)
        val yellowColorSpan = ForegroundColorSpan(yellowColor)
        spannableText.setSpan(yellowColorSpan, 22, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableText.setSpan(boldSpan, 22, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding!!.tvDontHaveAccount.text = spannableText
    }


    private fun onClick() {
        binding!!.tvDontHaveAccount.setOnClickListener {
            if (isAdded) {
                try {
                    App.singleton?.statusValidation = 1
                    binding!!.root.findNavController().navigate(R.id.signUpFragment2)
                } catch (e: Exception) {
                    Log.e("SignUpFragment", "Navigation error: ${e.message}", e)
                }
            } else {
                Log.e("SignUpFragment", "Fragment is not attached to an activity.")
            }
        }

        binding!!.btLoginPassword.setOnClickListener {

            loginUser()
        }

        binding!!.privacyPolicy.setOnClickListener {
            AppConstants.Login_STATUS_Value="1"
            val fragment = PrivacyPolicyFragment()
            fragment.show(
                (requireActivity() as AppCompatActivity).supportFragmentManager,
                PrivacyPolicyFragment::class.java.simpleName
            )

        }
        binding!!.terms.setOnClickListener {
            AppConstants.Login_STATUS_Value="1"
            val fragment = TermsAndConditionsFragment()
            fragment.show(
                (requireActivity() as AppCompatActivity).supportFragmentManager,
                PrivacyPolicyFragment::class.java.simpleName
            )
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
            binding!!.etPassword.setSelection(binding!!.etPassword.text.length)
            binding!!.ibHidePasscode.visibility = View.GONE
            binding!!.ibShowPasscode.visibility = View.VISIBLE
        }

        binding!!.tvNext.setOnClickListener{v: View?->

            checkUserApi()
        }
        binding!!.tvDontHaveAccount.setOnClickListener {
            if (isAdded) {
                try {
                    binding!!.root.findNavController().navigate(R.id.signUpFragment2)
                } catch (e: Exception) {
                    Log.e("SignUpFragment", "Navigation error: ${e.message}", e)
                }
            } else {
                Log.e("SignUpFragment", "Fragment is not attached to an activity.")
            }
        }

    }

    private fun loginUser() {
        val password = binding!!.etPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            errorDialogOpen(requireActivity(), "Password is required")
            binding!!.etPassword.requestFocus()
        } else {

            val hexString = generateRandomHexString(16)
            val publicKey = hexString
            val privateKey = AppConstants.privateKey

            val passwordConverter = PasswordConverter()

            val pass = password
            val md5Password = passwordConverter.convertPasswordToMD5(pass)

            println("MD5 Password: $md5Password")


            val jsonObject = JSONObject()
            jsonObject.put("username", sharedPre!!.getString(AppConstants.User_IDENTIFIER, ""))
            jsonObject.put("password", md5Password)

            val data = jsonObject.toString()
            val dataToEncrypt = data
            val app_secret = AppConstants.appSecret


            val ivHexString = "$privateKey$publicKey"
            val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

            if (encryptedString != null) {
                contentKeyPassword = "$publicKey^#^$encryptedString"
                println("Encrypted data: $encryptedString")
                Log.d("loginUser", contentKeyPassword)
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")
                    ?.let { Log.d("loginUser", it) }

            } else {

                println("Encryption failed.")

            }
            LoginViewModal().loginModalLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                contentKeyPassword
            ).observe(requireActivity()) { loginModal: LoginResponseModel? ->
                loginModal?.let { nonNullLoginModal ->
                    when (nonNullLoginModal.statusCode) {
                        200 -> {
                            with(sharedPre ?: return@observe) {
                                saveString(
                                    AppConstants.USER_EMAIL,
                                    nonNullLoginModal.data?.userInfo?.email
                                )
                                saveString(
                                    AppConstants.USER_NAME,
                                    "${nonNullLoginModal.data?.userInfo?.first_name} ${nonNullLoginModal.data?.userInfo?.last_name}"
                                )

                                sharedPre?.saveString(
                                    AppConstants.FIRST_NAME,
                                    "${nonNullLoginModal.data?.userInfo?.first_name}"
                                )
                                sharedPre?.saveString(
                                    AppConstants.LAST_NAME,
                                    "${nonNullLoginModal.data?.userInfo?.last_name}"
                                )
                                saveString(
                                    AppConstants.ACCESS_TOKEN,
                                    nonNullLoginModal.data?.tokensInfo?.accessToken
                                )
                                saveString(
                                    AppConstants.REFRESH_TOKEN,
                                    nonNullLoginModal.data?.tokensInfo?.refreshToken
                                )

                                saveString(
                                    AppConstants.User_IDENTIFIER,
                                    nonNullLoginModal.data?.userInfo?.identifier
                                )
                                saveString(
                                    AppConstants.PHONE,
                                    nonNullLoginModal.data?.userInfo?.mobile.toString()
                                )
                                saveModel(
                                    AppConstants.USER_ROLE,
                                    nonNullLoginModal.data?.activeIdentityInfo!!.identifier
                                )

                                saveString(
                                    AppConstants.COUNTRY_CODE,
                                    nonNullLoginModal.data.userInfo.country_code.toString()
                                )

                                saveModel(AppConstants.SAVE_MODAL, nonNullLoginModal.data)
                            }

                            LoginViewModal().getPreferencesDataList(
                                requireActivity(),
                                AppConstants.fiClientNumber,
                                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                                "Bearer " + CommonUtils.accessToken,
                            ).observe(requireActivity()) { getPreferences ->
                                getPreferences?.let {
                                    when (it.statusCode) {
                                        200 -> {
                                            sharedPre?.saveString(
                                                AppConstants.USER_PREFERENCES,
                                                it.data!!.preferencesInfo.destination_country.toString()
                                            )
                                            sharedPre?.saveString(
                                                AppConstants.USER_DISCIPLINES,
                                                it.data!!.preferencesInfo.disciplines.toString()
                                            )

                                            if (!it.data!!.hasAllPreferencesSet) {
                                                sharedPre!!.saveString(AppConstants.ISLOggedIn, "true")

                                                val intent = Intent(requireActivity(), SetPreferencesActivity::class.java)
                                                startActivity(intent)
                                                requireActivity().finish()
                                            } else {
                                                FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                                                    if (!task.isSuccessful) {
                                                        Log.w(
                                                            ContentValues.TAG,
                                                            "Fetching FCM registration token failed",
                                                            task.exception
                                                        )
                                                        return@addOnCompleteListener
                                                    }

                                                    token = task.result
                                                    Log.d("onCreateViewLoginToken", token)
                                                    sendFcmToken(token, requireActivity())
                                                }

                                                val intent = Intent(requireActivity(), MainActivity::class.java)
                                                startActivity(intent)
                                                requireActivity().finish()
                                                sharedPre!!.saveString(AppConstants.ISLOggedIn, "true")
                                            }
                                        }

                                        422 -> {
                                            // Handle invalid or missing preference data (session expired / data issue)
                                            App.singleton?.SHOW_PASSCODE_SECTION = false
                                            Log.w("getPreferencesDataList", "Received 422, refreshing fragment...")

                                            CommonUtils.toast(
                                                requireActivity(),
                                                it.message ?: "Something went wrong"
                                            )
                                            // Optionally clear login session to be safe
                                            sharedPre?.clearString(AppConstants.ISLOggedIn)

                                            // Refresh fragment via Navigation Component
                                            findNavController().navigate(findNavController().currentDestination!!.id)
                                        }

                                        else -> {
                                            CommonUtils.toast(
                                                requireActivity(),
                                                it.message ?: "Something went wrong"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        422 -> {
                            // Handle 422 status code
                            App.singleton?.SHOW_PASSCODE_SECTION = false

                            // Refresh fragment using Navigation Component
                            findNavController().navigate(findNavController().currentDestination!!.id
                            )
                        }

                        else -> {
                            errorDialogOpen(requireActivity(), nonNullLoginModal.message.toString())
                        }
                    }
                }
            }

        }
    }

    private fun checkUserApi() {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val phone = binding!!.etPhone.text.toString()
        val countryCode = binding!!.countryCode.selectedCountryCode
        val phoneNumberE164 = "+$countryCode$phone"
        var isAvailableInList = false

        sharedPre?.saveString(AppConstants.USER_EMAIL, phone)
        val userDevicesList: MutableList<PublicUserInfo> =
            sharedPre?.getUserList(AppConstants.USER_DEVICES) ?: mutableListOf()

        if (sharedPre!!.getUserList(AppConstants.USER_DEVICES)!!.isEmpty()) {
            sharedPre!!.saveUserList(AppConstants.USER_DEVICES, userDevicesList)
            hasDeviceIdentifier = "no"
        }

        try {
            val numberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phoneNumberE164, null)
            if (!phoneNumberUtil.isValidNumber(numberProto)) {
                binding!!.etPhone.error = "Invalid phone number"
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding!!.etPhone.error = "Invalid phone number"
            return
        }

        if (phone.isEmpty()) {
            binding!!.etPhone.error = "Phone number is required"
            return
        }

        val etPhone = binding!!.etPhone.text.toString()
        val etCode = binding!!.countryCode.selectedCountryCode
        sharedPre?.saveString(AppConstants.PHONE, etPhone)
        sharedPre?.saveString(AppConstants.COUNTRY_CODE, etCode)

        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        hasDeviceIdentifier = if (sharedPre!!.getString(AppConstants.Device_IDENTIFIER, "") == "") {
            "no"
        } else {
            "yes"
        }

        val formData = JSONObject()
        val deviceInfo = JSONObject()

        formData.put("data_type", "phone")
        formData.put("username", etPhone)
        formData.put("country_code", etCode)
        formData.put("has_device_identifier", hasDeviceIdentifier)

        if (hasDeviceIdentifier == "no") {
            deviceInfo.put("device_os", "android")
            deviceInfo.put("device_client", "app")
            deviceInfo.put("device_client_name", "student_app")
            deviceInfo.put("device_client_version", "v1.0.0.0-beta")
            formData.put("device_info", deviceInfo)
        } else {

            formData.put("has_device_identifier", "yes")

            formData.put("device_identifier", sharedPre!!.getString(AppConstants.Device_IDENTIFIER, ""))
        }

        val formDataToBeEncrypted = formData.toString()
        val encryptedString = encryptData(formDataToBeEncrypted, appSecret, ivHexString)

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
                        App.singleton?.email = "1"
                        if (loginModal.data?.oneTimePasswordInfo?.p_set == false || loginModal.data?.oneTimePasswordInfo?.v_set == false) {
                            binding!!.root.findNavController().navigate(R.id.verifyOtpFragment)

                            sharedPre!!.saveString(AppConstants.OTP, loginModal.data!!.oneTimePasswordInfo.otp)

                            sharedPre!!.saveString(AppConstants.OTP_IDENTIFIER, loginModal.data!!.oneTimePasswordInfo.identifier)

                            sharedPre!!.saveString(AppConstants.User_IDENTIFIER, loginModal.data!!.userInfo.identifier)

                            sharedPre!!.saveString(AppConstants.Device_IDENTIFIER, loginModal.data!!.userDeviceInfo.identifier)


                        } else {


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
                                    AppConstants.User_IDENTIFIER,
                                    nonNullLoginModal.data?.userInfo?.identifier
                                )

                                saveString(
                                    AppConstants.Device_IDENTIFIER,
                                    nonNullLoginModal.data?.userDeviceInfo?.identifier
                                )

                                if (!isAvailableInList) {
                                    userDevicesList.add(nonNullLoginModal.data?.publicInfo!!)
                                    saveUserList(AppConstants.USER_DEVICES, userDevicesList)

                                }
                                saveModel(AppConstants.SAVE_MODAL, nonNullLoginModal.data)
                            }
                        }


                        binding!!.btLoginPassword.visibility=View.VISIBLE
                        binding!!.llEnterPasscode.visibility=View.VISIBLE
                        binding!!.tvForgotPasscode.visibility=View.VISIBLE
                        binding!!.tvNext.visibility=View.GONE


                        //binding!!.root.findNavController().navigate(R.id.passwordFragment)

                    } else {
                        CommonUtils.toast(requireActivity(), nonNullLoginModal.message ?: "Login Failed")
                    }
                }
            }
    }


    fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }

            .joinToString("")
    }

    override fun onResume() {
        super.onResume()
        App.singleton?.statusValidation = 0

    }
}

private fun sendFcmToken(s: String?, activity: FragmentActivity?)
{
    ViewModalClass().sendFcmTokenLiveData(
        activity,
        AppConstants.fiClientNumber,
        sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
        "Bearer " + CommonUtils.accessToken,
        s.toString()
    ).observe(activity!!) { createCounsellingModel: TokenFcmData? ->
        createCounsellingModel?.let { nonNullEditPostModal ->

        }
    }
}

