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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.counselling.ScheduledAdapter
import com.student.Compass_Abroad.databinding.FragmentScheduledBinding
import com.student.Compass_Abroad.modal.counsellingModal.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class ScheduledFragment : Fragment() {

    private var _binding: FragmentScheduledBinding? = null
    private val binding get() = _binding!!

    private var adapterScheduledAdapter: ScheduledAdapter? = null
    private val applicationList: MutableList<Record> = mutableListOf()
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    private var hasLoadedData = false

    @SuppressLint("UseKtx")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ScheduledFragment", "📱 onCreateView called")

        _binding = FragmentScheduledBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupClickListeners()
        setupWindowInsets()

        // Only fetch if this is the first time loading
        if (!hasLoadedData) {
            Log.d("ScheduledFragment", "🆕 First load - fetching data")
            fetchDataFromApi()
        } else {
            Log.d("ScheduledFragment", "♻️ View recreated - showing existing data (${applicationList.size} items)")
            updateUIVisibility()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFaActive.layoutManager = layoutManager

        // Recreate adapter with existing data
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

    private fun setupClickListeners() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>("shouldRefresh")
            ?.observe(viewLifecycleOwner) { refresh ->
                if (refresh == true) {
                    refreshData()
                    findNavController().currentBackStackEntry?.savedStateHandle?.set("shouldRefresh", false)
                }
            }

        binding.fabFaActive.setOnClickListener {
            binding.root.findNavController().navigate(R.id.bookCounsellingFragment)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ScheduledFragment", "📱 onViewCreated")
    }

    // ✅ CRITICAL: Detects when fragment becomes visible in ViewPager
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        Log.d("ScheduledFragment", "👁️ Visibility: $isVisibleToUser | hasLoadedData: $hasLoadedData | isResumed: $isResumed")

        // Refresh when becoming visible (after initial load)
        if (isVisibleToUser && hasLoadedData && isResumed && _binding != null) {
            Log.d("ScheduledFragment", "🔄 Fragment visible - triggering refresh")
            refreshData()
        }
    }

    private fun fetchDataFromApi() {
        if (!hasNextPage || isLoading) {
            Log.d("ScheduledFragment", "⏭️ Skipping fetch: hasNextPage=$hasNextPage, isLoading=$isLoading")
            return
        }

        if (_binding == null) {
            Log.e("ScheduledFragment", "❌ Cannot fetch - binding is null")
            return
        }

        isLoading = true
        hasLoadedData = true

        if (currentPage == 1) {
            binding.pbFaActive.visibility = View.VISIBLE
        } else {
            binding.pbFaActivePagination.visibility = View.VISIBLE
        }

        Log.d("ScheduledFragment", "📡 Fetching page $currentPage")

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

            if (_binding == null) {
                Log.e("ScheduledFragment", "❌ Binding null during API response")
                isLoading = false
                return@observe
            }

            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.records ?: emptyList()

                    if (currentPage == 1) {
                        applicationList.clear()
                    }

                    val newItems = programResponse.filterNot { new ->
                        applicationList.any { existing -> existing.id == new.id }
                    }

                    applicationList.addAll(newItems)
                    adapterScheduledAdapter?.notifyDataSetChanged()

                    hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                    if (hasNextPage) {
                        currentPage++
                    }

                    Log.d("ScheduledFragment", "✅ Loaded ${newItems.size} new items (Total: ${applicationList.size})")
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
            if (_binding != null) {
                binding.pbFaActive.visibility = View.GONE
                binding.pbFaActivePagination.visibility = View.GONE
            }
        }
    }

    private fun updateUIVisibility() {
        if (_binding == null) return

        if (applicationList.isEmpty()) {
            binding.llFaActiveNoApplications.visibility = View.VISIBLE
            binding.rvFaActive.visibility = View.GONE
        } else {
            binding.llFaActiveNoApplications.visibility = View.GONE
            binding.rvFaActive.visibility = View.VISIBLE
        }
    }

    fun refreshData() {
        Log.d("ScheduledFragment", "🔄 refreshData() called")

        if (_binding == null) {
            Log.e("ScheduledFragment", "❌ Cannot refresh - binding is null")
            return
        }

        currentPage = 1
        hasNextPage = true
        isLoading = false

        applicationList.clear()
        adapterScheduledAdapter?.notifyDataSetChanged()

        fetchDataFromApi()
    }

    override fun onResume() {
        super.onResume()
        Log.d("ScheduledFragment", "🟢 onResume")

        if (_binding == null) {
            Log.e("ScheduledFragment", "❌ onResume but binding is null")
            return
        }

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