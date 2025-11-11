@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.databinding.ActivityNotificationDetailsBinding
import com.student.Compass_Abroad.modal.getNotification.RecordsInfo
import com.student.Compass_Abroad.modal.getNotificationRead.getNotificationReadResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass

class NotificationDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailsBinding

    companion object {
        var identifier: String? = null
        var data: RecordsInfo? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setOnClicklisteners()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (identifier != null) {
            getNotificationsRead(identifier)
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setOnClicklisteners() {
        binding.fabAcBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }
    @SuppressLint("SetTextI18n")
    private fun setNotificationData(notification: RecordsInfo?) {
        notification?.let {
            binding.notificationTitle.text = "Title: ${it.notification_info?.title ?: "N/A"}"
            binding.notificationContent.text = "Content: ${it.notification_info.content ?: "N/A"}"
            binding.notificationCategory.text =
                "Category: ${it.sent_template_info.notification_template_info.category_info.name ?: "N/A"}"
            binding.notificationChannel.text =
                "Channel: ${it.sent_template_info.notification_template_info.channel_info.name ?: "N/A"}"
            binding.notificationModuleType.text =
                "Module Type: ${it.notification_info.module_type ?: "N/A"}"
            binding.notificationReceivedAt.text =
                "Received At: ${CommonUtils.convertUTCToLocal(it.delivered_at ?: "")}"
            binding.notificationOpenedAt.text =
                "Opened At: ${CommonUtils.convertUTCToLocal(it.opened_at ?: "")}"
            binding.notificationClickedAt.text =
                "Clicked At: ${CommonUtils.convertUTCToLocal(it.read_at ?: "")}"
            binding.notificationStatus.text = "Status: ${it.status ?: "N/A"}"
            binding.notificationType.text =
                "Notification Type: ${it.notification_info.type ?: "N/A"}"
        }
    }

    private fun getNotificationsRead(identifier: String?) {
        ViewModalClass().getNotificationReadList(
            this,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            " Bearer " + CommonUtils.accessToken,
            identifier!!,
        ).observe(this) { notification: getNotificationReadResponse? ->
            notification?.let {
                if (it.statusCode == 200 && it.success) {
                    CommonUtils.toast(this, it.message ?: "Notification read successfully")
                    if (data != null) {
                        setNotificationData(data)
                    }

                } else {

                    CommonUtils.toast(this, it.message ?: "Failed")
                }
            }
        }
    }
}