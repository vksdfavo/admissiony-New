package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.Scout.adaptor.ScoutShortListedAdapter
import com.student.Compass_Abroad.databinding.FragmentScoutShortlistedProgramBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragScoutShortlistedProgram : BaseFragment() {
   private lateinit var binding: FragmentScoutShortlistedProgramBinding
    var scoutShortlisedAdapter: ScoutShortListedAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentScoutShortlistedProgramBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        ScoutShortlistedAdapter()
        return binding.root
    }

    private fun ScoutShortlistedAdapter() {
        scoutShortlisedAdapter= ScoutShortListedAdapter()
        binding.rvFpAp.adapter=scoutShortlisedAdapter
    }


}