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
import com.student.Compass_Abroad.adaptor.AdapterApplicationsTabs
import com.student.Compass_Abroad.databinding.FragmentApplicationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.fragments.BaseFragment
import androidx.navigation.findNavController

class ApplicationFragment : BaseFragment() {
    var binding: FragmentApplicationBinding? = null
    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplicationBinding.inflate(inflater, container, false)
        setTabAdaptor()

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.navigationBarColor =  requireActivity().getColor(R.color.appSecondaryColor)

        //tab layout
        onTabCLickListener()

        binding!!.fabCreateApplication.setOnClickListener{

            binding!!.root.findNavController().navigate(R.id.createApplicationFragment)

        }

        return binding!!.getRoot()
    }

    private fun onTabCLickListener() {

        binding!!.tlFa.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpFa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        //view pager
        binding!!.vpFa.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding!!.tlFa.selectTab(binding!!.tlFa.getTabAt(position))
            }
        })

        binding!!.civFApplications.setOnClickListener {

        }
        binding!!.fabFaNotification.findViewById<View>(R.id.fabFa_notification).setOnClickListener {

        }
    }

    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter = AdapterApplicationsTabs(fragmentManager, lifecycle, binding!!.tlFa.tabCount)
        binding!!.vpFa.setOffscreenPageLimit(2)
        binding!!.vpFa.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = true

    }

}