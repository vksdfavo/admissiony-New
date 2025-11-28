package com.student.Compass_Abroad.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailDetailsIntakes
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailIntakes
import com.student.Compass_Abroad.databinding.FragmentProgramDetailsHomeBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.home.FragProgramDetailDetails.Companion.details
import com.student.Compass_Abroad.fragments.program.ApplyProgramFragment
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random

class ProgramDetailsHomeFragment : BaseFragment() {
    private var adapterProgramDetailIntakes: AdapterProgramDetailIntakes? = null
    var contentKey = ""

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


        if (programDetails?.is_shortlisted == 0) {
            binding!!.fabFpddShortlist.visibility = View.VISIBLE
            binding!!.fabFpddShortlisted.visibility = View.GONE
        } else {
            binding!!.fabFpddShortlist.visibility = View.GONE
            binding!!.fabFpddShortlisted.visibility = View.VISIBLE
        }


        val intakes = programDetails?.program?.intakes


        setShorlisted()

        if (!intakes.isNullOrEmpty()) {
            binding!!.rvFpddIntakes.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding!!.rvFpddIntakes.visibility=View.VISIBLE
            binding!!.tvIntakes.visibility=View.VISIBLE
            adapterProgramDetailIntakes = AdapterProgramDetailIntakes(requireActivity(), intakes)
            binding!!.rvFpddIntakes.adapter = adapterProgramDetailIntakes
        } else {
            // Set placeholder if intakes is null or empty
            binding!!.rvFpddIntakes.visibility=View.GONE
            binding!!.tvIntakes.visibility=View.GONE
            binding!!.rvFpddIntakes.adapter = null
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
        val programName = programDetails.program.name
        val institutionLogoUrl = programDetails.program.institution.logo
        val institutionName = programDetails.program.institution.name
        val countryName = programDetails.program.institution.country?.name
        val campusName = programDetails.campus?.name

        // NEW — Extract Fees
        val applicationFee = programDetails.application_fee
        val tuitionFee = programDetails.tuition_fee
        val symbolCode = programDetails.program.institution.country?.currency_symbol


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

        val duration = programDetails.program.duration
        val durationType = programDetails.program.duration_type

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

        val url = programDetails!!.program?.institution?.url ?: ""


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

        val minRequirement  = programDetails!!.program?.min_requirement

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

    private fun setShorlisted() {
        binding!!.fabFpddShortlist.setOnClickListener { _: View ->
            binding!!.fabFpddShortlist.visibility = View.GONE
            binding!!.fabFpddShortlisted.visibility = View.VISIBLE
            val hexString = generateRandomHexString(16)
            var publicKey = hexString
            var privateKey = AppConstants.privateKey

            val formData = JSONObject()

            formData.put(
                "program_campus_identifier",
                programDetails?.identifier
            ) //email or phone
            val data = formData.toString()
            val dataToEncrypt = data
            val app_secret = AppConstants.appSecret

            val ivHexString = "$privateKey$publicKey"
            val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

            if (encryptedString != null) {
                contentKey = "$publicKey^#^$encryptedString"
                println("Encrypted data: $encryptedString")
                Log.d("sholisted", contentKey)

            } else {

                println("Encryption failed.")

            }
            addToShortlist(requireActivity(), contentKey)

        }
        binding!!.fabFpddShortlisted.setOnClickListener { v: View ->
            binding!!.fabFpddShortlisted.visibility = View.GONE
            binding!!.fabFpddShortlist.visibility = View.VISIBLE
            val hexString = generateRandomHexString(16)
            var publicKey = hexString
            var privateKey = AppConstants.privateKey

            val formData = JSONObject()

            formData.put("program_campus_identifier", programDetails?.identifier) //email or phone
            val data = formData.toString()
            val dataToEncrypt = data
            val app_secret = AppConstants.appSecret

            val ivHexString = "$privateKey$publicKey"
            val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)

            if (encryptedString != null) {
                contentKey = "$publicKey^#^$encryptedString"
                println("Encrypted data: $encryptedString")
                Log.d("sholisted", contentKey)

            } else {

                println("Encryption failed.")

            }
            addToShortlist(requireActivity(), contentKey)
        }

    }

    private fun addToShortlist(
        requireActivity: FragmentActivity,
        content: String,

        ) {

        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, content
        ).observe(requireActivity) { allShorListModal: ShortListResponse? ->
            allShorListModal?.let { nonNullForgetModal ->
                if (allShorListModal.statusCode == 200) {


                } else {
                    CommonUtils.toast(
                        requireActivity,
                        allShorListModal.message ?: " Failed"
                    )
                }
            }
        }
    }

    fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }

            .joinToString("")
    }
}
