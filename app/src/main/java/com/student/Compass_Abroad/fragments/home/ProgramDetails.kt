package com.student.Compass_Abroad.fragments.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailTabs
import com.student.Compass_Abroad.databinding.FragmentProgramDetailsBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.fragments.BaseFragment


class ProgramDetails : BaseFragment() {

    var binding:FragmentProgramDetailsBinding?=null

    companion object{

        var details: Record?=null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentProgramDetailsBinding.inflate(inflater,container,false)
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setValues()

        val fragmentManager: FragmentManager = getChildFragmentManager()
        val adapter = AdapterProgramDetailTabs(fragmentManager, lifecycle, binding!!.tlFpd.tabCount)
        binding!!.vpFpd.setOffscreenPageLimit(2)
        binding!!.vpFpd.setAdapter(adapter)

        binding!!.tlFpd.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpFpd.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding!!.backBtn.setOnClickListener{

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding!!.vpFpd.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                binding!!.tlFpd.selectTab(binding!!.tlFpd.getTabAt(position))

            }
        })

        return binding!!.root

    }
    private fun setValues() {
        val programName = details?.program?.name
        val institutionLogoUrl = details?.program?.institution?.logo
        val institutionName = details?.program?.institution?.name
        val countryName = details?.program?.institution?.country?.name
        binding?.tvApdProgramName?.text = programName ?: "---"

        if (!institutionLogoUrl.isNullOrEmpty()) {
            Glide.with(binding!!.root)
                .load(institutionLogoUrl)
                .into(binding!!.ivApd)
        } else {

            binding!!.ivApd.setImageResource(R.drawable.z_el)

        }

        if (sharedPre!!.getString(AppConstants.PROGRAM_CATEGORY,"") == "career_program")
        {
            binding?.tvApdCollegeName!!.visibility=View.GONE

        }else{
            binding?.tvApdCollegeName!!.visibility=View.VISIBLE

        }

        binding?.tvApdCollegeName?.text = institutionName ?: "---"

        binding?.tvApdCollegeCountry?.text = countryName ?: "---"
    }
    override fun onResume() {
        super.onResume()

       if(sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

           ScoutMainActivity.bottomNav?.isVisible=true

       }else{

           MainActivity.bottomNav?.isVisible=false

       }
    }
}