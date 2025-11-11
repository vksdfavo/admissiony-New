package com.student.Compass_Abroad.fragments.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdaptorViewDetailsTabs
import com.student.Compass_Abroad.databinding.FragmentViewDetailBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragmentViewDetail : BaseFragment() {

private lateinit var binding: FragmentViewDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentViewDetailBinding.inflate(inflater,container,false)

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            // Set the appearance to ensure dark icons on a light (white) status bar
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // For devices running below Android 11, use system UI flags
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setTabAdaptor()
        onTabCLickListener()
        onClickListeners()

        return binding.root
    }

    private fun onClickListeners() {
        binding.fabAcBack.setOnClickListener { v:View->
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
    }

    private fun onTabCLickListener() {
        binding!!.tlFa1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpFa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //view pager
        binding!!.vpFa.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding!!.tlFa1.selectTab(binding!!.tlFa1.getTabAt(position))
            }
        })

        //click listeners
        binding!!.civFApplications.setOnClickListener { v: View? -> }
        binding!!.fabFaNotification.findViewById<View>(R.id.fabFa_notification)
            .setOnClickListener { v: View? -> }
    }


    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter = AdaptorViewDetailsTabs(fragmentManager, lifecycle,binding!!.tlFa1.tabCount)
        binding.vpFa.offscreenPageLimit = 2  // Note: Use `offscreenPageLimit` instead of `setOffscreenPageLimit`
        binding.vpFa.adapter = adapter
        binding.vpFa.isUserInputEnabled = false

    }




    override fun onResume() {
        super.onResume()

        if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

            ScoutMainActivity.bottomNav!!.isVisible = false

        } else {

            MainActivity.bottomNav!!.isVisible = false

        }    }
}



