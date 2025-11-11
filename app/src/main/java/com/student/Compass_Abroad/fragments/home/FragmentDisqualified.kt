package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.firmliagent.modal.getLeadModel.Record
import com.student.Compass_Abroad.adaptor.DisqualifiedAdapter
import com.student.Compass_Abroad.databinding.FragmentDisqualifiedBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragmentDisqualified : BaseFragment() {
    private lateinit var binding: FragmentDisqualifiedBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var disqualifiedAdapter: DisqualifiedAdapter

    private var  disqualifiedRecords = mutableListOf<Record>()

    private var currentPage = 1
    private var isLoading = false
    private var isScrolling = false
    private val dataPerPage = 10
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentDisqualifiedBinding.inflate(inflater,container,false)
        //setupRecyclerView()
        //disqualifiedLead(currentPage, dataPerPage)
        return binding.root
    }
    /*private fun setupRecyclerView() {
        disqualifiedAdapter = DisqualifiedAdapter(requireActivity(), disqualifiedRecords)
        layoutManager = LinearLayoutManager(requireActivity())
        binding.rvLd.layoutManager = layoutManager
        binding.rvLd.adapter = disqualifiedAdapter

        // Pagination: Load more items when reaching the end of the list
        binding.rvLd.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (isScrolling && !isLoading && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                    isScrolling = false
                    binding.pbFpApPagination.visibility = View.VISIBLE
                    disqualifiedLead(currentPage + 1, dataPerPage)
                }
            }
        })
    }*/

    /*private fun disqualifiedLead(pageNo: Int, dataPerPage: Int) {
        if (isLoading) return
        isLoading = true
        // Show progress bar

        ViewModalClass().getLeadResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            pageNo, // Assuming your API takes page as string
            dataPerPage,"disqualified" // Assuming your API takes per_page as string
        ).observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val leadResponse = it.data?.records
                    if (leadResponse.isNullOrEmpty()) {
                        *//*Toast.makeText(
                            requireActivity(),
                            "No Lead Records Found",
                            Toast.LENGTH_SHORT
                        ).show()*//*
                    } else {
                        disqualifiedRecords.addAll(leadResponse)
                        disqualifiedAdapter.notifyDataSetChanged()
                        currentPage = pageNo // Update currentPage only on successful load
                    }
                } else {
                    CommonUtils.toast(requireActivity(), it.message ?: "Failed")
                }
                isLoading = false
                binding.pbFpApPagination.visibility = View.GONE // Hide progress bar
            } ?: run {
                isLoading = false
                binding.pbFpApPagination.visibility = View.GONE // Hide progress bar
            }
        })
    }*/
}

