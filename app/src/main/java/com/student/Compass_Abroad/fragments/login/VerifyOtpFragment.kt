package com.student.Compass_Abroad.fragments.login
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentVerifyOtpBinding
import com.student.Compass_Abroad.encrytion.PasswordConverter
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.verifyOtp.VerifyOtp
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random


class VerifyOtpFragment : BaseFragment() {
    var binding: FragmentVerifyOtpBinding? = null
    var contentKey = ""
    private var email: String? = null
    private var requestFrom: String? = null
    var countDownTimer: CountDownTimer? = null
    private var public_key = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?, ): View {

        binding = FragmentVerifyOtpBinding.inflate  (inflater, container, false)
        App.singleton?.statusValidation = 0

        onClicks()

        if (arguments != null) {

            requestFrom = arguments?.getString("request_from")!!
        }


        email = sharedPre?.getString(AppConstants.USER_EMAIL, "")

        timerForOtp(binding)
        binding!!.tvResend.text = "Resend"

        otpFunction()

        binding!!.tvResend.setOnClickListener {

            apiCheckUser(email)

            binding!!.et1Verify.setText("")
            binding!!.et2Verify.setText("")
            binding!!.et3Verify.setText("")
            binding!!.et4Verify.setText("")
            binding!!.et5Verify.setText("")
            binding!!.et6Verify.setText("")

        }

        return binding!!.getRoot()

    }


    private fun otpFunction() {
        binding!!.et1Verify.addTextChangedListener(CustomTextWatcher(binding!!.et1Verify))
        binding!!.et2Verify.addTextChangedListener(CustomTextWatcher(binding!!.et2Verify))
        binding!!.et3Verify.addTextChangedListener(CustomTextWatcher(binding!!.et3Verify))
        binding!!.et4Verify.addTextChangedListener(CustomTextWatcher(binding!!.et4Verify))
        binding!!.et5Verify.addTextChangedListener(CustomTextWatcher(binding!!.et5Verify))
        binding!!.et6Verify.addTextChangedListener(CustomTextWatcher(binding!!.et6Verify))

        binding!!.et1Verify.setOnKeyListener(KeyListener(binding!!.et1Verify, null))

        binding!!.et2Verify.setOnKeyListener(KeyListener(binding!!.et2Verify, binding!!.et1Verify))
        binding!!.et3Verify.setOnKeyListener(KeyListener(binding!!.et3Verify, binding!!.et2Verify))
        binding!!.et4Verify.setOnKeyListener(KeyListener(binding!!.et4Verify, binding!!.et3Verify))
        binding!!.et5Verify.setOnKeyListener(KeyListener(binding!!.et5Verify, binding!!.et4Verify))
        binding!!.et6Verify.setOnKeyListener(KeyListener(binding!!.et6Verify, binding!!.et5Verify))


        val spannableString1 = SpannableString(
            "A 6 digit verification code has been sent to\n${
                sharedPre?.getString(
                    AppConstants.USER_EMAIL,
                    ""
                )
            }"
        )
        binding!!.tvCodeSend.text = spannableString1


    }

    private fun onClicks() {
        binding!!.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding!!.verifyBtn.setOnClickListener {
            val enteredOtp =
                binding!!.et1Verify.getText().toString() + binding!!.et2Verify.getText()
                    .toString() + binding!!.et3Verify.getText()
                    .toString() + binding!!.et4Verify.getText()
                    .toString() + binding!!.et5Verify.getText()
                    .toString() + binding!!.et6Verify.getText().toString()

            verifyOtp(enteredOtp)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    private inner class CustomTextWatcher(var view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                when (view) {
                    binding!!.et1Verify -> binding!!.et2Verify.requestFocus()
                    binding!!.et2Verify -> binding!!.et3Verify.requestFocus()
                    binding!!.et3Verify -> binding!!.et4Verify.requestFocus()
                    binding!!.et4Verify -> binding!!.et5Verify.requestFocus()
                    binding!!.et5Verify -> binding!!.et6Verify.requestFocus()
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    class KeyListener(private val currentView: EditText, private val previousView: EditText?) :
        View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (currentView.text.isEmpty() && previousView != null) {
                    // Move focus to the previous EditText
                    previousView.requestFocus()
                    previousView.setSelection(previousView.text.length)
                    return true
                }
            }
            return false
        }
    }


    private fun verifyOtp(enteredOtp: String) {
        val otp = enteredOtp

        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey

        val passwordConverter = PasswordConverter()

        val otp_value = otp
        val md5Otp = passwordConverter.convertPasswordToMD5(otp_value)

        println("MD5 OTP: $md5Otp")


        val jsonObject = JSONObject()
        if (requestFrom.equals("forgot")) {
            jsonObject.put("request_from", "forgot")

        } else {

            jsonObject.put("request_from", "signup")
        }

        jsonObject.put("otp_identifier", sharedPre!!.getString(AppConstants.OTP_IDENTIFIER, ""))
        jsonObject.put("username", sharedPre!!.getString(AppConstants.User_IDENTIFIER, ""))
        jsonObject.put("otp", md5Otp)

        val data = jsonObject.toString()
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret

        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            println("Encrypted data: $encryptedString")


        } else {

            println("Encryption failed.")

        }
        ViewModalClass().verifyOTPModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            contentKey
        ).observe(requireActivity()) { loginModal: VerifyOtp? ->
            loginModal?.let { nonNullLoginModal ->
                if (nonNullLoginModal.statusCode == 200) {
                    val nextBundle = Bundle().apply {
                        putString("email", email)
                        putString("request_from", requestFrom)
                    }

                    sharedPre!!.saveString(AppConstants.OTP, enteredOtp)
                    Navigation.findNavController(binding!!.getRoot())
                        .navigate(R.id.newPassordFragment2, nextBundle)

                } else {
                    CommonUtils.toast(
                        requireActivity(),
                        nonNullLoginModal.message ?: "Login Failed"
                    )
                }
            }
        }
    }

    private fun apiCheckUser(email: String?) {
        val hexString = generateRandomHexString(16)
        public_key = hexString
        val encryptedContent = CommonUtils.createContentCheckUser(
            requireActivity(),
            public_key,
            email,
            "signup"
        )
        var data = "$public_key^#^$encryptedContent"
        CommonUtils.showProgress(requireActivity())


        ViewModalClass().checkUserModelLiveData(requireActivity(), data)
            .observe(requireActivity()) { loginModal: CheckUserModel? ->
                loginModal?.let { nonNullLoginModal ->
                    if (nonNullLoginModal.statusCode == 200) {

                        CommonUtils.toast(requireActivity(), "OTP Send to Registered Email")

                        with(sharedPre!!) {

                            saveString(
                                AppConstants.OTP_IDENTIFIER,
                                loginModal.data!!.oneTimePasswordInfo.identifier
                            )
                            saveString(
                                AppConstants.User_IDENTIFIER,
                                loginModal.data!!.userInfo.identifier
                            )
                            saveString(
                                AppConstants.Device_IDENTIFIER,
                                loginModal.data!!.userDeviceInfo.identifier
                            )
                        }


                    } else {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullLoginModal.message ?: "Forget Failed"
                        )
                    }
                }
            }

    }

    private fun timerForOtp(binding: FragmentVerifyOtpBinding?) {
        val totalMillis = (30 * 1000).toLong()
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding!!.timer.text = String.format(
                    "%02d:%02d",
                    secondsRemaining / 30,
                    secondsRemaining % 30
                )
            }

            override fun onFinish() {
                binding!!.tvResend.visibility = View.VISIBLE
                binding.timer.visibility = View.GONE
                binding.timer.text = "00:00"
            }
        }

        countDownTimer!!.start()
    }
}


private fun generateRandomHexString(length: Int): String {
    val hexChars = "0123456789abcdef"
    return (1..length)
        .map { hexChars[Random.nextInt(hexChars.length)] }
        .joinToString("")
}
