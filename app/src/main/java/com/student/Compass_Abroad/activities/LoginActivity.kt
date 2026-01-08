@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityLoginBinding
import com.student.bt_global.Utils.NeTWorkChange

class LoginActivity : AppCompatActivity() {
    var binding: ActivityLoginBinding? = null
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding!!.getRoot())

        val referralCode = intent.getStringExtra("referralCode")
        Log.d("referralCode", referralCode.toString())

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.login)

        val isReferred = !referralCode.isNullOrEmpty()
        val startDest = if (isReferred) R.id.signUpFragment2 else R.id.signInFragment
        navGraph.setStartDestination(startDest)

        val bundle = Bundle().apply {

            if (isReferred) {

                putString("referral", referralCode)

            }

        }

        navController.setGraph(navGraph, bundle)

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