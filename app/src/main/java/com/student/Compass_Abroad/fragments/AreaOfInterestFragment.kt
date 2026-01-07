package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AreaOfInterestAdaptor
import com.student.Compass_Abroad.databinding.FragmentAreaOfInterestBinding
import com.student.Compass_Abroad.retrofit.ViewModalClass


class AreaOfInterestFragment : BaseFragment() {
    private lateinit var binding: FragmentAreaOfInterestBinding
    private var disciplineAdapter: AreaOfInterestAdaptor? = null
    private val allDisciplineList = mutableListOf<com.student.Compass_Abroad.modal.preferCountryList.Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAreaOfInterestBinding.inflate(inflater, container, false)

        getDisciplineList()

        binding.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        return binding.root
    }
    private fun getDisciplineList() {
        ViewModalClass().getDisciplineDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->
            if (response?.success == true) {
                response.data?.let {
                    allDisciplineList.clear()
                    allDisciplineList.addAll(it)

                    binding.noAreaOfInterestFound.visibility = View.GONE
                    binding.rvAreaOfInterest.visibility = View.VISIBLE

                    disciplineAdapter = AreaOfInterestAdaptor(
                        requireContext(),
                        allDisciplineList,
                        object : AreaOfInterestAdaptor.Select {
                            override fun selectItemFilter(data: com.student.Compass_Abroad.modal.preferCountryList.Data) {
                                AppConstants.PROGRAM_STATUS="1"
                                binding!!.root.findNavController().navigate(R.id.fragProgramAllProg)
                            }
                        },allDisciplineList.size
                    )

                    val layoutManager = GridLayoutManager(
                        requireContext(),
                        3, // span count
                        GridLayoutManager.VERTICAL,
                        false
                    )

                    binding!!.rvAreaOfInterest.layoutManager = layoutManager
                    binding.rvAreaOfInterest.adapter = disciplineAdapter


                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible = false
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