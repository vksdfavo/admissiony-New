package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivitySplashBinding
import com.student.Compass_Abroad.onBoardingScreen.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private var isDeepLinkHandled = false
    private val splashDelayMillis: Long = 2000
    private val REQ_CODE_VERSION_UPDATE = 530

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if coming from notification FIRST, before any UI setup
        val fromNotification = intent?.getBooleanExtra("from_notification", false) ?: false
        Log.d("NotificationDebug", "From Notification Intent: $fromNotification")

        if (fromNotification) {
            Log.d("NotificationDebug", "Coming from notification - skipping splash screen")
            navigateDirectlyToMainActivity()
            return
        }

                                   // ✅ Init binding
        binding = ActivitySplashBinding.inflate(layoutInflater)

        // ✅ Set edge-to-edge BEFORE setting content view
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // ✅ Set content view
        setContentView(binding!!.root)

        // ✅ Apply insets AFTER view is set
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets

        // Rest of your splash logic
    }


    // Status & nav bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = getColor(R.color.splash_color)
            window.statusBarColor = getColor(R.color.splash_color)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        binding = ActivitySplashBinding.inflate(layoutInflater)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupSplashUI()
        checkForAppUpdate()
    }

    private fun navigateDirectlyToMainActivity() {
        val isLoggedIn = App.sharedPre?.getString(AppConstants.ISLOggedIn, "") == "true"
        val isScoutLogin = App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true"

        Log.d("SplashDebug", "Direct navigation - ISLOggedIn: $isLoggedIn, SCOUtLOGIN: $isScoutLogin")

        if (isLoggedIn) {
            val targetActivity = if (isScoutLogin) {
                ScoutMainActivity::class.java
            } else {
                MainActivity::class.java
            }
            val mainIntent = Intent(this, targetActivity).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = intent?.action
                data = intent?.data
                intent?.extras?.let {
                    Log.d("NotificationDebug", "Passing extras to MainActivity:")
                    it.keySet().forEach { key ->
                        Log.d("NotificationDebug", "  $key = ${it.get(key)}")
                    }
                    putExtras(it)
                }
            }
            startActivity(mainIntent)
            finish()
        } else {
            // User not logged in, but came from notification - still go to MainActivity
            // MainActivity should handle the case where user is not logged in
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = intent?.action
                data = intent?.data
                intent?.extras?.let { putExtras(it) }
            }
            startActivity(mainIntent)
            finish()
        }
    }

    private fun checkIfFromNotification(): Boolean {
        val fromNotification = intent?.getBooleanExtra("from_notification", false) == true
        Log.d("NotificationDebug", "From Notification: $fromNotification")
        return fromNotification
    }

    private fun setupSplashUI() {
        val flavor = BuildConfig.FLAVOR.lowercase()
        if (flavor == "admisiony") {
            binding!!.main.setBackgroundResource(R.drawable.splash_screen)
            Glide.with(this)
                .asGif()
                .load(R.drawable.splash)
                .into(binding!!.splashImageView)
        } else {
            binding!!.main.setBackgroundResource(R.drawable.splash_screen)
            binding!!.splashImageView.setImageDrawable(null)
        }
    }

    private fun handleDeepLink(): Boolean {
        val data: Uri? = intent?.data
        data?.let {
            val pathSegments = it.pathSegments
            if (pathSegments.size >= 2 && pathSegments[0] == "t") {
                val referralCode = pathSegments[1]
                isDeepLinkHandled = true
                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra("referralCode", referralCode)
                }
                startActivity(intent)
                finish()
                return true
            }
        }
        return false
    }

    private fun loadDataAfterSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (isDeepLinkHandled) return@postDelayed

            val isLoggedIn = App.sharedPre?.getString(AppConstants.ISLOggedIn, "") == "true"
            val isScoutLogin = App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true"

            Log.d("SplashDebug", "ISLOggedIn: $isLoggedIn, SCOUtLOGIN: $isScoutLogin")

            if (isLoggedIn) {
                val targetActivity = if (isScoutLogin) {
                    ScoutMainActivity::class.java
                } else {
                    MainActivity::class.java
                }
                val mainIntent = Intent(this@SplashActivity, targetActivity).apply {
                    action = intent?.action
                    data = intent?.data
                    intent?.extras?.let { putExtras(it) }
                }
                startActivity(mainIntent)
                finish()
            } else {
                saveLoginDefaults()
                startActivity(Intent(this@SplashActivity, OnBoardingNewActivity::class.java))
                finish()
            }
        }, splashDelayMillis)
    }

    private fun saveLoginDefaults() {
        val userDevicesList = App.sharedPre?.getUserList(AppConstants.USER_DEVICES) ?: emptyList()
        App.sharedPre?.saveUserList(AppConstants.USER_DEVICES, userDevicesList)

        listOf(
            AppConstants.fiClientNumber,
            AppConstants.privateKey,
            AppConstants.appSecret,
            AppConstants.ACCESS_TOKEN,
            AppConstants.REFRESH_TOKEN
        ).forEach { key ->
            val currentVal = App.sharedPre?.getString(key, "") ?: ""
            App.sharedPre?.saveString(key, currentVal)
        }
    }


    private fun checkForAppUpdate() {
        if (handleDeepLink()) return

        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        installStateUpdatedListener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showCompleteUpdateSnackbar()
            }
        }

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            when {
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                    appUpdateManager!!.registerListener(installStateUpdatedListener!!)
                    startAppUpdateFlexible(appUpdateInfo)
                }

                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                    startAppUpdateImmediate(appUpdateInfo)
                }

                else -> {
                    // No update, proceed with splash logic
                    loadDataAfterSplash()
                }
            }
        }.addOnFailureListener {
            loadDataAfterSplash() // On error, still proceed
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQ_CODE_VERSION_UPDATE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo, AppUpdateType.FLEXIBLE, this, REQ_CODE_VERSION_UPDATE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
            unregisterAppUpdateListener()
        }
    }

    private fun showCompleteUpdateSnackbar() {
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.restart) {
                appUpdateManager?.completeUpdate()
            }
            setActionTextColor(resources.getColor(R.color.theme_color))
        }.show()

        unregisterAppUpdateListener()
    }

    private fun unregisterAppUpdateListener() {
        appUpdateManager?.unregisterListener(installStateUpdatedListener!!)
    }

    override fun onDestroy() {
        unregisterAppUpdateListener()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        if (!isDeepLinkHandled) {
            handleDeepLink()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return)
        val updatedContext = App.updateBaseContextLocale(newBase, lang.toString())
        super.attachBaseContext(updatedContext)
    }
}