package com.student.Compass_Abroad.adaptor.counselling

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemScheduledLayoutBinding
import com.student.Compass_Abroad.modal.counsellingModal.Record
import java.text.SimpleDateFormat
import java.util.*

class ScheduledAdapter(
    var requireActivity: FragmentActivity,
    var arrayList1: MutableList<Record>
) : RecyclerView.Adapter<ScheduledAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemScheduledLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList1[position]

        holder.binding.completedId.text = "Counselling ID: " + currentItem.id.toString()
        holder.binding.destination.text = currentItem.destination_country
        holder.binding.counsellingBy.text = currentItem.assigned_user_info?.first_name?:"" + " " + (currentItem.assigned_user_info?.last_name ?: "")


//        Glide.with(context!!).load(currentItem.assigned_user_info.profile_picture_url).into(holder.binding.staffImg)

        val scheduleAt = if (currentItem.schedule_at != null) {
            CommonUtils.convertDate33(currentItem.schedule_at, "dd MMM yyyy, hh:mm a")
        } else {
            "NA"
        }

        if (scheduleAt != "NA") {
            val parts = scheduleAt.split(", ")
            if (parts.size == 2) {
                val formattedDate = parts[0] // "07 Feb 2025"
                val formattedTime = parts[1] // "10:12 AM"

                holder.binding.date.text = formattedDate+" $formattedTime"
            } else {
                holder.binding.date.text = "Invalid Date"
            }
        } else {
            holder.binding.date.text = "NA"
        }


        if (currentItem.meet_link == null) {
            holder.binding.plateform.visibility = View.GONE
            holder.binding.walkin.visibility = View.VISIBLE
        } else {

            holder.binding.plateform.setOnClickListener {
                val meetLink = currentItem.meet_link
                if (meetLink != null && URLUtil.isValidUrl(meetLink)) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(meetLink)
                    }

                    val context = holder.itemView.context
                    if (context != null) {
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Invalid Context", Toast.LENGTH_SHORT).show()
                    }

                } else {

                    Toast.makeText(holder.itemView.context, "Invalid URL", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    override fun getItemCount(): Int = arrayList1.size

    class ViewHolder(var binding: ItemScheduledLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun formatWithSuffix(date: Date, timeZone: TimeZone): String {
        val dayFormat = SimpleDateFormat("d", Locale.getDefault())
        dayFormat.timeZone = timeZone // Set time zone for day format
        val day = dayFormat.format(date).toInt()
        val dayWithSuffix = "$day${getDayOfMonthSuffix(day)}"

        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        monthYearFormat.timeZone = timeZone
        val monthYear = monthYearFormat.format(date)

        return "$dayWithSuffix $monthYear"
    }

    private fun getDayOfMonthSuffix(day: Int): String {
        return if (day in 11..13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}