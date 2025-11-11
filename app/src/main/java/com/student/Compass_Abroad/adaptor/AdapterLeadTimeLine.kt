package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemLeadTimelineBinding
import com.student.Compass_Abroad.modal.getLeadTimelineResponse.Data
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterLeadTimeLine(var context: Context?,var  leadTimelineList: MutableList<Data>) : RecyclerView.Adapter<AdapterLeadTimeLine.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterLeadTimeLine.MyViewHolder {
        val binding =
            ItemLeadTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterLeadTimeLine.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterLeadTimeLine.MyViewHolder, position: Int) {
        val data = leadTimelineList[position]
        holder.bind(data,context)
    }

    override fun getItemCount(): Int {
        return leadTimelineList.size
    }

    class MyViewHolder(// Use the generated ViewBinding class
        var binding: ItemLeadTimelineBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(data: Data, context: Context?) {

            binding.tvItemTimelineStatus.text = data.lead_stage_name ?: "Not Available"


            val createdAt = data.created_at ?: ""

            if (isValidDate(createdAt)) {
                binding.tvItemTimelineEstimated.text = "Est Date: ${CommonUtils.convertDate3(createdAt, "dd MMMM yyyy")}"
                binding.tvItemTimelineActual.text = "Actual Date: ${CommonUtils.convertDate3(createdAt, "dd MMMM yyyy")}"
            } else {
                binding.tvItemTimelineEstimated.text = "Est Date: Not Available"
                binding.tvItemTimelineActual.text = "Actual Date: Not Available"
            }

            binding.tvItemTimelineStatusS.text = data.lead_stage_short_name ?: "NA"

            // Helper function to validate date



            if (data.status == "completed") {
                binding.tvItemTimelineStatusS.setBackground(ContextCompat.getDrawable(context!!, R.drawable.shape_circle_green))
                binding.tvItemTimelineHalf1.setBackground(ContextCompat.getDrawable(context!!, R.drawable.vertical_dashed_line_green))
                binding.tvItemTimelineHalf2.setBackground(ContextCompat.getDrawable(context!!, R.drawable.vertical_dashed_line_green))
            } else {
                binding.tvItemTimelineStatusS.setBackground(ContextCompat.getDrawable(context!!, R.drawable.shape_circle_pending))
                binding.tvItemTimelineHalf1.setBackground(ContextCompat.getDrawable(context!!, R.drawable.vertical_dashed_line_pending))
                binding.tvItemTimelineHalf2.setBackground(ContextCompat.getDrawable(context!!, R.drawable.vertical_dashed_line_pending))
            }


        }
        private fun isValidDate(date: String): Boolean {
            return try {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                format.isLenient = false
                format.parse(date)
                true
            } catch (e: ParseException) {
                false
            }
        }
    }

}