package com.student.Compass_Abroad.Scout.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ComponentActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.ContactUsActivity
import com.student.Compass_Abroad.activities.LoginActivity
import com.student.Compass_Abroad.activities.ProfileActivity
import com.student.Compass_Abroad.databinding.ActivityScoutMainBinding
import com.student.Compass_Abroad.fragments.home.ApplicationActiveFragment
import java.lang.ref.WeakReference
import androidx.navigation.findNavController

@Suppress("DEPRECATION")
class ScoutMainActivity : AppCompatActivity() {
    var binding: ActivityScoutMainBinding? = null
    var navController: NavController? = null
    val profileActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Handle the result and refresh data in MainActivity
            // hitApiUserDetails() // Refresh user details or perform other updates
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoutMainBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        navController = this.findNavController(R.id.main_nav)
        bottomNav = findViewById(R.id.bottom_navigation)

        drawer = findViewById(R.id.drawerLayout)


        activity = WeakReference(this)
        window.statusBarColor = getColor(android.R.color.white)

        val versionCode = BuildConfig.VERSION_NAME.takeIf { it?.isNotEmpty() == true } ?: "N/A"
        binding?.tv66?.text = "App Version  $versionCode"

        onClicks()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(android.R.color.black)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        }

        NavigationUI.setupWithNavController(bottomNav!!, navController!!)

        setSupportActionBar(binding!!.toolbarDa)

        bottomNav!!.setupWithNavController(navController!!)

        bottomNav!!.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scout_home_fragment -> {
                    ApplicationActiveFragment.Companion.data = null
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.scout_home_fragment)
                    true
                }

                R.id.fragProgramAllProg2 -> {
                    ApplicationActiveFragment.Companion.data = null
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.fragProgramAllProg2)
                    true
                }

                R.id.fragStudent -> {
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.fragStudent)
                    true
                }

                else -> false
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.Companion.getLang(newBase ?: return) ?: "en"
        val context = App.Companion.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }


    companion object {

        var bottomNav: BottomNavigationView? = null
        var drawer: DrawerLayout? = null
        @SuppressLint("RestrictedApi")
        var activity: WeakReference<ComponentActivity>? = null

    }

    private fun onClicks() {

        binding!!.tvLogout.setOnClickListener {
            binding!!.drawerLayout.close()
            showLogoutDialog()
        }

        binding!!.tvHelpSupport.setOnClickListener {

            startActivity(Intent(this@ScoutMainActivity, ContactUsActivity::class.java))

        }

        binding!!.civProfileImageFd2.setOnClickListener {

            binding!!.drawerLayout.open()

        }

        binding!!.tvMyProfileNav.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            profileActivityLauncher.launch(intent)
        }

        binding!!.tvMyStudentNav.setOnClickListener {
            this.findNavController(R.id.main_nav).navigate(R.id.fragStudent)
            binding!!.drawerLayout.close()
        }

        val versionCode =
            BuildConfig.VERSION_NAME?.toString().takeIf { it?.isNotEmpty() == true } ?: "N/A"
        binding?.tv66?.text = "App Version  $versionCode"

    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.logout_confirmation))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            startActivity(Intent(this@ScoutMainActivity, LoginActivity::class.java))
            finish()
            App.Companion.sharedPre?.clearPreferences()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onResume() {
        super.onResume()

        val imageUrl = App.Companion.sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')

        Glide.with(this)
            .load(imageUrl).error(R.drawable.test_image)
            .into(binding!!.civNavDrawer)
    }
}