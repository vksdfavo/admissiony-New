package com.student.Compass_Abroad.adaptor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemWebinarBinding
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.modal.getWebinars.Record
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AdaptorWebinarRecyclerview(
    var requireActivity: FragmentActivity?,
    var arrayList1: ArrayList<com.student.Compass_Abroad.modal.getWebinars.Record>,
    var selector: select
) : RecyclerView.Adapter<AdaptorWebinarRecyclerview.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWebinarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface select {
        fun onclick(currentItem: Record)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList1[position]

        // Set event title and host
        // holder.binding.tvStatus.text = currentItem.status.replaceFirstChar { it.uppercase() } ?: "Not Available"
        holder.binding.tvhostName.text =
            currentItem.host.replaceFirstChar { it.uppercase() } ?: "Not Available"
        holder.binding.tvEventName.text =
            currentItem.event_title.replaceFirstChar { it.uppercase() } ?: "Not Available"
        holder.binding.tvOnline.text =
            currentItem.event_type.replaceFirstChar { it.uppercase() } ?: "Not Available"
        //holder.binding.tvAttendees.text = "Attendees: ${currentItem.attendeesCount ?: 0}"?:""


        if (currentItem.event_type == "online") {
            holder.binding.tvOnline.text = "Online"
            holder.binding.tvAddress.visibility = View.GONE
            holder.binding.tvlink.visibility = View.VISIBLE  // Show link if event is online

        } else {
            holder.binding.tvOnline.text = "In-person"
            holder.binding.tvAddress.visibility = View.VISIBLE
            holder.binding.tvAddress.text = currentItem.event_detail
            holder.binding.tvlink.visibility = View.GONE  // Hide link for in-person events
        }

        holder.binding.tvlink.setOnClickListener {
            val clipboard =
                holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                "Event Link",
                currentItem.event_detail
            )  // Copy the download link
            clipboard.setPrimaryClip(clip)

            // Optionally show a Toast to indicate that the link was copied
            Toast.makeText(holder.itemView.context, "Link copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }

        val startTime = currentItem.start_date
        val endTime = currentItem.end_date

        val formattedStart = formatSingleDateTime(startTime)
        val formattedEnd = formatSingleDateTime(endTime)

        holder.binding.tvDateTime.text = "Start Date: $formattedStart"
        holder.binding.endDate.text = "End Date: $formattedEnd"


        Glide.with(requireActivity!!)
            .load(currentItem.fileInfo?.view_page)
            .into(holder.binding.IVCountry)

        val eventStartDate = currentItem.start_date?.toDateUTC("yyyy-MM-dd'T'HH:mm:ss")
        val eventEndDate = currentItem.end_date?.toDateUTC("yyyy-MM-dd'T'HH:mm:ss")

        if (eventStartDate != null && eventEndDate != null) {
            val currentDate = Date()
            val hasEventStarted = currentDate.after(eventStartDate)
            val hasEventEnded = currentDate.after(eventEndDate)

            if (!hasEventEnded && currentItem.event_type == "online") {
                val timeUntilStartMillis = eventStartDate.time - currentDate.time
                val timeUntilStartSeconds = timeUntilStartMillis / 1000

                if (!hasEventStarted && timeUntilStartSeconds in 0..600) {
                    // Show countdown timer
                    object : CountDownTimer(timeUntilStartMillis, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val remainingSeconds = millisUntilFinished / 1000
                            val hours = remainingSeconds / 3600
                            val minutes = (remainingSeconds % 3600) / 60
                            val seconds = remainingSeconds % 60

                            val remainingTime =
                                String.format("%02d:%02d:%02d", hours, minutes, seconds)
                            holder.binding.timerLabel.text = remainingTime
                        }

                        override fun onFinish() {
                            holder.binding.timerLabel.visibility = View.GONE
                            holder.binding.btnJoins.visibility = View.VISIBLE
                        }
                    }.start()

                    holder.binding.timerLabel.visibility = View.VISIBLE
                    holder.binding.btnJoins.visibility = View.VISIBLE
                } else if (hasEventStarted && !hasEventEnded) {
                    // Event is ongoing
                    holder.binding.timerLabel.visibility = View.GONE
                    holder.binding.btnJoins.visibility = View.VISIBLE
                } else {
                    // Event is not starting soon yet
                    holder.binding.timerLabel.visibility = View.GONE
                    holder.binding.btnJoins.visibility = View.GONE
                }
            } else {
                // Event ended or not online
                holder.binding.timerLabel.visibility = View.GONE
                holder.binding.btnJoins.visibility = View.GONE
            }
        } else {
            // Invalid dates
            holder.binding.timerLabel.visibility = View.GONE
            holder.binding.btnJoins.visibility = View.GONE
        }




        holder.binding.btnJoins.setOnClickListener {
            currentItem.event_detail?.let { url ->
                if (url.startsWith("http")) {
                    selector.onclick(currentItem)

                } else {
                    Toast.makeText(requireActivity, "Invalid meeting link", Toast.LENGTH_SHORT)
                        .show()
                }
            } ?: Toast.makeText(requireActivity, "Meeting link not available", Toast.LENGTH_SHORT)
                .show()
        }

    }


    fun formatDateTimeToIST(startDate: String?, endDate: String?): String {
        return try {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val zoneIST = ZoneId.of("Asia/Kolkata")

            val startIST = startDate?.let {
                ZonedDateTime.parse(it, formatter).withZoneSameInstant(zoneIST)
            }

            val endIST = endDate?.let {
                ZonedDateTime.parse(it, formatter).withZoneSameInstant(zoneIST)
            }

            val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

            when {
                startIST != null && endIST != null -> {
                    "${startIST.format(dateFormatter)} (${startIST.format(timeFormatter)}) to ${
                        endIST.format(
                            dateFormatter
                        )
                    } (${endIST.format(timeFormatter)})"
                }

                startIST != null -> {
                    "${startIST.format(dateFormatter)} (${startIST.format(timeFormatter)})"
                }

                endIST != null -> {
                    "${endIST.format(dateFormatter)} (${endIST.format(timeFormatter)})"
                }

                else -> "Date not available"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid Date Format"
        }
    }


    override fun getItemCount(): Int {
        return arrayList1.size
    }

    // Extension function to parse date from string and convert to UTC
    fun String.toDateUTC(format: String): Date? {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("IST")  // Set UTC timezone
            sdf.parse(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    class ViewHolder(var binding: ItemWebinarBinding) : RecyclerView.ViewHolder(binding.root)


}

fun formatSingleDateTime(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        outputFormat.format(date!!)
    } catch (e: Exception) {
        dateString // fallback
    }
}
