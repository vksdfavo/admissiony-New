package com.student.Compass_Abroad.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterVoucherHistoryTabs
import com.student.Compass_Abroad.adaptor.AdaptorHistoryList
import com.student.Compass_Abroad.databinding.FragmentHistoryBinding
import com.student.Compass_Abroad.modal.getHistoryListModel.RecordsInfo
import com.student.Compass_Abroad.modal.getHistoryListModel.getHistoryListModel
import com.student.Compass_Abroad.modal.getVouchersHistoryTabs.getVouchersHistoryTabs
import com.student.Compass_Abroad.retrofit.ViewModalClass


class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val arraylistTabs = ArrayList<com.student.Compass_Abroad.modal.getVouchersHistoryTabs.Record>()
    private val arrayListHistoryList = ArrayList<RecordsInfo>()
    private var adaptorHistoryList: AdaptorHistoryList? = null
    private var presentPage = 1
    private var dataPerPage = 6
    private var search: String? = null
    private var isLoading = false
    private var hasNextPage = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        getTabs(requireActivity())
        setupSearch()
        loadHistoryData()
        setupHistoryRecyclerView()
        return binding.root
    }

    private fun setupSearch() {
        binding.ibFdSearchStudent.setOnClickListener {
            search = binding.etSerach.text.toString()
            if (search.isNullOrEmpty()) {
                CommonUtils.toast(requireActivity(), "Please enter Voucher name")
            } else {
                resetPagination()
                loadHistoryData()
            }
        }

        binding.etSerach.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    search = null
                    resetPagination()
                    loadHistoryData()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun resetPagination() {
        presentPage = 1
        hasNextPage = true
        arrayListHistoryList.clear()
        adaptorHistoryList?.notifyDataSetChanged()
    }

    private fun setupHistoryRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager
        adaptorHistoryList = AdaptorHistoryList(requireActivity(), arrayListHistoryList)
        binding.rvHistory.adapter = adaptorHistoryList

        binding.rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (!rv.canScrollVertically(1) && !isLoading && hasNextPage) {
                    presentPage++
                    loadHistoryData()
                }
            }
        })
    }

    private fun loadHistoryData() {
        isLoading = true

        // Show loader based on first load or pagination
        if (presentPage > 1) {
            binding.pbFaActivePagination.visibility = View.VISIBLE
        } else {
            //binding.AcitivePagination.visibility = View.VISIBLE
        }

        ViewModalClass().getVouchersHistoryModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            presentPage,
            dataPerPage,
            search,
            "created_at",
            "DESC"
        ).observe(viewLifecycleOwner) { response: getHistoryListModel? ->
            isLoading = false
            binding.pbFaActivePagination.visibility = View.GONE
           // binding.paginationProgress.visibility = View.GONE

            response?.let { model ->
                if (model.statusCode == 200) {
                    val newData = model.data?.recordsInfo.orEmpty()
                    if (newData.isEmpty() && presentPage == 1) {
                        binding.rvHistory.visibility = View.GONE
                        binding.noVoucherFound.visibility = View.VISIBLE
                    } else {
                        binding.rvHistory.visibility = View.VISIBLE
                        binding.noVoucherFound.visibility = View.GONE
                        arrayListHistoryList.addAll(newData)
                        adaptorHistoryList?.notifyDataSetChanged()
                        hasNextPage = model.data?.metaInfo?.hasNextPage == true
                    }
                } else {
                    CommonUtils.toast(requireActivity(), model.message ?: "Something went wrong")
                }
            }
        }
    }

    private fun getTabs(requireActivity: FragmentActivity) {
        ViewModalClass().getVoucherHistoryTabsDataList(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(requireActivity) { response: getVouchersHistoryTabs? ->
            response?.let {
                if (it.statusCode == 200 && it.data != null) {
                    arraylistTabs.clear()
                    arraylistTabs.addAll(it.data!!.records)
                    setupTabsRecyclerView()
                } else {
                    CommonUtils.toast(requireActivity, it.message ?: "Failed to load tabs")
                }
            }
        }
    }

    private fun setupTabsRecyclerView() {
        val adapter = AdapterVoucherHistoryTabs(arraylistTabs) { selectedTab ->
            // Handle item click if needed
        }
        binding.rvTabs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTabs.adapter = adapter
    }
}