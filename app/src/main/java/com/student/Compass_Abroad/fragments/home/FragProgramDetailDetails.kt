package com.student.Compass_Abroad.fragments.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailDetailsCampus
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailDetailsETS
import com.student.Compass_Abroad.adaptor.AdapterProgramDetailDetailsIntakes
import com.student.Compass_Abroad.databinding.FragmentFragProgramDetailDetailsBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random
import kotlin.toString
import androidx.navigation.findNavController


class FragProgramDetailDetails : BaseFragment() {

    private var binding: FragmentFragProgramDetailDetailsBinding? = null
    private var adapterProgramDetailDetailsIntakes: AdapterProgramDetailDetailsIntakes? = null
    private var adapterProgramDetailDetailsets: AdapterProgramDetailDetailsETS? = null
    var contentKey = ""

    companion object{

        var details: Record?=null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFragProgramDetailDetailsBinding.inflate(inflater, container, false)

        binding!!.backBtn.setOnClickListener{

            it.findNavController().popBackStack()
        }

        binding!!.tvFpddApply.setOnClickListener{

            binding!!.root.findNavController().navigate(R.id.applyProgramFragment)
        }

        if (sharedPre!!.getString(AppConstants.PROGRAM_CATEGORY,"")=="career_program")
        {
            binding?.linkWeb!!.visibility=View.GONE

        }else{
            binding?.linkWeb!!.visibility=View.VISIBLE

        }


        if (App.singleton?.assignStaffFav =="1")
        {
            binding!!.ll.visibility=View.GONE

        }else if (App.singleton?.assignStaffFav =="0")

        {

            binding!!.ll.visibility=View.VISIBLE

        }

        if(sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){
            binding!!.rlh.visibility=View.GONE
            binding!!.ll.visibility=View.GONE
            binding!!.tvFpddApply.visibility=View.GONE
        }else{
            binding!!.rlh.visibility=View.VISIBLE
            binding!!.tvFpddApply.visibility=View.VISIBLE
            binding!!.ll.visibility=View.VISIBLE


        }


        if(BuildConfig.FLAVOR == "MavenConsulting"){
            binding!!.rlh.visibility=View.GONE
        }else{
            binding!!.rlh.visibility=View.VISIBLE
        }


        binding!!.svFpdd.post { binding!!.svFpdd.fullScroll(View.FOCUS_DOWN) }
        val isLanguageProgram = details?.program?.additional_items?.duration_range != null
        val isCareer = details?.program?.additional_items?.duration != null
        val symbol = details?.program?.institution?.country?.currency_symbol
        val symbolCode = details?.program?.institution?.country?.currency_code
        val isHigherEducation = details?.program?.duration != null



        val applicationFee = if (isLanguageProgram) {
            details?.program?.additional_items?.application_fee ?: ""
        }
        else if(isCareer){
            details?.program?.additional_items?.application_fee ?: ""
        }
        else {
            details?.application_fee?.toString() ?: ""
        }


        val tuitionFee = if (isLanguageProgram) {
            details?.program?.additional_items?.tuition_fee ?: ""
        }
        else if(isCareer){
            details?.program?.additional_items?.tuition_fee ?: ""
        }
        else {
            details?.tuition_fee?.toString() ?: ""
        }

        val tvEnglishLevel= if(isLanguageProgram){
            details?.program?.additional_items?.english_level?:""
        }
        else if(isCareer){
            details?.program?.additional_items?.english_level?:""
        }
        else{
            ""
        }

        val tvAge= if(isLanguageProgram){
            details?.program?.additional_items?.age?:""
        }
        else if(isCareer){
            details?.program?.additional_items?.age?:""
        }
        else{
            ""
        }

        val tvAccomodation = if (isLanguageProgram) {
            val hasAccommodation = details?.program?.institution?.has_accommodation ?: ""
            val accommodationOptions = details?.program?.institution?.accommodation_options ?: ""

            if (hasAccommodation.isNotEmpty() && accommodationOptions.isNotEmpty()) {
                "$hasAccommodation ($accommodationOptions)"
            } else {
                hasAccommodation
            }
        }
        else if(isCareer){
            val hasAccommodation = details?.program?.institution?.has_accommodation ?: ""
            val accommodationOptions = details?.program?.institution?.accommodation_options ?: ""

            if (hasAccommodation.isNotEmpty() && accommodationOptions.isNotEmpty()) {
                "$hasAccommodation ($accommodationOptions)"
            } else {
                hasAccommodation
            }
        }

        else {
            ""
        }

        val campusName = details?.campus?.name
        val intakes = details?.program?.intakes
        val testScores = details?.program?.testscores


        val url = if (isLanguageProgram) {
            details?.program?.institution?.url ?: ""
        }
        else if (isCareer) {
            details?.program?.institution?.url ?: ""
        }
        else {
            details?.program?.url.toString() ?: ""
        }

        val duration = if (isLanguageProgram) {
            details?.program?.additional_items?.duration_range ?: ""
        }
        else if (isCareer) {
            details?.program?.additional_items?.duration ?: ""
        }
        else {
            details?.program?.duration?.toString() ?: ""
        }

        val durationType = if (!isLanguageProgram) {
            details?.program?.duration_type ?: ""
        }
        else {
            ""
        }

        if (sharedPre?.getString(AppConstants.CATEGORY,"") =="higher_education")
        {
            binding!!.startDate.visibility = View.GONE
            binding!!.startDateView.visibility = View.GONE
            binding!!.startDateText.visibility = View.GONE
            binding!!.weeklyHoursView.visibility = View.GONE
            binding!!.weeklyHours.visibility = View.GONE
            binding!!.weeklyHoursText.visibility = View.GONE
        }else if (App.sharedPre?.getString(AppConstants.CATEGORY,"") =="language_program"){

            binding!!.startDate.visibility = View.GONE
            binding!!.startDateView.visibility = View.GONE
            binding!!.startDateText.visibility = View.GONE
            binding!!.weeklyHoursView.visibility = View.VISIBLE
            binding!!.weeklyHours.visibility = View.VISIBLE
            binding!!.weeklyHoursText.visibility = View.VISIBLE

        }else if (App.sharedPre?.getString(AppConstants.CATEGORY,"") =="summer_school"){

            binding!!.startDate.visibility = View.VISIBLE
            binding!!.startDateView.visibility = View.VISIBLE
            binding!!.divider.visibility = View.GONE
            binding!!.startDateText.visibility = View.VISIBLE
            binding!!.weeklyHoursView.visibility = View.VISIBLE
            binding!!.weeklyHours.visibility = View.VISIBLE
            binding!!.weeklyHoursText.visibility = View.VISIBLE
        }
        else if (App.sharedPre?.getString(AppConstants.CATEGORY,"") =="career_program"){

            binding!!.startDate.visibility = View.GONE
            binding!!.startDateView.visibility = View.GONE
            binding!!.divider.visibility = View.GONE
            binding!!.startDateText.visibility = View.GONE
            binding!!.weeklyHoursView.visibility = View.GONE
            binding!!.weeklyHours.visibility = View.GONE
            binding!!.weeklyHoursText.visibility =View.GONE
        }


//
        binding!!.tvFpddDuration.text = duration?.let { "$it $durationType" } ?: "---"
        val startDate = details?.program?.additional_items?.start_date ?: ""
        val weekly_hours = details?.program?.additional_items?.weekly_hours ?: ""

// Set application fee with currency symbol or placeholder
        binding!!.tvFpddApplicationFee.text = when {
            applicationFee != null && symbolCode != null -> "$applicationFee $symbolCode"
            applicationFee != null -> "$applicationFee"
            symbolCode != null -> symbolCode
            else -> ""
        }

// Tuition Fee
        binding!!.tvFpddTuitionFee.text = when {
            tuitionFee != null && symbolCode != null -> "$tuitionFee $symbolCode"
            tuitionFee != null -> "$tuitionFee"
            symbolCode != null -> symbolCode
            else -> ""
        }
        binding!!.tvFpddCampus.text = campusName ?: "---"
        binding!!.startDate.text = startDate ?: "---"
        binding!!.weeklyHours.text = weekly_hours ?: "---"

        if (!intakes.isNullOrEmpty()) {
            binding!!.rvFpddIntakes.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding!!.rvFpddIntakes.visibility=View.VISIBLE
            binding!!.tvIntakes.visibility=View.VISIBLE
            adapterProgramDetailDetailsIntakes =
                AdapterProgramDetailDetailsIntakes(requireActivity(), intakes)
            binding!!.rvFpddIntakes.adapter = adapterProgramDetailDetailsIntakes
        } else {
            // Set placeholder if intakes is null or empty
            binding!!.rvFpddIntakes.visibility=View.GONE
            binding!!.tvIntakes.visibility=View.GONE
            binding!!.rvFpddIntakes.adapter = null // Remove any existing adapter
            // You might also want to set a placeholder in the TextView where intakes are displayed
        }

        if(!tvEnglishLevel.isNullOrEmpty()){
            binding!!.tvEnglishLevel.visibility=View.VISIBLE
            binding!!.tvLabelEnglishLevel.visibility=View.VISIBLE
            binding!!.tvLabelEnglishLevel.setText(tvEnglishLevel)

        }else{
            binding!!.tvEnglishLevel.visibility=View.GONE
            binding!!.tvLabelEnglishLevel.visibility=View.GONE
        }


        if(!tvAge.isNullOrEmpty()){
            binding!!.tvAge.visibility=View.VISIBLE
            binding!!.tvLabelAge.visibility=View.VISIBLE
            binding!!.tvLabelAge.setText(tvAge)

        }else{
            binding!!.tvAge.visibility=View.GONE
            binding!!.tvLabelAge.visibility=View.GONE
        }

        if(!tvAccomodation.isNullOrEmpty()){
            binding!!.tvAccomodation.visibility=View.VISIBLE
            binding!!.tvLabelAccomodation.visibility=View.VISIBLE
            binding!!.tvDividerAccomodation.visibility=View.VISIBLE
            binding!!.tvLabelAccomodation.setText(tvAccomodation)

        }else{
            binding!!.tvAccomodation.visibility=View.GONE
            binding!!.tvLabelAccomodation.visibility=View.GONE
            binding!!.tvDividerAccomodation.visibility=View.GONE
        }


// Set duration or placeholder
        binding!!.tvFpddDuration.text = duration?.let { "$it $durationType" } ?: "---"

// Set test scores or placeholder
        if (testScores != null && testScores.isNotEmpty()) {
            binding!!.rvFpddEts.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding!!.rvFpddEts.visibility=View.GONE
            binding!!.tvEts.visibility=View.GONE
            adapterProgramDetailDetailsets =
                AdapterProgramDetailDetailsETS(requireActivity(), testScores)
            binding!!.rvFpddEts.adapter = adapterProgramDetailDetailsets
        } else {
            binding!!.rvFpddEts.adapter = null
            binding!!.rvFpddEts.visibility=View.GONE
            binding!!.tvEts.visibility=View.GONE
        }

// Set URL or placeholder
        binding!!.tvFpddWebsite.text = url ?: "---"


// Validate and ensure URL starts with "https://"
        val finalUrl = if (!url.isNullOrBlank() && !url.startsWith("http://") && !url.startsWith("https://")) {
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



        if (details?.is_shortlisted == 0) {
            binding!!.fabFpddShortlist.setVisibility(View.VISIBLE)
            binding!!.fabFpddShortlisted.setVisibility(View.GONE)
        } else {
            binding!!.fabFpddShortlist.setVisibility(View.GONE)
            binding!!.fabFpddShortlisted.setVisibility(View.VISIBLE)
        }
        setShorlisted()
        setValues()

        return binding!!.root
    }

    private fun setValues() {

        val minRequirement  = details?.program?.min_requirement

        if (minRequirement != null) {
            binding!!.tvFpdrText.text = minRequirement.toString()
        } else {
            binding!!.tvFpdrText.text = "No minimum requirements specified"
        }

        val programName = details?.program?.name
        val institutionLogoUrl = details?.program?.institution?.logo
        val institutionName = details?.program?.institution?.name
        val countryName = details?.program?.institution?.country?.name
        binding?.tvApdProgramName?.text = programName ?: "---"

        if (!institutionLogoUrl.isNullOrEmpty()) {
            Glide.with(binding!!.root)
                .load(institutionLogoUrl)
                .into(binding!!.ivApd)
        } else {

            binding!!.ivApd.setImageResource(R.drawable.z_el)

        }

        if (sharedPre!!.getString(AppConstants.PROGRAM_CATEGORY,"") == "career_program")
        {
            binding?.tvApdCollegeNames!!.visibility=View.GONE

        }else{
            binding?.tvApdCollegeNames!!.visibility=View.VISIBLE

        }

        binding?.tvApdCollegeNames?.text = institutionName ?: "---"

        binding?.tvApdCollegeCountry?.text = countryName ?: "---"
    }


    private fun setShorlisted() {
        binding!!.fabFpddShortlist.setOnClickListener { _: View ->
            binding!!.fabFpddShortlist.setVisibility(View.GONE)
            binding!!.fabFpddShortlisted.setVisibility(View.VISIBLE)
            val hexString = generateRandomHexString(16)
            var publicKey = hexString
            var privateKey = AppConstants.privateKey

            //form data with email login code start
            val formData = JSONObject();

            formData.put(
                "program_campus_identifier",
                details?.identifier
            ) //email or phone
            val data = formData.toString();
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
            binding!!.fabFpddShortlisted.setVisibility(View.GONE)
            binding!!.fabFpddShortlist.setVisibility(View.VISIBLE)
            val hexString = generateRandomHexString(16)
            var publicKey = hexString
            var privateKey = AppConstants.privateKey

            val formData = JSONObject();

            formData.put("program_campus_identifier", details?.identifier) //email or phone
            val data = formData.toString();
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
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
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

    override fun onResume() {
        super.onResume()
       if(App.sharedPre?.getString(AppConstants.SCOUtLOGIN,"")=="true"){
           ScoutMainActivity.bottomNav!!.isVisible = false
       }else{
           MainActivity.bottomNav!!.isVisible = false
       }
    }


}