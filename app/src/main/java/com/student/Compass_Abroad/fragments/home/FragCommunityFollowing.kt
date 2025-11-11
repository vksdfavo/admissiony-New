package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterCommunityAllFeeds
import com.student.Compass_Abroad.databinding.FragmentFragCommunityFollowingBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getAllPosts.Record
import com.student.Compass_Abroad.modal.getAllPosts.getAllPostResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass

class FragCommunityFollowing : BaseFragment(),AdapterCommunityAllFeeds.select {
    private lateinit var binding: FragmentFragCommunityFollowingBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterCommunityAllFeeds: AdapterCommunityAllFeeds
    var arrayList1=ArrayList<Record>()
    var isScrolling = false
    private var isLoading = false
    var currentVisibleItems = 0
    var totalItemsInAdapter: Int = 0
    var scrolledOutItems: Int = 0
    private var dataPerPage = 15
    private var presentPage: Int = 1
    var nextPage: Int = 0
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { binding=FragmentFragCommunityFollowingBinding.inflate(inflater,container,false)
        binding.fabAfAddStu.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.fragAddCommunityPost)
        }

        arrayList1.clear();
        isLoading=false


        setupRecyclerView()
        // Load initial data
        loadInitialData()

        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.refreshTrigger.observe(viewLifecycleOwner) {
            refreshData()
        }
    }

    private fun onGetMyPost(
        requireActivity: FragmentActivity,
        presentPage: Int,
        dataPerPage: Int

    ) {

        ViewModalClass().getMyPostLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer "+ CommonUtils.accessToken,dataPerPage,presentPage).observe(requireActivity()) { allPostResponse: getAllPostResponse? ->
            allPostResponse?.let { nonNullForgetModal ->
                if (allPostResponse.statusCode == 200) {
                    binding.pbRecylerview.visibility = View.INVISIBLE
                    if(allPostResponse.data!!.records.size==0){
                        adapterCommunityAllFeeds.notifyDataSetChanged()
                        Toast.makeText(requireActivity,"No Posts Found", Toast.LENGTH_LONG).show()
                    }
                    else{
                        nextPage=allPostResponse?.data?.metaInfo!!.nextPage
                        for( i in 0 until allPostResponse.data!!.records.size){
                            arrayList1.add(allPostResponse.data!!.records[i])

                        }
                        if (isAdded) { // Check if fragment is added before accessing activity
                            setRecyclerView()
                        }

                    }

                } else {

                    if(isAdded){
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullForgetModal.message ?: " Failed"
                        )
                    }
                }
            }
        }


    }
    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFcAfFollowing.layoutManager = layoutManager
        adapterCommunityAllFeeds = AdapterCommunityAllFeeds(
            requireActivity(),
            arrayList1,
            this,
            this
        )
        binding.rvFcAfFollowing.adapter = adapterCommunityAllFeeds

        // Set up pagination listener
        binding.rvFcAfFollowing.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentVisibleItems = layoutManager.childCount
                totalItemsInAdapter = layoutManager.itemCount
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (scrolledOutItems + currentVisibleItems == totalItemsInAdapter)) {
                    isScrolling = false
                    loadMoreData()
                }
            }
        })
    }

    private fun loadInitialData() {
        if (!isLoading) {
            isLoading = true
            // Load initial data
            binding.pbRecylerview.visibility = View.VISIBLE
            onGetMyPost(requireActivity(), dataPerPage, presentPage)
        }
    }

    private fun loadMoreData() {
        if (!isLoading && presentPage < nextPage) {
            isLoading = true
            presentPage++
            binding.pbFcAfPagination.visibility = View.VISIBLE
            onGetMyPost(requireActivity(), dataPerPage, presentPage)
        }
    }


    private fun setRecyclerView() {
            adapterCommunityAllFeeds.notifyDataSetChanged()
            isLoading = false
            binding.pbFcAfPagination.visibility = View.INVISIBLE


    }
    private fun deletePost(
        requireActivity: FragmentActivity,
        identifier: String,
        position: Int,
    ) {

        ViewModalClass().deletePostLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            identifier
        ).observe(requireActivity()) { deleteResponse ->

            deleteResponse?.let { response ->
                val statusCode = response.statusCode
                if (statusCode == 204) {
                    CommonUtils.toast(
                        requireActivity(),
                        " deleted successfully."
                    )
                } else {

                    CommonUtils.toast(requireActivity(), response.message)
                }
            } ?: run {

                // Handle null response case (e.g., network error)
                arrayList1.clear()
                presentPage = 1

                if (isAdded) {
                    onGetMyPost(requireActivity(), dataPerPage, presentPage)
                }
            }
        }
    }

    override fun onCLick(record: Record,position:Int) {
        deletePost(requireActivity(), record.identifier,position)
        refreshData()


    }
    override fun onCLick2(record: Record, position: Int) {
        refreshData()
    }


    fun refreshData() {
        arrayList1.clear()
        presentPage = 1
        loadInitialData()
    }

    override fun onPause() {
        super.onPause()
        refreshData()
    }


}