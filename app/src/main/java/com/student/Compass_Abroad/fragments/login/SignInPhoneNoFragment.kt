package com.student.Compass_Abroad.fragments.login

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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


class SignInPhoneNoFragment : BaseFragment() {
    var binding:FragmentSignInPhoneNoBinding?=null
    var  hasDeviceIdentifier: String = ""
    var contentKey=""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInPhoneNoBinding.inflate(inflater, container, false)

        onClick()
        setTexts()

        return binding!!.getRoot()
    }

    private fun setTexts() {

        val spannableText = SpannableStringBuilder("Don't have an account? Sign Up")
        val yellowColor = ContextCompat.getColor(requireActivity(), R.color.theme_color)
        val yellowColorSpan = ForegroundColorSpan(yellowColor)
        spannableText.setSpan(yellowColorSpan, 22, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableText.setSpan(boldSpan, 22, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding!!.tvDontHaveAccount.text = spannableText
    }


    private fun onClick() {
        binding!!.tvNext.setOnClickListener{v: View?->

            checkUserApi()
        }
        binding!!.tvDontHaveAccount.setOnClickListener {
            if (isAdded) {
                try {
                    Navigation.findNavController(binding!!.root).navigate(R.id.signUpFragment2)
                } catch (e: Exception) {
                    Log.e("SignUpFragment", "Navigation error: ${e.message}", e)
                }
            } else {
                Log.e("SignUpFragment", "Fragment is not attached to an activity.")
            }
        }


    }
    private fun checkUserApi() {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val phone = binding!!.etPhone.text.toString()
        val countryCode = binding!!.countryCode.selectedCountryCode
        val phoneNumberE164 = "+$countryCode$phone"
        var isAvailableInList = false;
        App.sharedPre?.saveString(AppConstants.USER_EMAIL, phone)
        val userDevicesList: MutableList<PublicUserInfo> =
            App.sharedPre?.getUserList(AppConstants.USER_DEVICES) ?: mutableListOf()

        if (App.sharedPre!!.getUserList(AppConstants.USER_DEVICES)!!.isEmpty()) {
            App.sharedPre!!.saveUserList(AppConstants.USER_DEVICES, userDevicesList)
            hasDeviceIdentifier = "no";
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
        App.sharedPre?.saveString(AppConstants.PHONE, etPhone)
        App.sharedPre?.saveString(AppConstants.COUNTRY_CODE, etCode)

        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"

        hasDeviceIdentifier = if (App.sharedPre!!.getString(AppConstants.Device_IDENTIFIER, "") == "") {
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

            formData.put("device_identifier", App.sharedPre!!.getString(AppConstants.Device_IDENTIFIER, ""))
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
                            Navigation.findNavController(binding!!.root).navigate(R.id.verifyOtpFragment)

                            App.sharedPre!!.saveString(AppConstants.OTP, loginModal.data!!.oneTimePasswordInfo.otp)

                            App.sharedPre!!.saveString(AppConstants.OTP_IDENTIFIER, loginModal.data!!.oneTimePasswordInfo.identifier)

                            App.sharedPre!!.saveString(AppConstants.User_IDENTIFIER, loginModal.data!!.userInfo.identifier)

                            App.sharedPre!!.saveString(AppConstants.Device_IDENTIFIER, loginModal.data!!.userDeviceInfo.identifier)


                        } else {


                            with(App.sharedPre ?: return@observe) {
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


                            Navigation.findNavController(binding!!.root).navigate(R.id.passwordFragment)
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
