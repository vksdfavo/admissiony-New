@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityBasicInformationBinding

import com.student.Compass_Abroad.modal.staffProfile.UserInfo
import com.student.bt_global.Utils.NeTWorkChange

class BasicInformationActivity : AppCompatActivity() {
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    private lateinit var binding:ActivityBasicInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBasicInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

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

        binding.editProfile.setOnClickListener {

            startActivity(Intent(this@BasicInformationActivity, EditProfileActivity::class.java))

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

        val dob = userInfo.birthday ?: ""

        binding?.tvBiDob?.text = when {
            dob.isEmpty() || dob == "0000-00-00" -> "------"
            else -> dob
        }

        val martialStatus = userInfo?.marital_status ?: "-----"


        binding.tvBiEmail.text = email
        binding.tvBiName.text= "${firstName +" "}${ lastName}"
        binding.tvBiMobile.text = contact
        binding.tvBiGender.text = gender
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