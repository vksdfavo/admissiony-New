@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.onBoardingScreen

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.LoginActivity
import com.student.Compass_Abroad.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    fun getViewPager2(): ViewPager2 {
        return binding.viewPager2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        //view pager
        val fragmentManager = supportFragmentManager
        val adapter = AdapterOnBoarding(fragmentManager, lifecycle, 3)
        binding.viewPager2.setAdapter(adapter)

        window.statusBarColor = getColor(android.R.color.white)

        window.navigationBarColor = getColor(android.R.color.white)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val progress = when (binding.viewPager2.currentItem) {
                    0 -> {
                        50
                    }
                    1 -> {
                        75
                    }
                    2 -> {
                        100
                    }
                    else -> {
                        100
                    }
                }

                val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", progress)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.setDuration(750)
                animation.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if ( binding.viewPager2.currentItem == 2) {

                            binding.fabSkip.hide()
                            binding.progressBar.visibility = View.GONE
                            binding.tvGetStarted.visibility = View.VISIBLE

                        } else {

                            binding.tvGetStarted.visibility = View.INVISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                            binding.fabSkip.show()

                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }
                })
                animation.start()
            }

        })


        binding.indicatorOb.setViewPager( binding.viewPager2)
        binding.fabSkip.setOnClickListener { binding.viewPager2.currentItem += 1 }
        binding.tvGetStarted.setOnClickListener {
            startActivity(Intent(
                this@OnBoardingActivity,
                    LoginActivity::class.java
                )
            )
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

    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}