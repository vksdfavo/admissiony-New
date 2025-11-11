package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.StaticTestimonial
import com.student.Compass_Abroad.StudentStaticTestimonialsAdapter
import com.student.Compass_Abroad.StudentTestimonialsAdapter
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentStudentTestimonialsBinding
import com.student.Compass_Abroad.retrofit.LoginViewModal


class StudentTestimonials : BaseFragment() {

private lateinit var binding: FragmentStudentTestimonialsBinding
    var arrayListInStudentTestimonials =
        ArrayList<com.student.Compass_Abroad.modal.getTestimonials.Row>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentStudentTestimonialsBinding.inflate(layoutInflater)
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
        onClick()
        setupRecyclerViewStudentTestimonials()
        return binding.root
    }

    private fun onClick() {
        binding.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerViewStudentTestimonials() {
        val staticList = listOf(
            StaticTestimonial(
                name = "Aarav Sharma",
                description = "Compass Abroad helped me get admission to my dream university in Canada!",
                date = "2025-11-01",
                imageResId = R.drawable.test_banner
            ),
            StaticTestimonial(
                name = "Priya Mehta",
                description = "Amazing experience! The counselors were super supportive.",
                date = "2025-10-25",
                imageResId = R.drawable.test_banner
            ),
            StaticTestimonial(
                name = "Rohan Verma",
                description = "Very professional service. Highly recommend Compass Abroad!",
                date = "2025-10-15",
                imageResId = R.drawable.test_banner
            )
        )

        binding?.rvTestimonials?.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = StudentStaticTestimonialsAdapter(staticList)
        }

//        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
//        val token = "Bearer ${CommonUtils.accessToken}"
//
//        LoginViewModal().getTestimonials(
//            requireActivity(),
//            AppConstants.fiClientNumber,
//            deviceId, token,true,"webinar"
//        ).observe(viewLifecycleOwner) { response ->
//            response?.let { resp ->
//                if (resp.statusCode == 200) {
//                    val records = resp.data?.records?.rows
//                    if (!records.isNullOrEmpty()) {
//                        arrayListInStudentTestimonials.clear()
//                        arrayListInStudentTestimonials.addAll(records)
//
//                        binding?.rvTestimonials?.apply {
//                            layoutManager = LinearLayoutManager(requireContext(),
//                                LinearLayoutManager.VERTICAL
//                               ,false
//                            )
//                            adapter =
//                                StudentTestimonialsAdapter(arrayListInStudentTestimonials) { selectedItem ->
//                                    // Handle click here
//                                }
//
//                        }
//
//
//                    }
//                } else {
//                    val errorMsg = resp.message ?: "Failed"
//                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
//                        CommonUtils.toast(requireActivity(), errorMsg)
//                    }
//                }
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.visibility=View.GONE
    }

}