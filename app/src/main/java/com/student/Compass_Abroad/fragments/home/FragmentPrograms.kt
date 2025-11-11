package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.adaptor.ProgramAdapter
import com.student.Compass_Abroad.databinding.FragmentProgramsBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getApplicationResponse.Record

class FragmentPrograms : BaseFragment() {

    private lateinit var binding: FragmentProgramsBinding

    var adapterPrograms: ProgramAdapter? = null

    companion object {

        var data: Record? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProgramsBinding.inflate(inflater, container, false)

        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setRecylerview(data)
        setRecylerview(data)


        return binding.root
    }

    private fun setRecylerview(data: Record?) {
        if (data?.allProgramInfo.isNullOrEmpty()) {
            // Show "No Data" layout
            binding.llSaaNoData.visibility = View.VISIBLE
            binding.rvFc.visibility = View.GONE
        } else {
            // Show RecyclerView and hide "No Data" layout
            binding.llSaaNoData.visibility = View.GONE
            binding.rvFc.visibility = View.VISIBLE

            adapterPrograms = ProgramAdapter(requireActivity(), data, object : ProgramAdapter.select {
                override fun onClick(position: Record?, position1: Int) {
                    fetchDataFromApi(position, position1)
                }
            })
            val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.rvFc.layoutManager = layoutManager
            binding.rvFc.adapter = adapterPrograms
        }
    }




    private fun fetchDataFromApi(record: Record?, position1: Int) {
        App.singleton!!.institution_id = data!!.latestInstitutionInfo.institution_id.toString()
        App.singleton!!.programId = data!!.allProgramInfo[position1].program_id.toString()
        App.singleton!!.campusId = data!!.latestInstitutionInfo.campus_id.toString()
        App.singleton!!.countryId = data!!.destination_country_id.toString()
        App.singleton!!.position =position1.toString()

        Log.d("fetchDataFromApi", "Destination country ID: ${data!!.destination_country_id}")
        Log.d("fetchDataFromApi", data!!.allProgramInfo[position1].program_id.toString())

        if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

            binding.root.findNavController().navigate(R.id.fragmentViewDetail2)


        } else {
            binding.root.findNavController().navigate(R.id.fragmentViewDetail)

        }



    }

}
