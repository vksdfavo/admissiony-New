package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.adaptor.AdapterScoutApplicationActive
import com.student.Compass_Abroad.databinding.FragmentScoutApplicationBinding
import com.student.Compass_Abroad.fragments.BaseFragment


@Suppress("DEPRECATION")
class FragmentScoutApplication : BaseFragment() {
    private lateinit var binding: FragmentScoutApplicationBinding
    var adapterScoutApplicationActive: AdapterScoutApplicationActive? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentScoutApplicationBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        binding.civFApplications.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        ApplicationActiveAdapter(binding!!)
        return binding!!.root

    }

    private fun ApplicationActiveAdapter(_binding: FragmentScoutApplicationBinding) {
        adapterScoutApplicationActive = AdapterScoutApplicationActive()
        _binding.rvFaActive?.adapter = adapterScoutApplicationActive
    }

    override fun onResume() {
        super.onResume()
        ScoutMainActivity.bottomNav!!.isVisible = true

    }

}