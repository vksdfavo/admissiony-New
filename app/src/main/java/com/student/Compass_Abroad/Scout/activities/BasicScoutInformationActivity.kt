package com.student.Compass_Abroad.Scout.activities

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityBasicInformationBinding
import com.student.Compass_Abroad.databinding.ActivityBasicScoutInformationBinding
import com.student.Compass_Abroad.modal.staffProfile.UserInfo
import com.student.bt_global.Utils.NeTWorkChange

class BasicScoutInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicScoutInformationBinding
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBasicScoutInformationBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(android.R.color.white)
        }

        // Set navigation bar color to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(android.R.color.white)
        }

        // Make sure the status bar and navigation bar icons are dark (to be visible on white background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        setContentView(binding.root)

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

        val userInfo = intent.getSerializableExtra("userInfo") as? UserInfo

        val mobile = userInfo!!.mobile.toString() ?: ""
        val countryCode = userInfo.country_code.toString()?: ""
        val contact = "+$countryCode $mobile"



        val email = userInfo?.email ?: "-----"
        val profilePic = userInfo?.profile_picture_url
        val firstName = userInfo?.first_name ?: "-----"
        val lastName = userInfo?.last_name ?: "-----"
        val gender = userInfo?.gender ?: "-----"
        val birthday = userInfo?.birthday ?: "-----"
        val martialStatus = userInfo?.marital_status ?: "-----"


        binding.tvBiEmail.text = email
        binding.tvBiName.text= "${firstName +" "}${ lastName}"
        binding.tvBiMobile.text = contact
        binding.tvBiGender.text = gender
        binding.tvBiDob.text = birthday
        binding.tvBiMaritalStatus.text = martialStatus

        binding.fabBiBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }

        if (profilePic.isNullOrEmpty()) {

            binding.civBi.setImageResource(R.drawable.test_image)

        } else {

            Glide.with(this).load(profilePic).into(binding.civBi)

        }
    }


    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
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