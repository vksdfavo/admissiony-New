package com.student.Compass_Abroad.uniteli

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentAmbassadorChatBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.student.Compass_Abroad.uniteli.adapter.MyAmbassadorChatAdapter


class MyAmbassadorChatFragment : Fragment(), MyAmbassadorChatAdapter.OnChatClick {
    private lateinit var binding: FragmentAmbassadorChatBinding
    private var myAmbassadorAdapter: MyAmbassadorChatAdapter? = null
    private val ambassadorList: MutableList<com.student.Compass_Abroad.modal.ambassadroChatList.Record> = mutableListOf()
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    private val TAG = "MyAmbassadorChatFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAmbassadorChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun clearChatList() {
        ambassadorList.clear()
        myAmbassadorAdapter!!.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
        setApplicationActiveRecyclerview()
        fetchDataFromApi()

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor=="admisiony")
        {
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        }else{
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.theme_color)

        }


    }

    // ADD THIS NEW METHOD - Called from parent fragment
    fun forceApiRefresh() {
        Log.d(TAG, "forceApiRefresh called - forcing refresh")
        // Reset data
        currentPage = 1
        ambassadorList.clear()
        if (myAmbassadorAdapter != null) {
            myAmbassadorAdapter?.notifyDataSetChanged()
        }
        hasNextPage = true
        isLoading = false
        // Force call API
        fetchDataFromApi(forceRefresh = true)
    }

    fun setApplicationActiveRecyclerview() {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFaActive.layoutManager = layoutManager
        myAmbassadorAdapter = MyAmbassadorChatAdapter(requireActivity(), ambassadorList, this)
        binding.rvFaActive.adapter = myAmbassadorAdapter
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

    // MODIFY THIS METHOD - Add forceRefresh parameter with default false
    fun fetchDataFromApi(forceRefresh: Boolean = false) {
        // Skip if loading or no more pages, unless force refresh is true
        if (!forceRefresh && (!hasNextPage || isLoading)) {
            Log.d(TAG, "fetchDataFromApi: Skipping - isLoading=$isLoading, hasNextPage=$hasNextPage")
            return
        }

        // If force refresh, reset page
        if (forceRefresh) {
            Log.d(TAG, "fetchDataFromApi: Force refresh - resetting page to 1")
            currentPage = 1
            ambassadorList.clear()
            myAmbassadorAdapter?.notifyDataSetChanged()
            hasNextPage = true
        }

        Log.d(TAG, "fetchDataFromApi: Starting API call for page $currentPage")
        isLoading = true

        if (currentPage == 1) {
            binding.pbFaActive.visibility = View.VISIBLE
        } else {
            binding.pbFaActivePagination.visibility = View.VISIBLE
        }

        viewModel.getMyAmbassadorChatLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            currentPage,
            perPage,
            "desc",
            "created_at",
        ).observe(viewLifecycleOwner) { response ->
            Log.d(TAG, "API Response received for page $currentPage: ${response != null}")

            response?.let {
                if (it.statusCode == 201 || it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.records ?: emptyList()
                    Log.d(TAG, "API Success - records count: ${programResponse.size}")
                    ambassadorList.addAll(programResponse)
                    myAmbassadorAdapter?.notifyDataSetChanged()

                    hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                    if (hasNextPage) {
                        currentPage++
                    }

                    if (ambassadorList.isEmpty()) {
                        binding.llFaActiveNoApplications.visibility = View.VISIBLE
                        binding.rvFaActive.visibility = View.GONE
                    } else {
                        binding.llFaActiveNoApplications.visibility = View.GONE
                        binding.rvFaActive.visibility = View.VISIBLE
                    }
                } else {
                    Log.d(TAG, "API Error: ${it.message}")
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
                    binding.llFaActiveNoApplications.visibility = View.VISIBLE
                    binding.rvFaActive.visibility = View.GONE
                }
            } ?: run {
                Log.d(TAG, "API Response is null")
                binding.llFaActiveNoApplications.visibility = View.VISIBLE
                binding.rvFaActive.visibility = View.GONE
            }

            isLoading = false
            binding.pbFaActive.visibility = View.GONE
            binding.pbFaActivePagination.visibility = View.GONE
        }
    }

    override fun onClick(recordInfo: com.student.Compass_Abroad.modal.ambassadroChatList.Record) {
        viewModel.joinAmbassadroChatLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            recordInfo.ambassador_id.toString()
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 201 && it.success) {

                    val bundle = Bundle().apply {

                        putString("relation_identifier", response.data?.relation_identifier)
                    }

                    App.sharedPre!!.saveString(
                        AppConstants.RELATION_IDENTIFIER,
                        response.data?.relation_identifier
                    )

                    App.singleton?.relationIdentifier = response.data?.relation_identifier
                    findNavController().navigate(R.id.fragmentAmbassadorGetChat, bundle)

                    Log.d("relation_identifier", response.data?.relation_identifier.toString())
                    Log.d("relation_identifier", recordInfo.ambassador_id.toString())
                } else {

                    CommonUtils.toast(requireContext(), it.message ?: "Failed")

                }

            }
        }
    }

}