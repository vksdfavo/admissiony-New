package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.adaptor.AdapterScoutApplicationDetailTabs
import com.student.Compass_Abroad.databinding.FragmentScoutApplicationDetailBinding
import com.student.Compass_Abroad.databinding.FragmentScoutShortlistedProgramBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class ScoutApplicationDetail : BaseFragment() {
   private lateinit var binding:FragmentScoutApplicationDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentScoutApplicationDetailBinding.inflate(inflater,container,false)
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.theme_color)
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.theme_color)
        requireActivity().window.decorView.systemUiVisibility =0

        // Inflate the layout for this fragment
        setTabAdaptor()
        onTabCLickListener()
        return binding.root
    }
    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter = AdapterScoutApplicationDetailTabs(fragmentManager, lifecycle, binding!!.tlTa.tabCount)
        binding!!.vpTa.setOffscreenPageLimit(2)
        binding!!.vpTa.setAdapter(adapter)
    }
    private fun onTabCLickListener() {
        binding!!.tlTa.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpTa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //view pager
        binding!!.vpTa.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding!!.tlTa.selectTab(binding!!.tlTa.getTabAt(position))
            }
        })

        binding!!.fabFadBack.setOnClickListener { v: View ->
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


    }

}