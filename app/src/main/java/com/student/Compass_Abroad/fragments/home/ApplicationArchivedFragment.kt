package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.student.Compass_Abroad.databinding.FragmentApplicationArchivedBinding
import com.student.Compass_Abroad.fragments.BaseFragment

class ApplicationArchivedFragment : BaseFragment() {
    var binding: FragmentApplicationArchivedBinding? = null
    //var adapterApplicationsActive: AdapterApplicationsActive? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicationArchivedBinding.inflate(inflater, container, false)
        //setApplicationArchivedRecyclerview(requireContext())

        // Inflate the layout for this fragment
        return binding!!.getRoot()
    }

    /*private fun setApplicationArchivedRecyclerview(context: Context) {
        adapterApplicationsActive = AdapterApplicationsActive(context, applicationList)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvFaArchived.setLayoutManager(layoutManager)
        binding!!.rvFaArchived.setAdapter(adapterApplicationsActive)
    }*/
}