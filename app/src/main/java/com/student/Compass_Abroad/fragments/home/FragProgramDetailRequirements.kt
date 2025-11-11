package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentFragProgramDetailRequirementsBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragProgramDetailRequirements : BaseFragment() {

    lateinit var binding: FragmentFragProgramDetailRequirementsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFragProgramDetailRequirementsBinding.inflate(inflater, container, false)


        setValues()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setValues() {
        val minRequirement  = ProgramDetails.details?.program?.min_requirement

        // Check if minRequirement is not null, not empty, and not blank
        if (minRequirement != null) {
            binding.tvFpdrText.text = minRequirement.toString()
        } else {
            // Handle case where minRequirement is null, empty, or only whitespace
            // For example, you might want to set a default text or hide the TextView
            binding.tvFpdrText.text = "No minimum requirements specified"
            // binding.tvFpdrText.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if(App.sharedPre?.getString(AppConstants.SCOUtLOGIN,"")=="true"){
            ScoutMainActivity.bottomNav!!.isVisible = false
        }else{
            MainActivity.bottomNav!!.isVisible = false
        }
    }
}
