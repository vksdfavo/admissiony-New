package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.modal.getApplicationResponse.AllProgramInfo
import com.student.Compass_Abroad.modal.getApplicationResponse.InstitutionData
import com.student.Compass_Abroad.databinding.ProgramListItemBinding
import com.student.Compass_Abroad.modal.getApplicationResponse.Record

class ProgramAdapter(
    private val requireActivity: FragmentActivity,
    private val data: Record?,
    var selector: select
) : RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {


    interface select{
        fun onClick(position: Record?, position1: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProgramListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val programInfo = data?.allProgramInfo?.get(position)
        val currencyInfo = data?.latestInstitutionInfo?.institution_data
        holder.bind(programInfo, requireActivity, position + 1,currencyInfo,selector,data,position) // Pass the programInfo and position
    }

    override fun getItemCount(): Int {
        return data?.allProgramInfo?.size ?: 0
    }

    class ViewHolder(private val binding: ProgramListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            programInfo: AllProgramInfo?,
            requireActivity: FragmentActivity,
            preferenceNumber: Int,
            currencyInfo: InstitutionData?,
            selector: select,
            record: Record?,
            position: Int
        ) {
            val institutionData = currencyInfo
            val name = programInfo?.program_data?.name ?: ""
            val duration = programInfo?.program_data?.duration?.toIntOrNull() ?: 0
            val durationType = programInfo?.program_data?.duration_type.orEmpty()
            val campus = programInfo?.program_data?.campus ?: ""
            val tuition_fee = programInfo?.program_data?.tuition_fee ?: ""
            val application_fee = programInfo?.program_data?.application_fee ?: ""
            val isApplied = programInfo?.is_applied ?: 0
            val currency = institutionData?.currency ?: ""
            val currencySymbol = institutionData?.currency_symbol ?: ""
            val universityName = institutionData?.name ?: ""

            binding.tvApdProgramName.text = if (name.isNotEmpty()) name else "-----"
            binding.tvCollgeName.text =if(universityName.isNotEmpty()) universityName else "-----"


            binding.tvDuration.text = if (duration > 0) {
                "$duration $durationType"
            } else {
                "-----"
            }


            val countryName = institutionData?.country ?: ""

            binding.tvLocation.text = if (countryName.isNotEmpty()) "$countryName" else "-----"

            binding.tvApplication.text = if (application_fee.isNotEmpty()) "$application_fee $currencySymbol ($currency)" else "-----"

            if (isApplied > 0) {
                binding.tvApplied.setBackgroundResource(R.drawable.ic_applied)
                binding.tvApplied.text = "Applied"
                binding.tvApplied.setTextColor(ContextCompat.getColor(requireActivity, R.color.btnColorGreen))
            } else {
                binding.tvApplied.setBackgroundResource(R.drawable.ic_preference_back)
                binding.tvApplied.text = "PREFERENCE #$preferenceNumber"
                binding.tvApplied.setTextColor(ContextCompat.getColor(requireActivity, R.color.btnPurple))
            }

            binding.clickViewDetail.setOnClickListener {
                selector.onClick(record,position)

            }
        }
    }
}