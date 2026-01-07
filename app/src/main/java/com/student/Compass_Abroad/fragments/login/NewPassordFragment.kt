@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentNewPassordBinding
import com.student.Compass_Abroad.encrytion.PasswordConverter
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.forgotPasswordModel.ForgotPasswordModel
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random

class NewPassordFragment : BaseFragment() {
    var binding: FragmentNewPassordBinding? = null
    var num_password = 0
    var num_confirm_password = 0
    var contentKey=""
    private var email: String? = null
    private var requestFrom: String? = null
    private var otp: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPassordBinding.inflate(inflater, container, false)
        onClicks()

        arguments?.let {
            email = it.getString("email")

            requestFrom = it.getString("request_from")
            otp=App.sharedPre?.getString(AppConstants.OTP,"")



        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        )
        return binding!!.getRoot()
    }

    private fun onClicks() {

        binding!!.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding?.tvSp2Save?.setOnClickListener{
            val new_passcode = binding!!.etPasscode.text.toString().trim()
            val confirm_passcode = binding!!.confirmEtPasscode.text.toString().trim()

            if (new_passcode.isEmpty() || confirm_passcode.isEmpty()) {
                CommonUtils.toast(requireActivity(), "Please enter both password and confirm password")
                return@setOnClickListener
            }else if (new_passcode.length < 8) {
                CommonUtils.toast(requireActivity(), "Password must be at least 8 characters long")
                return@setOnClickListener
            }

            // Check if passwords match
            if (new_passcode != confirm_passcode) {
                CommonUtils.toast(requireActivity(), "Password and confirm password do not match")
                return@setOnClickListener
            }

            forgetOtp(new_passcode, confirm_passcode, otp)
        }
        binding!!.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
        binding!!.ibShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.etPasscode.selectionEnd
            if (num_password % 2 == 0) {
                binding!!.etPasscode.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.etPasscode.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_hide_password) }
            num_password++
            binding!!.etPasscode.setSelection(cursorPosition)
        }

        binding!!.ibHidePasscode.setOnClickListener {
            binding!!.etPasscode.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.etPasscode.setSelection(binding!!.etPasscode.text.length)
            binding!!.ibHidePasscode.visibility = View.GONE
            binding!!.ibShowPasscode.visibility = View.VISIBLE
        }

        binding!!.confirmIbShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.confirmEtPasscode.selectionEnd
            if (num_confirm_password % 2 == 0) {
                binding!!.confirmEtPasscode.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.confirmIbShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.confirmEtPasscode.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.confirmIbShowPasscode.setImageResource(R.drawable.ic_hide_password)
            }
            num_confirm_password++
            binding!!.confirmEtPasscode.setSelection(cursorPosition)
        }

        binding!!.confirmIBHidePasscode.setOnClickListener {
            binding!!.confirmEtPasscode.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.confirmEtPasscode.setSelection(binding!!.confirmEtPasscode.text.length)
            binding!!.confirmIBHidePasscode.visibility = View.GONE
            binding!!.confirmIbShowPasscode.visibility = View.VISIBLE
        }

    }



    private fun forgetOtp(newPasscode: String, confirmPasscode: String, otp: String?) {

        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey

        val passwordConverter = PasswordConverter()

        val otp_value = otp
        val md5Otp = otp_value?.let { passwordConverter.convertPasswordToMD5(it) }

        val password=newPasscode
        val confirmPassword=confirmPasscode
        val passwordMd5=passwordConverter.convertPasswordToMD5(password)
        val confirmPasswordPasswordMd5=passwordConverter.convertPasswordToMD5(confirmPassword)

        println("MD5 OTP: $md5Otp")


        val jsonObject = JSONObject()
        jsonObject.put("otp_identifier", App.sharedPre!!.getString(AppConstants.OTP_IDENTIFIER,""))
        jsonObject.put("username", App.sharedPre!!.getString(AppConstants.User_IDENTIFIER,""))
        jsonObject.put("password", passwordMd5)
        jsonObject.put("confirm_password", confirmPasswordPasswordMd5)
        jsonObject.put("otp", md5Otp)

        val data = jsonObject.toString();
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret

        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")
            Log.d("loginUser", contentKey)

        } else {

            println("Encryption failed.")

        }

        ViewModalClass().forgetPasswordModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            contentKey).observe(requireActivity()) { forgetModal: ForgotPasswordModel? ->
            forgetModal?.let { nonNullForgetModal ->
                if (nonNullForgetModal.statusCode == 200) {

                    with(App.sharedPre ?: return@observe) {

                        saveString(AppConstants.ISLOggedIn, "true")
                    }

                    CommonUtils.toast(
                        requireActivity(), "password created successfully"
                    )

                    Navigation.findNavController(binding!!.getRoot()).navigate(R.id.signInFragment)
                } else {
                    CommonUtils.toast(
                        requireActivity(),
                        nonNullForgetModal.message ?: "Reset Password Failed"
                    )
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



}