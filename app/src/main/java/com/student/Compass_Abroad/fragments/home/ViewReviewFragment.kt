package com.student.Compass_Abroad.fragments.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterViewReview
import com.student.Compass_Abroad.databinding.FragmentViewReviewBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.retrofit.ViewModalClass



@Suppress("DEPRECATION")
class ViewReviewFragment : BaseFragment() {

    private lateinit var binding: FragmentViewReviewBinding
    private lateinit var adapterViewReview: AdapterViewReview
    private var currentPage = 1
    private val dataPerPage = 10
    private var isLoading = false
    private var isLastPage = false

    companion object {
        lateinit var data: String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewReviewBinding.inflate(inflater, container, false)



        setOnClickListeners()
        setRecyclerView()
        loadReviews()

        return binding.root
    }

    private fun setRecyclerView() {
        adapterViewReview = AdapterViewReview(requireActivity())
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = adapterViewReview

        binding.rvReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0 && totalItemCount >= dataPerPage
                    ) {
                        currentPage++
                        loadReviews()
                    }
                }
            }
        })
    }

    private fun setOnClickListeners() {
        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadReviews() {
        isLoading = true
        binding.pbPagination.visibility = View.VISIBLE

        CommonUtils.accessToken?.let {
            ViewModalClass().getReviewListLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer $it",
                data,
                "approved",
                currentPage,
                dataPerPage
            ).observe(viewLifecycleOwner) { response ->
                binding.pbPagination.visibility = View.GONE
                isLoading = false

                if (response != null && response.statusCode == 200) {
                    if (response.data?.records?.isNotEmpty() == true) {
                        adapterViewReview.addReviews(response!!.data!!.records)
                        binding.llSaaNoData.visibility = View.GONE
                    } else {
                        isLastPage = true
                        if (currentPage == 1) {
                            binding.llSaaNoData.visibility = View.VISIBLE
                        }
                    }
                } else {
                    CommonUtils.toast(requireActivity(), response?.message ?: "Notes Failed")
                    if (currentPage == 1) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
    }
}