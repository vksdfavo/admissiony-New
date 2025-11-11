package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemProgramRecomBinding

import com.student.Compass_Abroad.databinding.ItemRecommendedProgramsBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.ProgramTags.RecordsInfo
import kotlin.toString

class AdapterProgramsAllProg(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<Record>,
    private var selectListener: select
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context: Context? = null
    var contentKey = ""
    private val applicationList: MutableList<RecordsInfo> = mutableListOf()

    interface select {
        fun onCLick(record: Record)
        fun likeClick(record: Record, position: Int)
        fun disLikeCLick(record: Record, position: Int)
        fun openDialogCLick(record: Record, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return if (AppConstants.PROGRAM_STATUS == "0") 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemRecommendedProgramsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyRecommendedViewHolder(binding)
        } else {
            val binding = ItemProgramRecomBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = arrayList1[position]

        if (holder is MyViewHolder) {
            holder.bind(currentItem, selectListener, position)
        } else if (holder is MyRecommendedViewHolder) {
            holder.bind(currentItem, selectListener, position)
        }
    }

    override fun getItemCount(): Int {
        return arrayList1.size
    }

    class MyViewHolder(private val binding: ItemProgramRecomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record, selectListener: select, position: Int) {

            binding.apply {
                tvApdProgramName.text = record.program?.name ?: ""

                if (sharedPre!!.getString(AppConstants.PROGRAM_CATEGORY, "") == "career_program") {
                    tvIADetailIntake.visibility = View.GONE
                    Log.d("onBindViewHolder", "hide")
                } else {
                    tvIADetailIntake.visibility = View.VISIBLE

                    Log.d("onBindViewHolder", "show")

                }

                val intakeText = record.program?.intakes?.getOrNull(0)?.intake_name ?: ""

                binding.tvIADetailIntake.apply {
                    text = intakeText
                    if (intakeText.length >= 25) {
                        // If 25 or more characters → wrap to next line
                        isSingleLine = false
                        maxLines = 2
                        ellipsize = null
                    } else {
                        // Otherwise keep it single line
                        isSingleLine = true
                        ellipsize = TextUtils.TruncateAt.END
                    }
                }


                val isLanguageProgram = record.program?.additional_items?.duration_range != null
                val isCareer = record.program?.additional_items?.duration != null
                val isHigherEducation = record.program?.duration != null
                val durationType = record.program.duration_type ?: ""

                // Tuition Fee Based on Program Type
                val tuitionFee = if (isLanguageProgram) {
                    record.program?.additional_items?.tuition_fee ?: ""
                } else if (isCareer) {
                    record.program?.additional_items?.tuition_fee ?: ""
                } else {
                    record.tuition_fee?.toString() ?: ""
                }

                val currencyCode = record.program?.institution?.country?.currency_code ?: ""
                civItemAaStatus.text = "$tuitionFee $currencyCode"

                val duration = if (isHigherEducation) {
                    "${record.program?.duration ?: ""} $durationType"
                } else if (isCareer) {
                    record.program?.additional_items?.duration ?: ""
                } else {
                    record.program?.additional_items?.duration_range ?: ""
                }
                ivItemProgramRecomDuration.text = duration

                val universityName = record.program?.institution?.name ?: ""
                val countryName = record.program?.institution?.country?.name ?: ""
                ivItemProgramRecomCountry.text = countryName
                tvIADetailIntake.text = universityName

//                val logoUrl = record.program?.institution?.logo
//                if (!logoUrl.isNullOrEmpty()) {
//                    Glide.with(binding.root)
//                        .load(logoUrl)
//                        .into(binding.ivItemProgramRecom)
//                } else {
//                    binding.ivItemProgramRecom.setImageResource(R.drawable.z_el)
//                }



                // Tags
                if (!record.program?.tags.isNullOrEmpty()) {

                    recyclerLay.visibility = View.VISIBLE
                    view.visibility = View.VISIBLE
                    recyclerTags.visibility = View.VISIBLE
                    val tagsAdapter = ProgramTagAdapter(record.program.tags)
                    recyclerTags.layoutManager =
                        LinearLayoutManager(
                            binding.root.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    recyclerTags.adapter = tagsAdapter
                } else {
                    recyclerLay.visibility = View.GONE
                    view.visibility = View.GONE
                    recyclerTags.visibility = View.GONE
                }

                if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                    ibHeart.visibility = View.GONE
                    ibHeart2.visibility = View.GONE
                    cardNew.visibility = View.GONE
                } else {
                    if (record.is_shortlisted == 0) {
                        ibHeart.visibility = View.VISIBLE
                        ibHeart2.visibility = View.GONE
                    } else {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                    }

                    ibHeart.setOnClickListener {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                        selectListener.likeClick(record, position)
                        record.is_shortlisted = 1
                    }

                    ibHeart2.setOnClickListener {
                        ibHeart2.visibility = View.GONE
                        ibHeart.visibility = View.VISIBLE
                        selectListener.disLikeCLick(record, position)
                        record.is_shortlisted = 0
                    }
                }

                itemView.setOnClickListener {
                    selectListener.onCLick(record)
                }



                menuApplications.setOnClickListener {
                    selectListener.openDialogCLick(record, position)
                }
            }
        }
    }

    class MyRecommendedViewHolder(private val binding: ItemRecommendedProgramsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record, selectListener: select, position: Int) {
            binding.apply {
                // Program Name
                tvApdCollegeName.text = record.program?.name ?: ""


                // University & Country
                record.program?.institution?.name ?: ""
                val countryName = record.program?.institution?.country?.name ?: ""
                location.text = countryName

                val college= record.program?.institution?.name ?: ""
                tvApdCollegeName.text = college



                // Load Institution Logo
                val logoUrl = record.program?.institution?.logo
                if (!logoUrl.isNullOrEmpty()) {
                    Glide.with(binding.root)
                        .load(logoUrl)
                        .into(binding.iv)
                } else {
                    binding.iv.setImageResource(R.drawable.z_el)
                }

                if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                    ibHeart.visibility = View.GONE
                    ibHeart2.visibility = View.GONE
                } else {
                    if (record.is_shortlisted == 0) {
                        ibHeart.visibility = View.VISIBLE
                        ibHeart2.visibility = View.GONE
                    } else {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                    }

                    ibHeart.setOnClickListener {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                        selectListener.likeClick(record, position)
                        record.is_shortlisted = 1
                    }

                    ibHeart2.setOnClickListener {
                        ibHeart2.visibility = View.GONE
                        ibHeart.visibility = View.VISIBLE
                        selectListener.disLikeCLick(record, position)
                        record.is_shortlisted = 0
                    }
                }

                itemView.setOnClickListener {
                    selectListener.onCLick(record)
                }


            }
        }
    }
}
