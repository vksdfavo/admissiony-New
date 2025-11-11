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
        val password = binding!!.etPassword.getText().toString()
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
                contentKey = "$publicKey^#^$encryptedString"
                println("Encrypted data: $encryptedString")
                Log.d("loginUser", contentKey)
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")
                    ?.let { Log.d("loginUser", it) }

            } else {

                println("Encryption failed.")

            }
            ViewModalClass().loginModalLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                contentKey
            ).observe(requireActivity()) { loginModal: LoginResponseModel? ->
                loginModal?.let { nonNullLoginModal ->
                    if (nonNullLoginModal.statusCode == 200) {

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


                        ViewModalClass().getPreferencesDataList(
                            requireActivity(),
                            AppConstants.fiClientNumber,
                            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                            "Bearer " + CommonUtils.accessToken,
                        ).observe(requireActivity()) { getPreferences ->
                            getPreferences?.let {
                                if (it.statusCode == 200) {
                                    sharedPre?.saveString(
                                        AppConstants.USER_PREFERENCES,
                                        getPreferences.data!!.preferencesInfo.destination_country
                                    )
                                    sharedPre?.saveString(
                                        AppConstants.USER_DISCIPLINES,
                                        getPreferences.data!!.preferencesInfo.disciplines.toString()
                                    )

                                    if (!getPreferences.data!!.hasAllPreferencesSet) {
                                        sharedPre!!.saveString(AppConstants.ISLOggedIn, "true")


                                        val intent = Intent(
                                            requireActivity(),
                                            SetPreferencesActivity::class.java
                                        )
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

                                            Log.d("onCreateViewLoginToken", token)
                                        }



                                        val intent =
                                            Intent(requireActivity(), MainActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
                                        sharedPre!!.saveString(AppConstants.ISLOggedIn, "true")

                                    }


                                } else {
                                    CommonUtils.toast(
                                        requireActivity(),
                                        it.message ?: "Something went wrong"
                                    )
                                }
                            }
                        }


                    } else {

                        errorDialogOpen(requireActivity(), nonNullLoginModal.message.toString())
                    }
                }
            }
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

