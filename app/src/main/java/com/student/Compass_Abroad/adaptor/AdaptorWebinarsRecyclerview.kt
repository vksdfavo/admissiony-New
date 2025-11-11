package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemWebinarBinding
import com.student.Compass_Abroad.modal.clientEventModel.Record

class AdaptorWebinarsRecyclerview(var requireActivity: FragmentActivity?,var  arrayList1: ArrayList<Record>, private val maxItemCount: Int) :
    RecyclerView.Adapter<AdaptorWebinarsRecyclerview.ViewHolder>() {

    var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWebinarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*val currentItem = arrayList1[position]
        holder.binding.tvStatus.text ="${currentItem.status ?:" Not Available"}"

        // Set event date
        holder.binding.tvEventName.text = "${currentItem.name ?: "Not Available"}"

        val eventDate = currentItem.event_at ?: "Not Available"
        val startTime = currentItem.start_at ?: "N/A"
        val endTime = currentItem.end_at ?: "N/A"

        val formattedDateTime = if (startTime != "N/A" && endTime != "N/A") {
            "$eventDate | $startTime to $endTime"
        } else {
            eventDate
        }

        holder.binding.tvDateTime.text = formattedDateTime
        holder.binding.tvCountryName.text = "India"*/
    }
    override fun getItemCount(): Int {
        return if (arrayList1.size > maxItemCount) maxItemCount else arrayList1.size

    }

    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemWebinarBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()



    )
}
