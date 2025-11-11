package com.student.Compass_Abroad.fragments

import android.R.attr.onClick
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.LatestUpdateAdapter
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.StaticLatestUpdate
import com.student.Compass_Abroad.StaticLatestUpdateAdapter
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentLatestUpdateBinding
import com.student.Compass_Abroad.retrofit.LoginViewModal


class LatestUpdateFragment : BaseFragment() {
  private lateinit var binding: FragmentLatestUpdateBinding

    var arrayListInLatestUpdate = ArrayList<com.student.Compass_Abroad.modal.getTestimonials.Row>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentLatestUpdateBinding.inflate(inflater,container,false)
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
        setupRecyclerLatestUpdates()
        return binding.root


    }

    private fun onClick() {
        binding.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerLatestUpdates() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"

        val staticUpdates = listOf(
            StaticLatestUpdate(
                title = "Study Visa Updates 2025",
                description = "Canada announces new study visa rules effective January 2025.",
                date = "2025-11-08",
                imageResId = R.drawable.latest
            ),
            StaticLatestUpdate(
                title = "UK Scholarship Alert",
                description = "New full scholarships available for Indian students in 2025 intake.",
                date = "2025-10-30",
                imageResId = R.drawable.latest
            ),
            StaticLatestUpdate(
                title = "Australia Intake 2026",
                description = "Applications for the February 2026 intake now open for top universities.",
                date = "2025-10-20",
                imageResId = R.drawable.latest
            )
        )

        // ✅ Show static list in RecyclerView
        binding.rvLatestUpdates.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = StaticLatestUpdateAdapter(staticUpdates)
        }

//        LoginViewModal().getTestimonials(
//            requireActivity(),
//            AppConstants.fiClientNumber,
//            deviceId, token,true,"recent_update"
//        ).observe(viewLifecycleOwner) { response ->
//            response?.let { resp ->
//                if (resp.statusCode == 200) {
//                    val records = resp.data?.records?.rows
//                    arrayListInLatestUpdate.clear()
//                    arrayListInLatestUpdate.addAll(records!!)
//                    binding?.rvLatestUpdates?.apply {
//                        layoutManager = LinearLayoutManager(
//                            requireContext(),
//                            LinearLayoutManager.VERTICAL,
//                            false
//                        )
//                        adapter =
//                            LatestUpdateAdapter(arrayListInLatestUpdate) { selectedItem ->
//                                // Handle click here
//                            }
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