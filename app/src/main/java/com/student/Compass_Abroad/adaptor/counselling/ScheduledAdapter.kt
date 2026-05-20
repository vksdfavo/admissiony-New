package com.student.Compass_Abroad.adaptor.counselling

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.CommonUtils.convertDate33
import com.student.Compass_Abroad.Utils.CommonUtils.convertDateUTC
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
        var location=currentItem.destination_country ?:""
        if(location.isEmpty()){
            holder.binding.destination.text = currentItem.dynamic_lead.country ?: "N/A"
        }else{
            holder.binding.destination.text = currentItem.destination_country ?:"N/A"
        }
        holder.binding.counsellingBy.text = currentItem.assigned_user_info?.first_name
            ?: ("" + " " + (currentItem.assigned_user_info?.last_name ?: "")) ?: "N/A"


        val scheduleAt = currentItem.schedule_at?.let {
            convertDate33(it, "dd MMM yyyy, hh:mm a")
        } ?: "NA"

        holder.binding.date.text = scheduleAt

        if (scheduleAt != "NA") {
            val parts = scheduleAt.split(", ")
            if (parts.size == 2) {
                val formattedDate = parts[0]
                val formattedTime = parts[1]

                holder.binding.date.text = formattedDate + "," + " $formattedTime"
            } else {
                holder.binding.date.text = "Invalid Date"
            }
        } else {
            holder.binding.date.text = "N/A"
        }

        val counsellingType = currentItem.counseling_type ?: ""
        val meetLink = currentItem.meet_link ?: ""

        if (meetLink.isEmpty()) {

            holder.binding.walkin.visibility = View.GONE
            holder.binding.plateform.visibility = View.VISIBLE

            holder.binding.plateform.text = "Link Pending"
            holder.binding.plateform.setTextColor(Color.LTGRAY)

        } else {

            if (meetLink.isNotEmpty()) {
                holder.binding.walkin.visibility = View.GONE
                holder.binding.plateform.visibility = View.VISIBLE

                holder.binding.plateform.text = "Meeting Link"
                holder.binding.plateform.setTextColor(Color.BLUE)

                holder.binding.plateform.setOnClickListener {
                    if (URLUtil.isValidUrl(meetLink)) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meetLink))
                        holder.itemView.context.startActivity(intent)
                    } else {
                        Toast.makeText(holder.itemView.context, "Invalid URL", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            } else {

                // Case 3 → Online but no link (Link Pending)
                holder.binding.walkin.visibility = View.GONE
                holder.binding.plateform.visibility = View.VISIBLE

                holder.binding.plateform.text = "Link Pending"
                holder.binding.plateform.setTextColor(Color.LTGRAY)

                holder.binding.plateform.setOnClickListener(null) // remove old click listener
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