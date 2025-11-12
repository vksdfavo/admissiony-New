package com.student.Compass_Abroad.fragments.counselling

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.counselling.ScheduledAdapter
import com.student.Compass_Abroad.databinding.FragmentScheduledBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.counsellingModal.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass
import androidx.navigation.findNavController

class ScheduledFragment : BaseFragment() {
    private lateinit var binding: FragmentScheduledBinding
    private var adapterScheduledAdapter: ScheduledAdapter? = null
    private val applicationList: MutableList<Record> = mutableListOf()
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    private var isRecyclerViewSetup = false

    @SuppressLint("UseKtx")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduledBinding.inflate(inflater, container, false)

        // Setup RecyclerView only once
        if (!isRecyclerViewSetup) {
            setApplicationActiveRecyclerview(requireActivity())
            isRecyclerViewSetup = true
        }

        binding.fabFaActive.setOnClickListener {
            binding.root.findNavController().navigate(R.id.bookCounsellingFragment)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
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
            "scheduled",
            currentPage,
            perPage,
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.records ?: emptyList()

                    if (currentPage == 1) {
                        // Clear data only when loading the first page
                        applicationList.clear()
                    }

                    // ✅ Avoid adding duplicates by filtering
                    val newItems = programResponse.filterNot { new ->
                        applicationList.any { existing -> existing.id == new.id }
                    }

                    applicationList.addAll(newItems)
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

        // Refresh data when returning from another fragment
        refreshData()

        // Setup status bar and navigation bar colors
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val currentFlavor = BuildConfig.FLAVOR.lowercase()
        window.navigationBarColor = if (currentFlavor == "admisiony") {
            requireActivity().getColor(R.color.bottom_gradient_one)
        } else {
            requireActivity().getColor(R.color.navigationBarColor)
        }
    }
}