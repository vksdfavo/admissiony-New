@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterApplicationDetailTabs
import com.student.Compass_Abroad.databinding.FragmentApplicationDetailBinding
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.modal.getApplicationResponse.Record
import androidx.navigation.findNavController



class ApplicationDetail : Fragment() {
    private lateinit var binding: FragmentApplicationDetailBinding

    companion object {
        var data: Record? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        binding = FragmentApplicationDetailBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        setTabAdaptor()


        onTabCLickListener()

        /*if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

            binding?.rrChat?.visibility=View.GONE

        }else{

          binding?.rrChat?.visibility=View.VISIBLE

        }*/

        data?.let { onSetData(it) }
        onClickListeners()
        return binding.root
    }



    private fun onSetData(data: Record) {
        //binding.tvIADetailId.setText("Application ID: ${data.id}")
        val institutionData = data.latestInstitutionInfo.institution_data
        val campus = institutionData.campus
        val country = institutionData.country
        binding.tvApdProgramName.text =
            "${data.latestInstitutionInfo.institution_data.name}"
        val intakeName = data.intakeInfo?.intake_name ?: "0"
        val intakeYear = data.intakeInfo?.intake_year ?: ""
        var applicationFee = App.singleton!!.applicationFee ?: "0"

        if (data.allProgramInfo.isNotEmpty()) {
            binding.programName.text = data.allProgramInfo[0].program_data.name
        } else {
            binding.programName.text = "No program available"
        }

        binding.tvItemAaIntakeFee.text =
            "$intakeName $intakeYear"
        binding.applicationFeeNew.text =
                    "$applicationFee"

        binding.nameCountry.text="${campus},${country}"

    }

    private fun onClickListeners() {

        /*binding.civItemAaChat.setOnClickListener { v: View ->

            if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                App.singleton!!.idetity = "applications"
                v.findNavController().navigate(R.id.fragmentAgentChat2)

            } else {

                App.singleton!!.idetity = "applications"
                v.findNavController().navigate(R.id.fragmentAgentChat)
            }


        }*/

    }

    private fun onTabCLickListener() {
        binding.tlTa.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpTa.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        //view pager
        binding.vpTa.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding!!.tlTa.selectTab(binding!!.tlTa.getTabAt(position))
            }
        })

        binding.fabFadBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


    }

    private fun setTabAdaptor() {
        val fragmentManager = requireActivity().supportFragmentManager
        val adapter = AdapterApplicationDetailTabs(fragmentManager, lifecycle, binding!!.tlTa.tabCount)
        binding!!.vpTa.setOffscreenPageLimit(2)
        binding!!.vpTa.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

            ScoutMainActivity.bottomNav!!.isVisible = false

        } else {

            MainActivity.bottomNav!!.isVisible = false

        }



    }

}