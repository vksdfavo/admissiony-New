package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.NotificationDetailsActivity
import com.student.Compass_Abroad.databinding.ItemNotificationBinding
import com.student.Compass_Abroad.modal.getNotification.RecordsInfo
import androidx.navigation.findNavController

class AdapterNotification(
    private val context: Context,
    private var notifications: List<RecordsInfo>
) : RecyclerView.Adapter<AdapterNotification.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification, position)
    }

    override fun getItemCount(): Int = notifications.size


    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: RecordsInfo, position: Int) {

            val contentHtml = notification.notification_info.content
            val titleHtml = notification.notification_info.title

            val content = HtmlCompat.fromHtml(contentHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            val title = HtmlCompat.fromHtml(titleHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


            itemView.setOnClickListener {
                if (notification.notification_info.module_type == "application") {

                    binding.root.findNavController().navigate(R.id.applicationActiveFragment)

                } else {
                    binding.root.findNavController().navigate(R.id.homeFragment)

                }

            }

            if (notification.read_status == "read") {
                val spannableText = SpannableString("$content($title)")
                spannableText.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.text_color
                        )
                    ), // Replace with your gray color resource
                    0,
                    spannableText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Make the title bold
                val startTitle =
                    content.length + 1 // Start index of title after content and parenthesis
                val endTitle = spannableText.length
                spannableText.setSpan(
                    StyleSpan(Typeface.BOLD), // Make title bold
                    startTitle,
                    endTitle,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.status.text = spannableText
                binding.greenDot.visibility = View.GONE
            } else {
                // Apply black color for unread status and add a green dot
                val spannableText = SpannableString("$content($title)")

                // Set gray color for entire text
                spannableText.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.black
                        )
                    ), // Replace with your gray color resource
                    0,
                    spannableText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Make the title bold
                val startTitle =
                    content.length + 1 // Start index of title after content and parenthesis
                val endTitle = spannableText.length
                spannableText.setSpan(
                    StyleSpan(Typeface.BOLD), // Make title bold
                    startTitle,
                    endTitle,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.status.text = spannableText
                binding.greenDot.visibility = View.VISIBLE
            }

            // Set created at text
            binding.createAt.text = CommonUtils.convertUTCToLocal(notification.sent_at)

            // Handle view details click
            binding.viewDetails.setOnClickListener {
                val notificationInfo = notification.notification_info
                if (notificationInfo != null && notificationInfo.identifier != null) {
                    NotificationDetailsActivity.identifier = notificationInfo.identifier
                    NotificationDetailsActivity.data = notification
                    val intent = Intent(context, NotificationDetailsActivity::class.java)
                    context.startActivity(intent)
                } else {
                    CommonUtils.toast(context, "Notification data is missing or invalid.")
                }
            }
        }
    }


}
