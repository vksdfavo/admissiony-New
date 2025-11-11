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
import com.student.Compass_Abroad.databinding.FragmentFragScoutApplicationTimelineBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragScoutApplicationTimeline : BaseFragment() {

    private lateinit var binding: FragmentFragScoutApplicationTimelineBinding
    private lateinit var adapterTimeline: AdapterScoutTimeline

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentFragScoutApplicationTimelineBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvFt.layoutManager = LinearLayoutManager(requireContext())
        adapterTimeline = AdapterScoutTimeline()
        binding.rvFt.adapter = adapterTimeline

    }

    override fun onResume() {
        super.onResume()
        ScoutMainActivity.bottomNav!!.visibility = View.GONE
    }
}