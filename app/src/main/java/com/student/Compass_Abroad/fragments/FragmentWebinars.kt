package com.student.Compass_Abroad.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdaptorWebinarRecyclerview
import com.student.Compass_Abroad.databinding.FragmentWebinarsBinding
import com.student.Compass_Abroad.modal.getWebinars.Record
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass

class FragmentWebinars : BaseFragment(), AdaptorWebinarRecyclerview.select {

    private var binding: FragmentWebinarsBinding? = null
    private var selectedTab: String = "ongoing"
    private val dataPerPage = 20
    private var presentPage = 1
    private var lastPage = 1
    private var isScrolling = false
    private var isLoading = false
    private val webinarsList = ArrayList<Record>()
    private var adapter: AdaptorWebinarRecyclerview? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebinarsBinding.inflate(inflater, container, false)

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setupTabLayout()
        setupClickListeners()
        setupRecyclerView()
        fetchWebinars()
        return binding!!.root
    }

    private fun setupClickListeners() {
        binding?.fabFpBack?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTabLayout() {
        binding?.tlFc?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTab = when (tab?.position) {
                    0 -> "ongoing"
                    1 -> "upcoming"
                    2 -> "past"
                    else -> "ongoing"
                }

                resetWebinarList()
                fetchWebinars()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvWebinars?.layoutManager = layoutManager
        adapter = AdaptorWebinarRecyclerview(requireActivity(), webinarsList, this)
        binding?.rvWebinars?.adapter = adapter

        binding?.rvWebinars?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val visibleItemCount = lm.childCount
                val totalItemCount = lm.itemCount
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()

                if (isScrolling && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    if (!isLoading && presentPage < lastPage) {
                        isScrolling = false
                        presentPage++
                        binding?.pbPagination?.visibility = View.VISIBLE
                        fetchWebinars()
                    }
                }
            }
        })
    }

    private fun fetchWebinars() {
        if (isLoading) return
        isLoading = true

        if(presentPage == 1){
            // Show progress indicator when fetching data
            binding?.pbPagination?.visibility = View.GONE
            binding?.pb?.visibility = View.VISIBLE
        }else{
            binding?.pbPagination?.visibility = View.VISIBLE
            binding?.pb?.visibility = View.GONE
        }

        // Log the parameters for debugging purposes
        Log.d("Webinars", "Fetching data for page: $presentPage, Tab: $selectedTab")

        // Make the API call to fetch webinars
        ViewModalClass().getWebinarsModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            presentPage,
            dataPerPage,
            selectedTab
        ).observe(viewLifecycleOwner) { response: getWebinarsResponse? ->
            if (response?.statusCode == 200 && response.data != null) {
                val newRecords = response.data!!.records.orEmpty()
                lastPage = response.data!!.metaInfo?.lastPage ?: 1

                Log.d("Webinars", "Fetched ${newRecords.size} webinars.")
                binding?.pb?.visibility = View.GONE
                binding?.pbPagination?.visibility = View.GONE
                if (newRecords.isEmpty() && webinarsList.isEmpty()) {
                    showNoData()
                } else {
                    showData()

                    val previousSize = webinarsList.size
                    webinarsList.addAll(newRecords)
                    adapter?.notifyItemRangeInserted(previousSize, newRecords.size)
                }
            } else {
                showErrorMessage("Failed to load webinars")
            }
            isLoading = false
        }
    }

    private fun resetWebinarList() {
        presentPage = 1
        webinarsList.clear()
        adapter?.notifyDataSetChanged()
    }

    private fun showNoData() {
        binding?.rvWebinars?.visibility = View.GONE
        binding?.noWbinarFound?.visibility = View.VISIBLE
    }

    private fun showData() {
        binding?.rvWebinars?.visibility = View.VISIBLE
        binding?.noWbinarFound?.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onclick(currentItem: Record) {
        postAttendee(currentItem)
    }

    private fun postAttendee(currentItem: Record) {
        val deviceIdentifier = App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        val firstName = App.sharedPre?.getString(AppConstants.FIRST_NAME, "")?.takeIf { it.isNotBlank() }
        val lastName = App.sharedPre?.getString(AppConstants.LAST_NAME, "")?.takeIf { it.isNotBlank() }
        val email = App.sharedPre?.getString(AppConstants.USER_EMAIL, "")?.takeIf { it.isNotBlank() }
        val phone = App.sharedPre?.getString(AppConstants.PHONE, "")?.takeIf { it.isNotBlank() }


        ViewModalClass().postAttendeLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            currentItem.identifier,
            firstName,
            lastName,
            email,
            phone,
            "internal"
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(requireActivity(), "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }

            if (response.statusCode == 200) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(currentItem.event_detail)
                requireActivity().startActivity(intent)
            } else {
                Toast.makeText(requireActivity(), response.message ?: "Failed to submit review", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

