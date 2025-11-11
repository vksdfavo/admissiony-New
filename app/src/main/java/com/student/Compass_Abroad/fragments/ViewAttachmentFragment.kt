package com.student.Compass_Abroad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.DocumentAdaptor
import com.student.Compass_Abroad.databinding.FragmentViewAttachmentBinding
import com.student.Compass_Abroad.modal.getApplicationDocuments.RecordsInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass

class ViewAttachmentFragment : BaseFragment() {
    private lateinit var binding: FragmentViewAttachmentBinding
    private lateinit var documentAdaptor: DocumentAdaptor
    private val recordInfoList: MutableList<RecordsInfo> = mutableListOf()
    private lateinit var viewModel: ViewModalClass
    private var chatIdentifier: String = ""
    private var entity: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding = FragmentViewAttachmentBinding.inflate(inflater, container, false)
        chatIdentifier = App.singleton?.chatidentifier ?: ""

        entity=App.singleton?.idetity ?: ""


        setupViewModel()

        setRecyclerView()

        fetchDataFromApi()

        binding.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        Log.d("chat_message", chatIdentifier)

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ViewModalClass::class.java)
    }

    private fun setRecyclerView() {

        documentAdaptor = DocumentAdaptor(requireActivity(),recordInfoList)
        binding.rvFa.layoutManager = LinearLayoutManager(context)
        binding.rvFa.adapter = documentAdaptor

    }

    private fun fetchDataFromApi() {
        binding.pbFadAs.visibility = View.VISIBLE

        viewModel.getViewAttachmentsModalClassData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            chatIdentifier,
            entity
        ).observe(viewLifecycleOwner) { response ->
            binding.pbFadAs.visibility = View.GONE

            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val newRecords = it.data?.recordsInfo ?: emptyList()

                    Log.d("API_Response", "Fetched records: ${newRecords.size}")
                    it.data?.recordsInfo ?: emptyList()

                    recordInfoList.clear()
                    recordInfoList.addAll(newRecords)
                    documentAdaptor.notifyDataSetChanged()

                    Log.d("API_Response", "Record list size after update: ${recordInfoList.size}")

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
        }
    }
}
