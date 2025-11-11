package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterTimeline
import com.student.Compass_Abroad.databinding.FragmentFragApplicationTimelineBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getApplicationTimelineResponse.Data
import com.student.Compass_Abroad.retrofit.ViewModalClass


class FragApplicationTimeline : Fragment() {
    private lateinit var  binding: FragmentFragApplicationTimelineBinding
    private lateinit var adapterTimeline: AdapterTimeline
    private val applicationTimelineList: MutableList<Data> = mutableListOf()

    companion object {
        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentFragApplicationTimelineBinding.inflate(inflater,container,false)


        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        setupRecyclerView()
        fetchDataFromApi()

        if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

            binding?.civItemAaChat?.visibility=View.GONE

        }else{

          binding?.civItemAaChat?.visibility=View.VISIBLE

        }

        onclick()

        return binding.root
    }

    private fun onclick() {
        binding.civItemAaChat.setOnClickListener { v: View ->

            if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                App.singleton!!.idetity = "applications"
                v.findNavController().navigate(R.id.fragmentAgentChat2)

            } else {

                App.singleton!!.idetity = "applications"
                v.findNavController().navigate(R.id.fragmentAgentChat)
            }


        }
    }

    override fun onResume() {
        super.onResume()

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE //

    }

    private fun setupRecyclerView() {
        binding.rvFt.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun fetchDataFromApi() {
        val viewModel = ViewModalClass()

        // Replace with your actual data identifier or handle null case
        val identifier = data?.identifier ?: ""

        Log.d("fetchDataFromApi", identifier)

        viewModel.getApplicationTimelineResponseResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            identifier
        ).observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                binding.pbAdTimeline.visibility = View.GONE // Hide progress bar

                if (it.statusCode == 200 && it.success) {
                    val leadResponse = it.data

                    if (leadResponse.isNullOrEmpty()) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvFt.visibility = View.GONE
                    } else {
                        binding.llSaaNoData.visibility = View.GONE
                        binding.rvFt.visibility = View.VISIBLE

                        applicationTimelineList.clear()
                        applicationTimelineList.addAll(leadResponse)

                        // Initialize adapter with fetched data
                        adapterTimeline = AdapterTimeline(activity, applicationTimelineList)
                        binding.rvFt.adapter = adapterTimeline

                        adapterTimeline.notifyDataSetChanged() // Notify adapter of dataset change
                    }
                } else {
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
                    binding.llSaaNoData.visibility = View.VISIBLE
                    binding.rvFt.visibility = View.GONE
                }
            } ?: run {
                binding.pbAdTimeline.visibility = View.GONE // Hide progress bar
                binding.llSaaNoData.visibility = View.VISIBLE
                binding.rvFt.visibility = View.GONE
            }
        })
    }

}