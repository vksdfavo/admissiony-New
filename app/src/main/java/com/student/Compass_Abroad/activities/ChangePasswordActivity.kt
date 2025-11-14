package com.student.Compass_Abroad.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityChangePasswordBinding
import com.student.Compass_Abroad.encrytion.PasswordConverter
import com.student.Compass_Abroad.modal.forgotPasswordModel.ForgotPasswordModel
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange
import java.util.logging.Handler

class ChangePasswordActivity : AppCompatActivity() {
    var binding: ActivityChangePasswordBinding? = null
    var num_password = 0
    var num_confirm_password = 0
    var num_old_password = 0
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        WindowCompat.setDecorFitsSystemWindows(window, false)

        onClicks()
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

    }


    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }


    private fun onClicks() {

        binding?.backBtn?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding?.tvSp2Save?.setOnClickListener { v: View ->
            val old_passcode = binding!!.etOldPasscode.text.toString().trim()
            val new_passcode = binding!!.etPasscode.text.toString().trim()
            val confirm_passcode = binding!!.confirmEtPasscode.text.toString().trim()


            if (old_passcode.isEmpty() || new_passcode.isEmpty() || confirm_passcode.isEmpty()) {
                CommonUtils.toast(this, "Please enter all fields")
                return@setOnClickListener
            }

            if (old_passcode.length < 8 || new_passcode.length < 8) {
                CommonUtils.toast(this, "Password must be at least 8 characters long")
                return@setOnClickListener
            }


            if (new_passcode != confirm_passcode) {

                CommonUtils.toast(this, "Password and confirm password do not match")

                return@setOnClickListener

            }

            if (new_passcode != confirm_passcode) {

                CommonUtils.toast(this, "New password and confirm password do not match")

                return@setOnClickListener
            }

             forgetOtp(old_passcode, confirm_passcode,new_passcode)
        }


          // Show/Hide Old Password
        binding!!.ibOldShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.etOldPasscode.selectionEnd
            if (num_old_password % 2 == 0) {
                binding!!.etOldPasscode.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.ibOldShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.etOldPasscode.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.ibOldShowPasscode.setImageResource(R.drawable.ic_hide_password)
            }
            num_old_password++
            binding!!.etOldPasscode.setSelection(cursorPosition)
        }

        binding!!.ibHideOldPasscode.setOnClickListener {
            binding!!.etOldPasscode.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.etOldPasscode.setSelection(binding!!.etOldPasscode.text.length)
            binding!!.ibHideOldPasscode.visibility = View.GONE
            binding!!.ibOldShowPasscode.visibility = View.VISIBLE
        }

        // Show/Hide New Password

        binding!!.ibShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.etPasscode.selectionEnd
            if (num_password % 2 == 0) {
                binding!!.etPasscode.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.etPasscode.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_hide_password)
            }
            num_password++
            binding!!.etPasscode.setSelection(cursorPosition)
        }

        binding!!.ibHidePasscode.setOnClickListener {
            binding!!.etPasscode.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.etPasscode.setSelection(binding!!.etPasscode.text.length)
            binding!!.ibHidePasscode.visibility = View.GONE
            binding!!.ibShowPasscode.visibility = View.VISIBLE
        }

        // Show/Hide Confirm Password
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

    private fun forgetOtp(oldPasscode: String, confirmPasscode: String, new_passcode: String) {
        val passwordConverter = PasswordConverter()
        val confirmPassword=confirmPasscode
        val Password=confirmPasscode
        val oldpasswordMd5= passwordConverter.convertPasswordToMD5(oldPasscode)

        ViewModalClass().changePasswordModalLiveData(
            this,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            oldpasswordMd5,
            App.sharedPre!!.getString(AppConstants.User_IDENTIFIER,"")!!,
            confirmPassword,
            Password
        ).observe(this) { forgetModal: ForgotPasswordModel? ->
            forgetModal?.let { nonNullForgetModal ->
                if (nonNullForgetModal.statusCode == 200) {
                    onBackPressedDispatcher.onBackPressed()
                    CommonUtils.toast(this, "Password Changed Successfully")

                } else {
                    CommonUtils.toast(
                        this,
                        nonNullForgetModal.message ?: "Reset Password Failed"
                    )
                }
            }
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