package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemTimelineBinding


class AdapterTimeline(
    var context: Context?,
    var data: MutableList<com.student.Compass_Abroad.modal.getApplicationTimelineResponse.Data>
) :RecyclerView.Adapter<AdapterTimeline.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data1 = data[position]
        holder.bind(data1,context,data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(
        var binding: ItemTimelineBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(data: com.student.Compass_Abroad.modal.getApplicationTimelineResponse.Data, context: Context?, data1: MutableList<com.student.Compass_Abroad.modal.getApplicationTimelineResponse.Data>) {

            binding.tvItemTimelineStatus.text = data.applicationTimelineStatusInfo.name

            if(data.est_date.isNullOrEmpty()){
                binding.tvItemTimelineEstimated.text = "N/A"
            }else{
                binding.tvItemTimelineEstimated.text = "Est Date: ${CommonUtils.convertDate4(data.est_date)}"
            }

            if(data.actual_date.isNullOrEmpty()){
                binding.tvItemTimelineActual.text = "N/A"
            }else{
                binding.tvItemTimelineActual.text = "Actual Date: ${CommonUtils.convertDate4(data.actual_date )}"
            }

           // binding.tvItemTimelineStatusS.text = data.applicationTimelineStatusInfo.short_name

            val isLastItem = position == data1.size - 1

            val lineDrawable =
                if (isLastItem) R.drawable.vertical_dashed_line_transparent else R.drawable.vertical_dashed_line_pending


            when (data.status) {
                "completed" -> {
                    setDotSize(binding.tvItemTimelineStatusS, 24)
                    binding.tvItemTimelineStatusS.background = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.shape_circle_green
                    )
                    binding.tvItemTimelineHalf1.background = ContextCompat.getDrawable(
                        context,
                        if (isLastItem) lineDrawable else R.drawable.vertical_dashed_line_green
                    )
                    binding.tvItemTimelineHalf2.background = ContextCompat.getDrawable(
                        context,
                        if (isLastItem) lineDrawable else R.drawable.vertical_dashed_line_green
                    )
                }

                "hold" -> {
                    setDotSize(binding.tvItemTimelineStatusS, 8)
                    binding.tvItemTimelineStatusS.background = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.shape_circle_yellow
                    )
                    binding.tvItemTimelineHalf1.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                    binding.tvItemTimelineHalf2.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                }

                "cancelled" -> {
                    setDotSize(binding.tvItemTimelineStatusS, 8)
                    binding.tvItemTimelineStatusS.background = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.shape_circle_red_new
                    )
                    binding.tvItemTimelineHalf1.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                    binding.tvItemTimelineHalf2.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                }

                else -> {
                    setDotSize(binding.tvItemTimelineStatusS, 8)
                    binding.tvItemTimelineStatusS.background = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.shape_circle_pending
                    )
                    binding.tvItemTimelineHalf1.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                    binding.tvItemTimelineHalf2.background = ContextCompat.getDrawable(
                        context,
                        lineDrawable
                    )
                }
            }

        }

        fun setDotSize(view: View, sizeDp: Int) {
            val density = view.context.resources.displayMetrics.density
            val sizePx = (sizeDp * density).toInt()
            view.layoutParams = view.layoutParams.apply {
                width = sizePx
                height = sizePx
            }
        }

    }



}