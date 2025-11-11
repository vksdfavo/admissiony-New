@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.student.Compass_Abroad.BannerItem
import com.student.Compass_Abroad.OnBoardingBannerAdapter
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ActivityOnBoardingNewBinding
import kotlin.math.abs

class OnBoardingNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardingNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSkip.setOnClickListener {

            startActivity(Intent(this@OnBoardingNewActivity, LoginActivity::class.java))
            finish()
        }

        setupBannerViewPager()
        setupButtonActions()
    }

    private fun setupBannerViewPager() {
        val banners = listOf(
            BannerItem(R.drawable.one),
            BannerItem(R.drawable.two),
            BannerItem(R.drawable.three)
        )

        val adapter = OnBoardingBannerAdapter(banners)
        binding.viewPagerBanners.apply {
            this.adapter = adapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutDirection = ViewPager2.LAYOUT_DIRECTION_LTR

            // Slide animation
            setPageTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
                page.alpha = 0.5f + r * 0.5f
            }

            // Handle page changes
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val progress = when (position) {
                        0 -> 50
                        1 -> 75
                        2 -> 100
                        else -> 100
                    }

                    val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", progress)
                    animation.interpolator = AccelerateDecelerateInterpolator()
                    animation.duration = 750
                    animation.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            if (position == 2) {
                                // Last slide
                                binding.fabSkip.hide()
                                binding.progressBar.visibility = View.GONE
                                binding.tvGetStarted.visibility = View.VISIBLE
                            } else {
                                // Other slides
                                binding.tvGetStarted.visibility = View.INVISIBLE
                                binding.progressBar.visibility = View.VISIBLE
                                binding.fabSkip.show()
                            }
                        }
                    })
                    animation.start()
                }
            })
        }

        // Connect circle indicator
        binding.indicatorOb.setViewPager(binding.viewPagerBanners)
    }

    private fun setupButtonActions() {
        // Next page button
        binding.fabSkip.setOnClickListener {
            val nextItem = binding.viewPagerBanners.currentItem + 1
            if (nextItem < binding.viewPagerBanners.adapter!!.itemCount) {
                binding.viewPagerBanners.currentItem = nextItem
            }
        }

        // Get Started button
        binding.tvGetStarted.setOnClickListener {
            startActivity(Intent(this@OnBoardingNewActivity, LoginActivity::class.java))
            finish()
        }
    }
}
