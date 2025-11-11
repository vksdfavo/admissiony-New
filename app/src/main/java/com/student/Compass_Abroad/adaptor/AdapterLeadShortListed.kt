package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemshortlistedprogramBinding
import com.student.Compass_Abroad.modal.getLeadShorlistedProgram.Row


class AdapterLeadShortListed(
    private val context: Context,
    private val leadShorlistedProgramList: MutableList<Row>
) : RecyclerView.Adapter<AdapterLeadShortListed.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemshortlistedprogramBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = leadShorlistedProgramList[position]
        val programDetails = program.program

        // Load logo image with Glide and handle null URL
        val logoUrl = programDetails?.institution?.logo ?: ""
        Glide.with(context)
            .load(logoUrl)
            .placeholder(R.drawable.test_image) // Use an appropriate placeholder
            .into(holder.binding.ivItemProgramRecom)

        // Set program name and handle null or empty values
        holder.binding.tvApdProgramName.text = programDetails?.name?.takeIf { it.isNotEmpty() } ?: "N/A"

        // Set duration and handle null values
        val duration = programDetails?.duration?.toString()?.takeIf { it.isNotEmpty() } ?: "N/A"
        holder.binding.tvDuration.text = "$duration Months"

        val tuitionFee = program.tuition_fee?.toString() ?: "N/A"
        holder.binding.tvTuitionFees.text = tuitionFee

        // Set application fee and handle null values
        val applicationFee = program.application_fee?.toString() ?: "N/A"
        holder.binding.tvApplicationFee.text = applicationFee

        // Set English test score and handle null or empty values
        val testScore = programDetails?.testscores?.firstOrNull()?.toString() ?: "N/A"
        holder.binding.tvEnglishTestScore.text = testScore
    }

    override fun getItemCount(): Int {
        return leadShorlistedProgramList.size
    }

    class ViewHolder(val binding: ItemshortlistedprogramBinding) : RecyclerView.ViewHolder(binding.root)
}
