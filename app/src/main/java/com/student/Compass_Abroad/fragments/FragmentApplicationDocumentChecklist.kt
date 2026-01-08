package com.student.Compass_Abroad.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.DocumentChecklistAdaptor
import com.student.Compass_Abroad.databinding.FragmentApplicationDocumentChecklistBinding
import com.student.Compass_Abroad.fragments.home.SharedViewModel
import com.student.Compass_Abroad.modal.getDocumentChecklistModal.Data
import com.student.Compass_Abroad.retrofit.ViewModalClass



class FragmentApplicationDocumentChecklist : BaseFragment() {

    private val recordInfoList: MutableList<Data> = mutableListOf()

    private val viewModel: ViewModalClass by lazy { ViewModalClass() }

    companion object {
        var data: String? = null
    }

    private lateinit var binding: FragmentApplicationDocumentChecklistBinding
    private lateinit var documentAdaptor: DocumentChecklistAdaptor
    private val sharedVM: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentApplicationDocumentChecklistBinding.inflate(inflater, container, false)


        sharedVM.documentChecklistTrigger.observe(viewLifecycleOwner) { shouldRefresh ->
            if (shouldRefresh && data != null) {
                fetchDataFromApi()   // <- no param needed
            }
        }

        return binding.root
    }


    private fun setRecyclerView(data: String?) {
        documentAdaptor = DocumentChecklistAdaptor(requireActivity(),recordInfoList,data)
        binding.rvFa.layoutManager = LinearLayoutManager(context)
        binding.rvFa.adapter = documentAdaptor
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDataFromApi() {
        data?.let {
            viewModel.getDocumentChecklist(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                it
            ).observe(viewLifecycleOwner, Observer { response ->
                response?.let { it ->
                    if (it.statusCode == 200 && it.success == true) {

                        val recordInfoListResponse = it.data ?: emptyList()

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


    override fun onResume() {
        super.onResume()
        if(data!=null){
            setRecyclerView(data)
        }

        fetchDataFromApi()

    }
}