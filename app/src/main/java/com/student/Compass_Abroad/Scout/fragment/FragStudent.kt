package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.adaptor.StudentAdapter
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.FragmentFragStudentBinding
import com.student.Compass_Abroad.fragments.BaseFragment

import com.student.Compass_Abroad.modal.getLeads.getLeadsModal
import com.student.Compass_Abroad.retrofit.ViewModalClass

class FragStudent : Fragment() {

    private var binding: FragmentFragStudentBinding? = null
    private lateinit var studentAdapter: StudentAdapter // Using lateinit since we initialize it in onViewCreated
    private var arrayListLead = ArrayList<com.student.Compass_Abroad.modal.getLeads.Record>()

    private var currentPage = 1
    private val dataPerPage = 10
    private var isLoading = false
    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragStudentBinding.inflate(inflater, container, false)

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        binding!!.civProfileImageFd2.setOnClickListener {
            ScoutMainActivity.drawer?.open()
        }

        setupRecyclerView() // Set up the RecyclerView and Adapter
        setupPagination() // Set up pagination for infinite scrolling

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        getLead(currentPage)

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor=="admisiony")
        {
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.bottom_gradient_one)

        }else{
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.theme_color)

        }


        ScoutMainActivity.bottomNav?.isVisible = true
        currentPage = 1
        isLastPage = false
        val imageUrl = sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')

//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding!!.civProfileImageFd2)
//        getLead(currentPage)
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(requireActivity(),arrayListLead) // Initialize the adapter with the initial empty data
        binding?.rvFpAp?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvFpAp?.adapter = studentAdapter // Set the adapter to the RecyclerView
    }

    private fun setupPagination() {
        val layoutManager = binding?.rvFpAp?.layoutManager as LinearLayoutManager
        binding?.rvFpAp?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isLastPage && lastVisibleItem + 2 >= totalItemCount) {
                    currentPage++
                    getLead(currentPage)
                }
            }
        })
    }

    private fun getLead(page: Int) {
        isLoading = true
        ViewModalClass().getLeadModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            page,
            dataPerPage
        ).observe(viewLifecycleOwner) { getLeadModal: getLeadsModal? ->
            isLoading = false
            getLeadModal?.let { nonNullModal ->
                if (nonNullModal.statusCode == 200) {
                    val newList = nonNullModal.data?.records ?: emptyList()

                    if (page == 1) arrayListLead.clear()

                    arrayListLead.addAll(newList)
                    studentAdapter.notifyDataSetChanged()

                    // ✅ Toggle visibility based on list content
                    if (arrayListLead.isEmpty()) {
                        binding?.llFpApNoData?.visibility = View.VISIBLE
                        binding?.rvFpAp?.visibility = View.GONE
                    } else {
                        binding?.llFpApNoData?.visibility = View.GONE
                        binding?.rvFpAp?.visibility = View.VISIBLE
                    }

                    // ✅ Pagination check
                    isLastPage = newList.size < dataPerPage
                } else {
                    binding?.llFpApNoData?.visibility = View.VISIBLE
                    binding?.rvFpAp?.visibility = View.GONE
                    CommonUtils.toast(requireActivity(), nonNullModal.message ?: "Failed")
                }
            } ?: run {
                // ✅ Handle null or failed response
                binding?.llFpApNoData?.visibility = View.VISIBLE
                binding?.rvFpAp?.visibility = View.GONE
                CommonUtils.toast(requireActivity(), "Something went wrong.")
            }
        }
    }



}
