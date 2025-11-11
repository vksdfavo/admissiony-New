package com.student.Compass_Abroad.fragments.counselling

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.counselling.ScheduledAdapter
import com.student.Compass_Abroad.databinding.FragmentCompletedBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.counsellingModal.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass

class CompletedFragment : BaseFragment() {
    private lateinit var binding: FragmentCompletedBinding
    private var adapterScheduledAdapter: ScheduledAdapter? = null
    private val applicationList: MutableList<Record> = mutableListOf()
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    private var isRecyclerViewSetup = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedBinding.inflate(inflater, container, false)

        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.navigationBarColor)

        // Setup RecyclerView only once
        if (!isRecyclerViewSetup) {
            setApplicationActiveRecyclerview(requireActivity())
            isRecyclerViewSetup = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Fetch data only if list is empty (first load)
        if (applicationList.isEmpty()) {
            fetchDataFromApi()
        }
    }

    private fun setApplicationActiveRecyclerview(context: Context) {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFaActive.layoutManager = layoutManager

        adapterScheduledAdapter = ScheduledAdapter(requireActivity(), applicationList)
        binding.rvFaActive.adapter = adapterScheduledAdapter

        binding.rvFaActive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && hasNextPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        fetchDataFromApi()
                    }
                }
            }
        })
    }

    private fun fetchDataFromApi() {
        if (!hasNextPage || isLoading) return

        isLoading = true
        if (currentPage == 1) {
            binding.pbFaActive.visibility = View.VISIBLE
        } else {
            binding.pbFaActivePagination.visibility = View.VISIBLE
        }

        viewModel.getCounsellingResponseData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            App.sharedPre!!.getString(AppConstants.USER_IDENTIFIER, "")!!,
            "completed",
            currentPage,
            perPage,
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.records ?: emptyList()
                    applicationList.addAll(programResponse)
                    adapterScheduledAdapter?.notifyDataSetChanged()

                    hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                    if (hasNextPage) {
                        currentPage++
                    }

                    updateUIVisibility()
                } else {
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
                    binding.llFaActiveNoApplications.visibility = View.VISIBLE
                    binding.rvFaActive.visibility = View.GONE
                }
            } ?: run {
                binding.llFaActiveNoApplications.visibility = View.VISIBLE
                binding.rvFaActive.visibility = View.GONE
            }

            isLoading = false
            binding.pbFaActive.visibility = View.GONE
            binding.pbFaActivePagination.visibility = View.GONE
        }
    }

    private fun updateUIVisibility() {
        if (applicationList.isEmpty()) {
            binding.llFaActiveNoApplications.visibility = View.VISIBLE
            binding.rvFaActive.visibility = View.GONE
        } else {
            binding.llFaActiveNoApplications.visibility = View.GONE
            binding.rvFaActive.visibility = View.VISIBLE
        }
    }

    private fun refreshData() {
        // Reset pagination variables
        currentPage = 1
        hasNextPage = true
        isLoading = false

        // Clear existing data
        applicationList.clear()
        adapterScheduledAdapter?.notifyDataSetChanged()

        // Fetch fresh data
        fetchDataFromApi()
    }

    override fun onResume() {
        super.onResume()

        // Refresh data when returning from another fragment or when tab is selected
        refreshData()

        // Set navigation bar color
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.bottom_gradient_one)
    }
}