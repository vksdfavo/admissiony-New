package com.student.Compass_Abroad.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.adaptor.AdapterAssignedStaffTabs
import com.student.Compass_Abroad.databinding.ActivityAssignStaffBinding
import com.student.Compass_Abroad.fragments.BaseFragment

@Suppress("DEPRECATION")
class AssignStaffFragment : BaseFragment() {
    private lateinit var binding: ActivityAssignStaffBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityAssignStaffBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = requireActivity().getColor(android.R.color.white)
        requireActivity().window.navigationBarColor = requireActivity().getColor(android.R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR


        setViewPager()

        binding.fabBiBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        return binding.root
    }

    private fun setViewPager() {
        val titles = listOf(
            getString(R.string.my_staff),
            getString(R.string.applications)
        )

        val walletHVAdapter = AdapterAssignedStaffTabs(childFragmentManager, titles)
        binding.vpFc.adapter = walletHVAdapter
        binding.tlFc.setupWithViewPager(binding.vpFc)

        binding.vpFc.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.d("ViewPager", "Page scrolled to position: $position with offset: $positionOffset")
            }

            override fun onPageSelected(position: Int) {
                Log.d("ViewPager", "Page selected: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_IDLE -> Log.d("ViewPager", "Scrolling stopped")
                    ViewPager.SCROLL_STATE_DRAGGING -> Log.d("ViewPager", "Scrolling started")
                    ViewPager.SCROLL_STATE_SETTLING -> Log.d("ViewPager", "Scrolling settling")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }
}