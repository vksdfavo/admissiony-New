package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.student.Compass_Abroad.InDemandCoursesAdapter
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.generateRandomHexString
import com.student.Compass_Abroad.databinding.FragmentInDemandCoursesBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject


class InDemandCoursesFragment : BaseFragment() {

 var binding: FragmentInDemandCoursesBinding?=null
    var arrayListInDemand = ArrayList<com.student.Compass_Abroad.modal.inDemandCourse.Data>()
    private var contentKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentInDemandCoursesBinding.inflate(inflater,container,false)
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

        binding!!.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        onClick()
        setupRecyclerViewInDemand()

        return binding!!.root
    }

    private fun onClick() {
        binding?.fabAcBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerViewInDemand() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"
        LoginViewModal().get_in_demandCourses(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceId,
            token
        ).observe(viewLifecycleOwner) { response ->
            response?.let { topDestinations ->
                if (topDestinations.statusCode == 200) {
                    val destinations = topDestinations.data
                    if (!destinations.isNullOrEmpty()) {
                        arrayListInDemand.clear()
                        arrayListInDemand.addAll(destinations)
                        binding?.rvIndemand?.apply {
                            layoutManager = StaggeredGridLayoutManager(
                                2,
                                StaggeredGridLayoutManager.VERTICAL
                            )
                            adapter = InDemandCoursesAdapter(
                                arrayListInDemand,
                                object : InDemandCoursesAdapter.OnCourseClickListener {
                                    override fun onItemClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                    }

                                    override fun onLikeClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                        val hexString = generateRandomHexString(16)
                                        val publicKey = hexString
                                        val privateKey = AppConstants.privateKey

                                        val formData = JSONObject().apply {
                                            put(
                                                "program_campus_identifier",
                                                data.program_campus_identifier
                                            )
                                        }
                                        val dataToEncrypt = formData.toString()
                                        val appSecret = AppConstants.appSecret
                                        val ivHexString = "$privateKey$publicKey"
                                        val encryptedString =
                                            encryptData(dataToEncrypt, appSecret, ivHexString)

                                        if (encryptedString != null) {
                                            contentKey = "$publicKey^#^$encryptedString"
                                            Log.d("shortlisted", contentKey)
                                            addToShortlist(requireActivity(), contentKey)
                                        } else {
                                            Log.d("shortlisted", "Encryption failed.")
                                        }

                                    }

                                    override fun onDislikeClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                        val hexString = generateRandomHexString(16)
                                        val publicKey = hexString
                                        val privateKey = AppConstants.privateKey

                                        val formData = JSONObject().apply {
                                            put(
                                                "program_campus_identifier",
                                                data.program_campus_identifier
                                            )
                                        }
                                        val dataToEncrypt = formData.toString()
                                        val appSecret = AppConstants.appSecret
                                        val ivHexString = "$privateKey$publicKey"
                                        val encryptedString =
                                            encryptData(dataToEncrypt, appSecret, ivHexString)

                                        if (encryptedString != null) {
                                            contentKey = "$publicKey^#^$encryptedString"
                                            Log.d("shortlisted", contentKey)
                                            // removeFromShortlist(data)
                                            addToShortlist(requireActivity(), contentKey)
                                        } else {
                                            Log.d("shortlisted", "Encryption failed.")
                                        }

                                    }
                                }
                            )
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
    private fun addToShortlist(requireActivity: FragmentActivity, content: String) {
        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken, content
        ).observe(viewLifecycleOwner) { shortListResponse: ShortListResponse? ->
            shortListResponse?.let { nonNullForgetModal ->
                if (shortListResponse.statusCode == 200) {

                    // Successfully added to shortlist
                } else {
                    CommonUtils.toast(requireActivity, shortListResponse.message ?: " Failed")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav?.visibility = View.GONE

    }

}