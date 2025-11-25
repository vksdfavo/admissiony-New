package com.student.Compass_Abroad.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentProgramDetailsHomeBinding
import com.student.Compass_Abroad.fragments.program.ApplyProgramFragment

class ProgramDetailsHomeFragment : BaseFragment() {

    private lateinit var binding: FragmentProgramDetailsHomeBinding

    companion object {
        var programDetails: com.student.Compass_Abroad.modal.getProgramDetails.Data? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProgramDetailsHomeBinding.inflate(inflater, container, false)


        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()

        }

        binding!!.tvFpddApply.setOnClickListener{

            val bundle = Bundle().apply {
                putString("ProgramDetailStatus", "1")
            }

            ApplyProgramFragment.programDetails = programDetails

            binding!!.root.findNavController().navigate(R.id.applyProgramFragment, bundle)
        }

        setNewDetailsData(programDetails)

        return binding.root
    }


    private fun setNewDetailsData(programDetails: com.student.Compass_Abroad.modal.getProgramDetails.Data?) {

        if (programDetails == null) return

        // Extract Values
        val programName = programDetails.programInfo.program.name
        val institutionLogoUrl = programDetails.programInfo.program.institution.logo
        val institutionName = programDetails.programInfo.program.institution.name
        val countryName = programDetails.programInfo.program.institution.country?.name
        val campusName = programDetails.programInfo.campus?.name

        // NEW — Extract Fees
        val applicationFee = programDetails.programInfo.application_fee
        val tuitionFee = programDetails.programInfo.tuition_fee
        val symbolCode = programDetails.programInfo.program.institution.country?.currency_symbol


        // Set Program Name
        binding.tvApdProgramName.text = programName ?: "---"

        // Set Application Fee
        binding.tvFpddApplicationFee.text = when {
            applicationFee != null && symbolCode != null -> "$symbolCode$applicationFee"
            applicationFee != null -> applicationFee.toString()
            symbolCode != null -> symbolCode
            else -> "---"
        }

        // Set Tuition Fee
        binding.tvFpddTuitionFee.text = when {
            tuitionFee != null && symbolCode != null -> "$symbolCode$tuitionFee"
            tuitionFee != null -> tuitionFee.toString()
            symbolCode != null -> symbolCode
            else -> "---"
        }

        // Set Image
        if (!institutionLogoUrl.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(institutionLogoUrl)
                .into(binding.ivApd)
        } else {
            binding.ivApd.setImageResource(R.drawable.z_el)
        }

        // Set Other Fields
        binding.tvApdCollegeNames.text = institutionName ?: "---"
        binding.tvApdCollegeCountry.text = countryName ?: "---"
        binding.tvFpddCampus.text = campusName ?: "---"

        val duration = programDetails.programInfo.program.duration
        val durationType = programDetails.programInfo.program.duration_type

        binding.tvFpddDuration.text = when {
            duration != null && durationType != null -> {
                val typeFinal = if (durationType.equals("month", true) && duration != "1") {
                    "months"
                } else {
                    durationType
                }
                "$duration $typeFinal"
            }

            duration != null -> duration
            durationType != null -> durationType
            else -> "---"
        }

        val url = programDetails?.programInfo!!.program?.institution?.url ?: ""


        binding!!.tvFpddWebsite.text = url ?: "---"


// Validate and ensure URL starts with "https://"
        val finalUrl =
            if (!url.isNullOrBlank() && !url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }

// Set click listener to open URL or do nothing if URL is null
        if (!finalUrl.isNullOrBlank()) {
            binding!!.tvFpddVisit.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
        } else {
            binding!!.tvFpddVisit.setOnClickListener(null) // Remove click listener if URL is null
        }

        if (!finalUrl.isNullOrBlank()) {
            binding!!.tvFpddVisit.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
        } else {
            binding!!.tvFpddVisit.setOnClickListener(null) // Remove click listener if URL is null
        }

        val minRequirement  = programDetails?.programInfo!!.program?.min_requirement

        if (minRequirement != null) {
            binding!!.tvFpdrText.text = minRequirement.toString()
        } else {
            binding!!.tvFpdrText.text = "No minimum requirements specified"
        }

    }


    override fun onResume() {
        super.onResume()

        if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
            ScoutMainActivity.bottomNav!!.isVisible = false
        } else {
            MainActivity.bottomNav!!.isVisible = false
        }

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
