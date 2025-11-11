package com.student.Compass_Abroad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.adaptor.AdapterCounsellingTabs
import com.student.Compass_Abroad.adaptor.AdapterShorListedTabs
import com.student.Compass_Abroad.databinding.FragmentShortListBinding
import com.student.Compass_Abroad.fragments.home.CompareProgram
import com.student.Compass_Abroad.modal.AllProgramModel.Record


class ShortListFragment : BaseFragment() {
    private lateinit var binding: FragmentShortListBinding
    private var arrayList1 = ArrayList<Record>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShortListBinding.inflate(inflater, container, false)


        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.lll.setOnClickListener {
            if (arrayList1.isNotEmpty()) {
                CompareProgram.detail = arrayList1
            } else {
                Toast.makeText(requireActivity(), "No items to compare", Toast.LENGTH_LONG).show()
            }
        }


        setViewPager()

        return binding.root
    }


    private fun setViewPager() {
        val walletHVAdapter = AdapterShorListedTabs(childFragmentManager)
        binding.vpFc.adapter = walletHVAdapter
        binding.tlFc.setupWithViewPager(binding.vpFc)

        binding.vpFc.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d(
                    "ViewPager",
                    "Page scrolled to position: $position with offset: $positionOffset"
                )
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

}