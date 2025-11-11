package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemProgramCompareBinding
import java.util.ArrayList
import kotlin.math.log

class AdapterProgramsCompareProgram(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>,
    private var selectListener: select
) : RecyclerView.Adapter<AdapterProgramsCompareProgram.MyViewHolder>() {

    var context: Context? = null

    // Store checked state using SparseBooleanArray (efficient for indexing)
    private val checkedStateMap = SparseBooleanArray()

    interface select {
        fun onCLick(record: com.student.Compass_Abroad.modal.AllProgramModel.Record)
        fun likeClick(record: com.student.Compass_Abroad.modal.AllProgramModel.Record, position: Int)
        fun disLikeCLick(record: com.student.Compass_Abroad.modal.AllProgramModel.Record, position: Int)
        fun onCheckboxSelected(record: com.student.Compass_Abroad.modal.AllProgramModel.Record, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProgramCompareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = arrayList1[position]

        holder.bind(currentItem)

        holder.binding.iv.visibility = View.GONE

        Log.d("assignStaffFav ",App.singleton?.assignStaffFav.toString())

//
//        if (currentItem.is_shortlisted == 0) {
//            holder.binding.ibHeart.visibility = View.VISIBLE
//            holder.binding.ibHeart2.visibility = View.GONE
//        } else {
//            holder.binding.ibHeart.visibility = View.GONE
//            holder.binding.ibHeart2.visibility = View.VISIBLE
//        }
//
//        holder.binding.ibHeart.setOnClickListener {
//            holder.binding.ibHeart.visibility = View.GONE
//            holder.binding.ibHeart2.visibility = View.VISIBLE
//            selectListener.likeClick(currentItem, position)
//            arrayList1[position].is_shortlisted = 1
//        }
//
//        holder.binding.ibHeart2.setOnClickListener {
//            holder.binding.ibHeart2.visibility = View.GONE
//            holder.binding.ibHeart.visibility = View.VISIBLE
//            selectListener.disLikeCLick(currentItem, position)
//            arrayList1[position].is_shortlisted = 0
//        }

        holder.itemView.setOnClickListener {

            selectListener.onCLick(currentItem)
        }

        // Set checkbox state correctly from SparseBooleanArray
        holder.binding.cbProgramSelect.setOnCheckedChangeListener(null)
        holder.binding.cbProgramSelect.isChecked = checkedStateMap[position]

        holder.binding.cbProgramSelect.setOnCheckedChangeListener { _, isChecked ->
            checkedStateMap.put(position, isChecked)  // Store the state in the map
            selectListener.onCheckboxSelected(currentItem, isChecked)
        }
    }

    class MyViewHolder(var binding: ItemProgramCompareBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: com.student.Compass_Abroad.modal.AllProgramModel.Record) {
            binding.apply {
                tvApdProgramName.text = record.program.name
                tvIADetailIntake.text = record.program.intakes.getOrNull(0)?.intake_name ?: ""

                val isLanguageProgram = record.program?.additional_items?.duration_range != null
                val isHigherEducation = record.program?.duration != null
                val durationType = record.program.duration_type ?: ""

                val tuitionFee = if (isLanguageProgram) {
                    record.program?.additional_items?.tuition_fee ?: ""
                } else {
                    record.tuition_fee?.toString() ?: ""
                }
                val currencyCode = record.program?.institution?.country?.currency_code ?: ""
                civItemAaStatus.text = "$tuitionFee $currencyCode"

                tv6.text = "Duration"
                val duration = if (isHigherEducation) {
                    "${record.program?.duration ?: ""} $durationType"
                } else {
                    record.program?.additional_items?.duration_range ?: ""
                }
                ivItemProgramRecomDuration.text = duration

                val universityName = record.program.institution.name
                val countryName = record.program.institution.country.name ?: ""
                ivItemProgramRecomCountry.text = countryName
                tvIADetailIntake.text = universityName
            }

            Glide.with(binding.root)
                .load(record.program.institution.logo)
                .into(binding.ivItemProgramRecom)
        }
    }

    override fun getItemCount(): Int {
        return arrayList1.size
    }
}
