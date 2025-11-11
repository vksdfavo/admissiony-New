package com.student.Compass_Abroad.Utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        lateinit var context: Context
            private set

        var sharedPre: SharedPrefs? = null
            private set

        var singleton: Singleton? = null
            private set

        // Track if app is in foreground
        var isAppInForeground: Boolean = false
            private set

        private var activityReferences = 0
        private var isActivityChangingConfigurations = false

        fun setLanguage(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }

        fun updateBaseContextLocale(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }
    }

    object AppState {
        var isInChatScreen: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        sharedPre = SharedPrefs(applicationContext)
        singleton = Singleton()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val lang = SharedPrefs.getLang(applicationContext) ?: "en"
        setLanguage(applicationContext, lang)

        // Register lifecycle callbacks to track foreground/background state
        registerActivityLifecycleCallbacks(this)
    }

    // Activity lifecycle callbacks to track foreground/background state
    override fun onActivityStarted(activity: Activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App entered foreground
            isAppInForeground = true
        }
    }

    override fun onActivityStopped(activity: Activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App entered background
            isAppInForeground = false
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
