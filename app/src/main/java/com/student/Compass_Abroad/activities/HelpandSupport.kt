package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityHelpandSupportBinding
import com.student.bt_global.Utils.NeTWorkChange

@Suppress("DEPRECATION")
class HelpandSupport : AppCompatActivity() {
    private lateinit var binding:ActivityHelpandSupportBinding
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHelpandSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = getColor(android.R.color.white)
        window.navigationBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        setclickListeners()

    }

    private fun setclickListeners() {
        binding.fabFpBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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