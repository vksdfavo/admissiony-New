@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterApplicationsActive
import com.student.Compass_Abroad.adaptor.AdapterFragmentPayments
import com.student.Compass_Abroad.adaptor.leadToApplication.AdapterApplicationPaymentDetails
import com.student.Compass_Abroad.databinding.FragmentPaymentDetailBinding
import com.student.Compass_Abroad.fragments.home.SharedViewModel
import com.student.Compass_Abroad.modal.getApplicationResponse.Record
import com.student.Compass_Abroad.modal.paymentDetails.RecordsInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass


class PaymentDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentDetailBinding
    private var adapterPaymentDetail: AdapterApplicationPaymentDetails? = null
    private val applicationList: MutableList<RecordsInfo> = mutableListOf()
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    private val viewModel: ViewModalClass by lazy {
        ViewModalClass()
    }
    var search:String?=null

    companion object{

        var  data:String?=null

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentPaymentDetailBinding.inflate(inflater, container, false)
        
        onClickListeners()
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.theme_color)

        setPaymentDetailRecyclerview(requireContext())
        search=data
        binding.etFpSearch.setText(search)

        search?.let { fetchDataFromApi(it) }
        searchData()

        return binding.root
    }

    private fun searchData() {
        binding.etFpSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                search = s.toString().trim()

                if (search.isNullOrEmpty()) {
                    applicationList.clear()
                    adapterPaymentDetail?.notifyDataSetChanged()
                    currentPage = 1
                    hasNextPage = true
                    fetchDataFromApi(search!!)
                }
            }
        })

        // Trigger search when clicking the search button
        binding.ibFpSearch.setOnClickListener {
            search = binding.etFpSearch.text.toString().trim()

            if (search.isNullOrEmpty()) {
                CommonUtils.toast(requireActivity(), "Please enter an ID")
            } else {
                applicationList.clear()
                adapterPaymentDetail?.notifyDataSetChanged()
                currentPage = 1
                hasNextPage = true
                fetchDataFromApi(search!!)
            }
        }
    }


    private fun setPaymentDetailRecyclerview(context: Context) {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFpAp.layoutManager = layoutManager
        adapterPaymentDetail = AdapterApplicationPaymentDetails(context, applicationList)
        binding!!.rvFpAp.adapter = adapterPaymentDetail

        binding!!.rvFpAp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                search?.let { fetchDataFromApi(it) }
            }
        })
    }


    private fun fetchDataFromApi( search:String) {
        if (!hasNextPage || isLoading) return

        isLoading = true
        if (currentPage == 1) {
            binding!!.pbFpAp.visibility = View.VISIBLE
        } else {
            binding!!.pbFpApPagination.visibility = View.VISIBLE
        }
        if (search != null) {
            viewModel.getApplicationResponsePaymentDetailsLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                currentPage,
                perPage,
                "desc",
                "id",
                search
            ).observe(viewLifecycleOwner) { response ->
                response?.let {
                    if (it.statusCode == 200 && it.success) {
                        val programResponse = it.data?.recordsInfo ?: emptyList()
                        applicationList.addAll(programResponse)
                        adapterPaymentDetail?.notifyDataSetChanged()

                        hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                        if (hasNextPage) {
                            currentPage++
                        }

                        if (applicationList.isEmpty()) {
                            binding!!.llFpApNoData.visibility = View.VISIBLE
                            binding!!.rvFpAp.visibility = View.GONE
                        } else {
                            binding!!.llFpApNoData.visibility = View.GONE
                            binding!!.rvFpAp.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUtils.toast(requireContext(), it.message ?: "Failed")
                        binding!!.llFpApNoData.visibility = View.VISIBLE
                        binding!!.rvFpAp.visibility = View.GONE
                    }
                } ?: run {
                    binding!!.llFpApNoData.visibility = View.VISIBLE
                    binding!!.rvFpAp.visibility = View.GONE
                }

                isLoading = false
                binding!!.pbFpAp.visibility = View.GONE
                binding!!.pbFpApPagination.visibility = View.GONE
            }
        }
    }
    private fun onClickListeners() {
        binding.fabFadBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
    }
    override fun onResume() {
        super.onResume()
        searchData()

        MainActivity.bottomNav!!.isVisible = false
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}