package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdaptorAllComments
import com.student.Compass_Abroad.databinding.FragmentCommentsBinding

import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.getAllComments.Record
import com.student.Compass_Abroad.modal.getAllComments.getAllComments
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.util.ArrayList


class FragmentComments : BaseFragment(), AdaptorAllComments.select {
    private lateinit var binding: FragmentCommentsBinding
    var arrayListPostComment = ArrayList<com.student.Compass_Abroad.modal.getAllComments.Record>()

    var isScrolling = false
    var isLoading = false

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adaptorComments: AdaptorAllComments
    var currentVisibleItems = 0
    var totalItemsInAdapter: kotlin.Int = 0
    var scrolledOutItems: kotlin.Int = 0
    var dataPerPage = 9
    var presentPage: kotlin.Int = 1
    var nextPage: kotlin.Int = 0

    companion object {
        var post: com.student.Compass_Abroad.modal.getAllPosts.Record? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentCommentsBinding.inflate(inflater,container,false)
        setData()

        clickListener()
        arrayListPostComment.clear();
        isLoading=false


        setupRecyclerView(post!!.identifier)

        // Load initial data
        loadInitialData(post!!.identifier)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun clickListener() {
        binding.llCaAddComment.setOnClickListener { v:View->
            AddFragmentComments.data=post
        Navigation.findNavController(binding.root).navigate(R.id.addFragmentComments)
        }
           binding.fabAcBack.setOnClickListener { v:View->
                requireActivity()!!.onBackPressed()
            }

    }

    private fun setData() {
        var record= post
        val profilePictureUrl = record!!.userInfo?.profile_picture_url

// Check if profilePictureUrl is null or empty
        if (profilePictureUrl == null || profilePictureUrl.toString().isEmpty()) {
            // Load a default image using Glide's placeholder functionality
            Glide.with(requireActivity())
                .load(R.drawable.test_image2) // Set your default image resource here
                .into(binding.ivItemCommunityAllFeeds)
        } else {
            // Load the profile picture using Glide
            Glide.with(requireActivity())
                .load(profilePictureUrl.toString())
                .into(binding.ivItemCommunityAllFeeds)
        }

        binding.tvItemCommentsName.setText(record.userInfo.first_name)
        val timeAgo = CommonUtils.getTimeAgo(record.created_at)
        binding.tvItemCommentsTime.text = timeAgo


        val publicKey = record.content_key
        val privateKey = AppConstants.privateKey



        val app_secret = AppConstants.appSecret

        val ivHexString = "$privateKey$publicKey"


        val descriptionString= decryptData(record.content,app_secret,ivHexString)
        binding.tvItemCommentsMsg.getSettings().setJavaScriptEnabled(true) // Enable JavaScript if needed
        val htmlData =descriptionString.toString()
        binding.tvItemCommentsMsg.loadData(htmlData, "text/html", "UTF-8")


    }


    private fun setupRecyclerView(identifier: String) {
        // Initialize RecyclerView and its adapter

        layoutManager = LinearLayoutManager(requireActivity())
        binding.rvCa.layoutManager = layoutManager
        adaptorComments = AdaptorAllComments(requireActivity(), arrayListPostComment,post,this)
        binding.rvCa.adapter = adaptorComments

        // Set up pagination listener
        binding.rvCa.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    loadMoreData(identifier)
                }
            }
        })
    }

    private fun loadInitialData(identifier: String) {
        if (!isLoading) {
            isLoading = true
            // Load initial data
            onGetAllComments(requireActivity(), identifier,dataPerPage, presentPage)
        }
    }

    private fun loadMoreData(identifier: String) {
        if (!isLoading && presentPage < nextPage) {
            isLoading = true
            presentPage++
            binding.pbFcAfPagination.visibility = View.VISIBLE
            // Load more data
            onGetAllComments(requireActivity(),identifier ,dataPerPage, presentPage)
        }
    }

    private fun setRecyclerView(arrayList: ArrayList<Record>) {
        // Update RecyclerView with new data
        adaptorComments.notifyDataSetChanged()
        isLoading = false
        binding.pbFcAfPagination.visibility = View.INVISIBLE
    }
    private fun onGetAllComments(
        requireActivity: FragmentActivity,
        identifier:String,
        presentPage: Int,
        dataPerPage: Int

    ) {

        ViewModalClass().getAllCommentLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,identifier ,dataPerPage, presentPage
        ).observe(requireActivity()) { getAllComments: getAllComments? ->
            getAllComments?.let { nonNullAllCommentModal ->
                if (getAllComments.statusCode == 200) {

                    if (getAllComments.data!!.records.size == 0) {
                        adaptorComments.notifyDataSetChanged()
                        Toast.makeText(requireActivity, "No Comments Found", Toast.LENGTH_LONG).show()
                    } else {
                        nextPage = getAllComments?.data?.metaInfo!!.nextPage
                        for (i in 0 until getAllComments.data!!.records.size) {
                            arrayListPostComment.add(getAllComments.data!!.records[i])
                        }
                        if (isAdded) { // Check if fragment is added before accessing activity
                            setRecyclerView(arrayListPostComment)
                        }

                    }

                } else {

                    if (isAdded) {
                        CommonUtils.toast(
                            requireActivity(),
                            nonNullAllCommentModal.message ?: " Failed"
                        )
                    }
                }
            }
        }


    }



    private fun deleteComment(
       requireActivity: FragmentActivity,
        postIdentifier: String,
        CommentIdentifier: String,

    ) {

        ViewModalClass().deleteCommentLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            postIdentifier,
            CommentIdentifier
        ).observe(requireActivity()) { DeleteCommentResponse ->

            DeleteCommentResponse?.let { response ->
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

                if(isAdded){
                    arrayListPostComment.clear();
                    loadInitialData(post!!.identifier)
                }


            }
        }
    }

    override fun onCLick(record: Record, post: com.student.Compass_Abroad.modal.getAllPosts.Record?) {

            deleteComment(requireActivity(),post!!.identifier,record.identifier)

    }

    override fun onReplyClick(
        replyRecord: com.student.Compass_Abroad.modal.getCommentReplies.Record,
        post: com.student.Compass_Abroad.modal.getAllPosts.Record?,
        identifier: String,
    ) {
        deleteReply(requireActivity(),post!!.identifier,replyRecord.identifier,identifier)
    }
    private fun deleteReply(
        requireActivity: FragmentActivity,
        postIdentifier: String,
        replyIdentifier: String,
        identifier: String,

        ) {

        ViewModalClass().deleteReplyLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
            postIdentifier,
            replyIdentifier
        ).observe(requireActivity()) { DeleteReplyResponse ->

            DeleteReplyResponse?.let { response ->
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

                if(isAdded){
                    // Remove the reply from the list and update the adapter
                    adaptorComments.refreshData(postIdentifier, identifier)

                    arrayListPostComment.clear()
                    // Load initial data
                    loadInitialData(post!!.identifier)

                }


            }

        }

    }

    fun refreshData() {
        arrayListPostComment.clear()
        presentPage = 1
        loadInitialData(post!!.identifier)
    }
    override fun onPause() {
        super.onPause()
        // Clear the data and reset page number when the fragment goes into the background
        refreshData()
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }

}