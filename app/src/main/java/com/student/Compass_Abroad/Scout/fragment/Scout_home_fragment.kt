package com.student.Compass_Abroad.Scout.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.modalClass.scoutsummary.ScoutSummaryModal
import com.student.Compass_Abroad.Scout.retrofitScout.ViewModalScout
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentScoutHomeFragmentBinding
import com.student.Compass_Abroad.databinding.HomeinfolayoutBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.fragments.home.FragProgramAllProg
import com.student.Compass_Abroad.modal.getDestinationCountryList.Data
import com.student.Compass_Abroad.modal.refreshToken.RefreshTokenResonse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.RetrofitClient12
import com.student.Compass_Abroad.retrofit.ViewModalClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class Scout_home_fragment : Fragment() {
    private lateinit var binding: FragmentScoutHomeFragmentBinding
    private var identityInfo: com.student.Compass_Abroad.modal.staffProfile.Data? = null
    var apiInterface = RetrofitClient12.retrofitCallerObject11!!.create(ApiInterface::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentScoutHomeFragmentBinding.inflate(inflater, container, false)


        binding.civProfileImageFd2.setOnClickListener {
            ScoutMainActivity.drawer!!.open()
        }

        binding!!.llMaReferEarn.setOnClickListener {
            createReferandShare(requireActivity())
        }


        if (getString(R.string.app_name).trim().equals("Admissiony.com", ignoreCase = true)) {

            saveSelectedToSharedPreferences(AppConstants.CountryList, "230", "United Kingdom")


        } else {
            clearFilter()

        }

        setScoutDataSummary()
        identityInfo = null
        binding.switchStu.isChecked = true
        var isCurrentRoleScout = true
        sharedPre?.saveString(AppConstants.SCOUtLOGIN, "true")
        binding.switchStu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == isCurrentRoleScout) {
                binding.switchStu.isChecked = isCurrentRoleScout
                return@setOnCheckedChangeListener
            }
            clearFilter()

            val message =
                if (isChecked) "Switching to Scout dashboard..." else "Switching to Student dashboard..."
            CommonUtils.progressDialog(requireActivity(), null, message, 1000)

            val identityInfoList = identityInfo?.userInfo?.identityInfo
            val activeIdentifier = identityInfo?.activeUserIdentityInfo?.identifier

            if (identityInfoList != null && activeIdentifier != null) {
                val otherIdentities =
                    identityInfoList.filter { it.identifier != activeIdentifier }.take(2)

                Handler(Looper.getMainLooper()).postDelayed({
                    CommonUtils.progressDialogueDismiss()

                    if (otherIdentities.isNotEmpty()) {
                        val identity = otherIdentities.first()

                        CoroutineScope(Dispatchers.IO).launch {
                            val refreshResponse = refreshTokenApi(identity.identifier, context)
                            withContext(Dispatchers.Main) {
                                if (refreshResponse?.statusCode == 200) {
                                    val newToken = refreshResponse.data?.tokensInfo?.accessToken
                                    sharedPre?.saveString(AppConstants.ACCESS_TOKEN, newToken)

                                    val isStudent =
                                        identity.name.contains("Student", ignoreCase = true)
                                    val intent = if (isStudent) {
                                        FragProgramAllProg.selectedTab="recommended"
                                        Intent(requireActivity(), MainActivity::class.java)
                                    } else {
                                        FragProgramAllProg.selectedTab="all"
                                        Intent(requireActivity(), ScoutMainActivity::class.java)
                                    }

                                    startActivity(intent)
                                    requireActivity().finish()
                                    hitApiUserDetails()
                                } else {
                                    handleRefreshTokenError(
                                        refreshResponse?.statusCode,
                                        refreshResponse?.message
                                    )
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Other role identifier not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 2000)
            } else {
                Toast.makeText(requireContext(), "Identity info not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        onClicks()
        return binding.root
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
        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        if (currentFlavor == "admisiony") {
            requireActivity().window.navigationBarColor =
                requireActivity().getColor(R.color.bottom_gradient_one)

        } else {
            requireActivity().window.navigationBarColor =
                requireActivity().getColor(R.color.theme_color)

        }

        hitApiUserDetails()
        onClicks()
        ScoutMainActivity.bottomNav!!.isVisible = true

        val imageUrl = sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')

//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding!!.civProfileImageFd2)

    }

    private fun hitApiUserDetails() {
        ViewModalClass().getStaffProfileData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity()) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {


                    setUserData(staffData)
                    val firstName =
                        staffData.data?.studentProfileInfo?.assignedStaffInfo?.first_name ?: ""
                    val lastName =
                        staffData.data?.studentProfileInfo?.assignedStaffInfo?.last_name ?: ""

                    val fullName = when {
                        firstName.isNotEmpty() && lastName.isNotEmpty() -> "$firstName $lastName"
                        firstName.isNotEmpty() -> firstName
                        lastName.isNotEmpty() -> lastName
                        else -> ""
                    }

                    identityInfo = staffData.data
                    binding?.tvFdStuCoordinatorName?.text = fullName


                    binding!!.fabFdStuCoordinatorcall.setOnClickListener {
                        val phoneNumber =
                            staffData.data?.studentProfileInfo?.assignedStaffInfo?.mobile?.toString()
                        val countryCode =
                            staffData.data?.studentProfileInfo?.assignedStaffInfo?.country_code?.toString()

                        if (!phoneNumber.isNullOrEmpty() && !countryCode.isNullOrEmpty()) {
                            // Ensure the country code starts with '+'
                            val formattedCountryCode =
                                if (countryCode.startsWith("+")) countryCode else "+$countryCode"
                            val fullNumber = formattedCountryCode + phoneNumber

                            val phoneIntent =
                                Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", fullNumber, null))
                            if (phoneIntent.resolveActivity(requireActivity().packageManager) != null) {
                                startActivity(phoneIntent)
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "No app available to place a call",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        identityInfo = staffData.data


                        App.singleton?.studentIdentifier =
                            staffData.data?.studentProfileInfo?.identifier

                        sharedPre!!.saveString(
                            AppConstants.USER_IDENTIFIER,
                            staffData.data?.studentProfileInfo?.identifier
                        )
                    }
                } else {
                    val errorMessage = nonNullForgetModal.message ?: "Failed"

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMessage)
                    }
                }
            }
        }
    }

    private fun setUserData(staffData: StaffProfileModal) {
        sharedPre!!.saveString(AppConstants.FIRST_NAME, staffData.data?.userInfo?.first_name)
        sharedPre!!.saveString(AppConstants.LAST_NAME, staffData.data!!.userInfo.last_name)
        sharedPre!!.saveString(AppConstants.DOB, staffData.data!!.userInfo.birthday)

        /* val firstName = staffData.data?.userInfo?.first_name ?: ""
        val lastName = staffData.data?.userInfo?.last_name ?: ""

        binding!!.name.text = "Hi, " + "$firstName $lastName"*/

        val profilePictureUrl = staffData.data?.userInfo?.profile_picture_url

        sharedPre!!.saveModel(AppConstants.USER_IMAGE, profilePictureUrl)

    }

    fun refreshTokenApi(list: String, context: Context?): RefreshTokenResonse? {
        return try {
            val response = apiInterface.getRefreshToken(
                fiClientNumber = AppConstants.fiClientNumber,
                device_number = sharedPre?.getString(AppConstants.Device_IDENTIFIER, ""),
                authorization = "Bearer ${sharedPre?.getString(AppConstants.REFRESH_TOKEN, "")}",
                identity = list
            )!!.execute() // Assuming you are using a suspend function

            if (response.isSuccessful) {

                response.body()

            } else {
                Log.e(
                    "ProfileActivity",
                    "Error refreshing token: ${response.code()} ${response.errorBody()?.string()}"
                )
                null
            }
        } catch (e: IOException) {
            Log.e("ProfileActivity", "Token refresh failed due to network error: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Unexpected error during token refresh: ${e.message}", e)
            null
        }
    }


    private fun handleRefreshTokenError(code: Int?, errorBody: String?) {
        if (code == 422) {
            Log.e("AuthInterceptor", "Unprocessable Entity: $errorBody")
        } else {
            Log.e("AuthInterceptor", "Token refresh failed with unexpected error: $code")
        }
    }

    private fun clearFilter() {

        sharedPre!!.clearKeyLabelValue(AppConstants.PGWP_KEY)
        sharedPre!!.clearKeyLabelValue(AppConstants.ATTENDANCE_KEY)
        sharedPre!!.clearKeyLabelValue(AppConstants.Accomodation)
        sharedPre!!.clearKeyLabelValue(AppConstants.PROGRAM_TYPE_KEY)
        sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
        sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
        sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
        sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)
        sharedPre!!.clearKey(AppConstants.AgeList)
        sharedPre!!.clearKey(AppConstants.EnglishLevelList)

        clearAllSelectedValues()
    }

    private fun clearAllSelectedValues() {
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.StateList)
        clearSelectedValuesFromSharedPreferences(AppConstants.CityList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }

    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }

    private fun setScoutDataSummary() {
        ViewModalScout().ScoutSummaryLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken
        ).observe(viewLifecycleOwner) { scoutSummary: ScoutSummaryModal? ->
            scoutSummary?.let { scoutSummary ->
                if (view != null) {
                    if (scoutSummary.statusCode == 200) {

                        binding.tvFiStatus.text = scoutSummary.data.leads_count.toString()
                        binding.tv22.text = scoutSummary.data.applications_count.toString()
                        binding.tv33.text = scoutSummary.data.potential_payout_amount.toString()
                        binding.civItemAaAsShort.text =
                            scoutSummary.data.actual_payout_amount.toString()
                        binding.potentialEarnedCurrency.text =
                            "(${scoutSummary.data.potential_payout_currency})"
                        binding.earnedAmountCurrency.text =
                            "(${scoutSummary.data.actual_payout_currency})"

                    }
                }
            }
        }
    }

    private fun saveSelectedToSharedPreferences(
        keyPrefix: String,
        ids: String,
        labels: String
    ) {
        val sharedPrefs = SharedPrefs(requireActivity())
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }

    private fun createReferandShare(activity1: Activity) {
        val deviceIdentifier =
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        ViewModalClass().postReferLinkLiveData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            "user"
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }
            Log.d(
                "SaveReviewResponse",
                "Status Code: ${response.statusCode}, Message: ${response.message}"
            )

            // Check if status code is 201 (Created) meaning the referral link was generated successfully
            if (response.statusCode == 201) {
                val shortUrl = response.data?.shortUrl

                if (!shortUrl.isNullOrEmpty()) {
                    // Show success message
                    //Toast.makeText(activity1, response.message ?: "Referral link generated successfully!", Toast.LENGTH_SHORT).show()

                    // Log the short URL for debugging
                    Log.e("ReferralLink", shortUrl)

                    // Share the referral link
                    shareReferralLink(activity1, shortUrl)
                } else {
                    // Handle case when the short URL is null or empty
                    Toast.makeText(
                        activity1,
                        "Failed to generate referral link",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareReferralLink(activity1: Activity, shortUrl: String) {
        // Create an Intent to share the referral link
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this awesome app! Use my referral link: $shortUrl"
            )
        }

        // Start the share activity
        activity1.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


    private fun onClicks() {

        binding.ibFdU1.setOnClickListener {
            showInfoBottomSheet(
                "Signed Up Students - No. of students signed up by scout",
                "This is the total number of students that have signed up using your code or has been manually assigned to you."
            )
        }

        binding.ibFdU2.setOnClickListener {
            showInfoBottomSheet(
                "Total Applications - Scout's student applications",
                "This metric represents the total number of student applications submitted through Scout’s platform. Each application reflects a step taken by a student toward their educational journey abroad, facilitated with your support. The number serves as an indicator of your engagement and effectiveness in guiding students through the application process."
            )
        }

        binding.ibFdU3.setOnClickListener {
            showInfoBottomSheet(
                "Potential Earned - Potential amount earned by Scout",
                "This is the potential amount you can earn if all of your students enrol to their selected institutions."
            )
        }

        binding.ibFdU4.setOnClickListener {
            showInfoBottomSheet(
                "Earned Amount - Earning of Scout",
                "This is the amount you have earned in USD by recommending other students to study abroad and achieve their dreams."
            )
        }


    }

    private fun showInfoBottomSheet(title: String, description: String) {
        val inflater = LayoutInflater.from(requireActivity())
        val bottomSheetBinding = HomeinfolayoutBinding.inflate(inflater, null, false)

        bottomSheetBinding.tvDialogTitle.text = title
        bottomSheetBinding.content.text = description

        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheet2)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()


    }
}