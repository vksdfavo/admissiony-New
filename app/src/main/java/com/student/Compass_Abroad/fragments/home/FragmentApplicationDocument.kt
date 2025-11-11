package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.DocumentAdaptor
import com.student.Compass_Abroad.databinding.FragmentApplicationDocumentBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.retrofit.ViewModalClass


class FragmentApplicationDocument : BaseFragment() {
    private val recordInfoList: MutableList<com.student.Compass_Abroad.modal.getApplicationDocuments.RecordsInfo> = mutableListOf()

    private val viewModel: ViewModalClass by lazy { ViewModalClass() }

    companion object {
        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }

    private lateinit var binding: FragmentApplicationDocumentBinding
    private lateinit var documentAdaptor: DocumentAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentApplicationDocumentBinding.inflate(inflater, container, false)

        // Initialize RecyclerView and Adapter
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        setRecyclerView()
         setClickListeners()
        // Fetch data from API
        fetchDataFromApi()

        if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){

           binding.fabAddDocuments.visibility=View.GONE

        }else{

            binding.fabAddDocuments.visibility=View.VISIBLE

        }


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("uploadDocumentResult")?.observe(
            viewLifecycleOwner
        ) { result ->
            if (result) {
                fetchDataFromApi()
            }
        }


        return binding.root
    }



    private fun setClickListeners() {
        binding.fabAddDocuments.setOnClickListener { v:View->
            Navigation.findNavController(v).navigate(R.id.fragmentUploadDocuments)
        }
    }

    private fun setRecyclerView() {
        documentAdaptor = DocumentAdaptor(requireActivity(),recordInfoList)
        binding.rvFa.layoutManager = LinearLayoutManager(context)
        binding.rvFa.adapter = documentAdaptor
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDataFromApi() {
        binding.pbFadAs.visibility = View.VISIBLE

        data?.let {
            viewModel.getDocumentsResponseLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                it.identifier
            ).observe(viewLifecycleOwner, Observer { response ->
                binding.pbFadAs.visibility = View.GONE

                response?.let {
                    if (it.statusCode == 200 && it.success) {


                        val recordInfoListResponse = it.data?.recordsInfo ?: emptyList()


                        recordInfoList.clear()
                        recordInfoList.addAll(recordInfoListResponse)
                        documentAdaptor.notifyDataSetChanged()

                        if (recordInfoList.isEmpty()) {
                            binding.llSaaNoData.visibility = View.VISIBLE
                            binding.rvFa.visibility = View.GONE
                        } else {
                            binding.llSaaNoData.visibility = View.GONE
                            binding.rvFa.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUtils.toast(requireContext(), it.message ?: "Failed")
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvFa.visibility = View.GONE
                    }
                } ?: run {
                    binding.llSaaNoData.visibility = View.VISIBLE
                    binding.rvFa.visibility = View.GONE
                }
            })
        }
    }
}