package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.adaptor.AdapterScoutTimeline
import com.student.Compass_Abroad.databinding.FragmentFragScoutApplicationAssignedStaffBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragScoutApplicationAssignedStaff : BaseFragment() {
    private lateinit var binding: FragmentFragScoutApplicationAssignedStaffBinding
    private lateinit var adapterScoutTimeline: AdapterScoutTimeline

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentFragScoutApplicationAssignedStaffBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvFt.layoutManager = LinearLayoutManager(requireContext())
        adapterScoutTimeline = AdapterScoutTimeline()
        binding.rvFt.adapter = adapterScoutTimeline

    }

    override fun onResume() {
        super.onResume()
        ScoutMainActivity.bottomNav!!.visibility = View.GONE
    }
}