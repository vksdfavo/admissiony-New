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
import com.student.Compass_Abroad.TopInDemandIntuitionsAdapter
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentInDemandInstitutionBinding
import com.student.Compass_Abroad.retrofit.LoginViewModal


class InDemandInstitution : BaseFragment() {

    private lateinit var binding: FragmentInDemandInstitutionBinding

    var arrayListInDemandInstitution =
        ArrayList<com.student.Compass_Abroad.modal.in_demandInstitution.Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentInDemandInstitutionBinding.inflate(inflater,container,false)
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
        setupRecyclerViewInDemandIntuitions()

        binding.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root

    }
    private fun setupRecyclerViewInDemandIntuitions() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"
        LoginViewModal().get_in_demandInstitution(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceId, token
        ).observe(viewLifecycleOwner) { response ->
            response?.let { topDestinations ->
                if (topDestinations.statusCode == 200) {
                    val destinations = topDestinations.data
                    if (!destinations.isNullOrEmpty()) {
                        arrayListInDemandInstitution.clear()
                        arrayListInDemandInstitution.addAll(destinations)
                        binding?.rvIndemandIntuitions?.apply {
                            layoutManager = StaggeredGridLayoutManager(
                                3, StaggeredGridLayoutManager.VERTICAL
                            )
                            adapter =
                                TopInDemandIntuitionsAdapter(arrayListInDemandInstitution) { selectedItem ->


                                }
                        }
                    }

                } else {
                    val errorMsg = topDestinations.message ?: "Failed"
                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMsg)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav?.visibility = View.GONE

    }

}