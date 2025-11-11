package com.student.Compass_Abroad.fragments


import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

import com.student.Compass_Abroad.databinding.FragmentVouchersBinding


class FragmentVouchers : BaseFragment() {
    var binding: FragmentVouchersBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVouchersBinding.inflate(getLayoutInflater(), container, false)

        binding!!.backBtn.setOnClickListener {
             requireActivity().onBackPressedDispatcher.onBackPressed()
        }

       binding!!.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment =
                if (position == 0) BuyFragment() else HistoryFragment()
        }

        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, pos ->
            tab.text = if (pos == 0) "Buy" else "History"
        }.attach()
        return binding!!.getRoot()
    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}