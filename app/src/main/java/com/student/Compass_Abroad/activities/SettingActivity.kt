package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivitySettingBinding
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.bt_global.Utils.NeTWorkChange

@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {

    private var binding:ActivitySettingBinding?=null
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR


        binding!!.fabMySettingsBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.tvSettingsLoginToNewAccount.setOnClickListener {

            startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
            finish()
        }

        binding!!.changePasscode.setOnClickListener {
            startActivity(Intent(this@SettingActivity, ChangePasswordActivity::class.java))

        }
        binding!!.tvSettingsDeactivate.setOnClickListener {v:View->
            showDeativateDialog()

        }
        binding!!.tvSettingsContactUs.setOnClickListener {

            startActivity(Intent(this, ContactUsActivity::class.java))

        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

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
    fun showDeativateDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteAccount(this)
            App.sharedPre?.clearPreferences()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteAccount(
        requireActivity: FragmentActivity, ) {
        ViewModalClass().deleteAccountLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(this) { deleteResponse ->

            deleteResponse?.let { response ->
                val statusCode = response.statusCode
                if (statusCode == 200||statusCode==204) {
                    CommonUtils.toast(
                       this,
                        " Deleted Account successfully."
                    )
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    App.sharedPre?.clearPreferences()

                } else {

                    CommonUtils.toast(this, response.message)
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