package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterNotification
import com.student.Compass_Abroad.databinding.FragmentHomeBinding
import com.student.Compass_Abroad.databinding.FragmentNotificationBinding
import com.student.Compass_Abroad.modal.getNotification.getNotificationResponse
import com.student.Compass_Abroad.modal.getNotificationReadAll.getNotificationReadAllResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass


@Suppress("DEPRECATION")
class NotificationFragment : BaseFragment() {
    private lateinit var binding: FragmentNotificationBinding
    private var adapterNotification: AdapterNotification? = null
    private var layoutManager: LinearLayoutManager? = null
    private var arrayNotificationList = ArrayList<com.student.Compass_Abroad.modal.getNotification.RecordsInfo>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {

        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setClickListeners()

        getNotifications()

        return binding.getRoot()


    }
    private fun setClickListeners() {

        binding.fabAcBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        binding.tvMarkAllAsRead.setOnClickListener {

            getNotificationsMarkAllRead()

        }
    }

    private fun getNotifications() {
        ViewModalClass().getNotificationList(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(requireActivity()) { notification: getNotificationResponse? ->
            notification?.let {
                if (it.statusCode == 200) {
                    if (!it.data?.recordsInfo.isNullOrEmpty()) {
                        arrayNotificationList.clear()
                        arrayNotificationList.addAll(it.data!!.recordsInfo)

                        binding.rvNotification.visibility = View.VISIBLE
                        binding.llSaaNoData.visibility = View.GONE

                        setNotificationAdapter(arrayNotificationList)

                    } else {
                        binding.tvMarkAllAsRead.visibility = View.GONE

                        binding.rvNotification.visibility = View.GONE
                        binding.llSaaNoData.visibility = View.VISIBLE
                    }
                } else {
                    binding.rvNotification.visibility = View.GONE
                    binding.llSaaNoData.visibility = View.VISIBLE
                    CommonUtils.toast(requireActivity(), it.message ?: "Failed")
                }
            } ?: run {
                binding.rvNotification.visibility = View.GONE
                binding.llSaaNoData.visibility = View.VISIBLE
                CommonUtils.toast(requireActivity(), "No response from server.")
            }
        }
    }

    private fun setNotificationAdapter(notificationList: List<com.student.Compass_Abroad.modal.getNotification.RecordsInfo>) {
        adapterNotification = AdapterNotification(requireActivity(), notificationList)
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotification.layoutManager = layoutManager
        binding.rvNotification.adapter = adapterNotification
    }

    private fun getNotificationsMarkAllRead() {
        ViewModalClass().getNotificationReadAllList(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(requireActivity()) { notification: getNotificationReadAllResponse? ->
            notification?.let {
                if (it.statusCode == 200 && it.success) {
                    CommonUtils.toast(requireActivity(), it.message ?: "Marked as read")

                    getNotifications()
                } else {

                    CommonUtils.toast(requireActivity(), it.message ?: "Failed")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
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