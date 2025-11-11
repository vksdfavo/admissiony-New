package com.student.Compass_Abroad.fragments.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.viewpager2.widget.ViewPager2
import com.student.Compass_Abroad.adaptor.AdapterSignTabs
import com.student.Compass_Abroad.databinding.FragmentSignInBinding
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.fragments.BaseFragment


class SignInFragment : BaseFragment() {

    var binding: FragmentSignInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        setTabAdaptor()

        onTabCLickListener()
        App.singleton?.statusValidation = 1



        return binding!!.getRoot()
    }



    private fun onTabCLickListener() {

        binding!!.tlFa.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.vpFa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding!!.vpFa.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding!!.tlFa.selectTab(binding!!.tlFa.getTabAt(position))
            }
        })

    }

    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter = AdapterSignTabs(fragmentManager, lifecycle, binding!!.tlFa.tabCount)
        binding!!.vpFa.setOffscreenPageLimit(2)
        binding!!.vpFa.setAdapter(adapter)
    }

}