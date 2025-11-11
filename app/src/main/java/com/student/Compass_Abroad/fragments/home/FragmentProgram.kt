package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramsTabs
import com.student.Compass_Abroad.databinding.FragmentProgramBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.student.Compass_Abroad.fragments.BaseFragment


class FragmentProgram : BaseFragment() {

    lateinit var binding: FragmentProgramBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgramBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.navigationBarColor =  requireActivity().getColor(R.color.appSecondaryColor)

        val fragmentManager = getChildFragmentManager()
        val adapter = AdapterProgramsTabs(fragmentManager, lifecycle, binding.tlFh.tabCount)
        binding.vpFp.setOffscreenPageLimit(2)
        binding.vpFp.setAdapter(adapter)

        //tab layout
        binding.tlFh.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpFp.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        binding.vpFp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tlFh.selectTab(binding.tlFh.getTabAt(position))
            }
        })

        binding.fabFpFilterStart.setOnClickListener {
          //  Navigation.findNavController(binding.root).navigate(R.id.programFilterFragment)
        }

        binding.fabFpHeart.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.shortListedFragment)
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = true
    }
}