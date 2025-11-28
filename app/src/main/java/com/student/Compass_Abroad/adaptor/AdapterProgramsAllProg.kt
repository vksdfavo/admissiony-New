package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.ItemProgramRecomBinding
import com.student.Compass_Abroad.databinding.ItemRecommendedProgramsShimmerBinding
import com.student.Compass_Abroad.databinding.ItemRecommendedProgramssBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Record

class AdapterProgramsAllProg(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<Record>,
    private var selectListener: select
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isLoading = false


    companion object {
        private const val VIEW_TYPE_RECOMMENDED = 0
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_SHIMMER = 2
    }


    interface select {
        fun onCLick(record: Record)
        fun likeClick(record: Record, position: Int)
        fun disLikeCLick(record: Record, position: Int)
        fun openDialogCLick(record: Record, position: Int)
    }

    fun setLoading(loading: Boolean) {
        isLoading = loading
        notifyDataSetChanged()
    }

    // ----------------------------
    // VIEW TYPE HANDLING FIXED
    // ----------------------------
    override fun getItemViewType(position: Int): Int {
        return if (isLoading) {
            VIEW_TYPE_SHIMMER
        } else {
            if (AppConstants.PROGRAM_STATUS == "0")
                VIEW_TYPE_RECOMMENDED
            else
                VIEW_TYPE_NORMAL
        }
    }


    override fun getItemCount(): Int {
        return if (isLoading) 5 else arrayList1.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            VIEW_TYPE_RECOMMENDED -> {
                val binding = ItemRecommendedProgramssBinding.inflate(inflater, parent, false)
                MyRecommendedViewHolder(binding)
            }

            VIEW_TYPE_NORMAL -> {
                val binding = ItemProgramRecomBinding.inflate(inflater, parent, false)
                MyViewHolder(binding)
            }

            VIEW_TYPE_SHIMMER -> {
                val binding = ItemRecommendedProgramsShimmerBinding.inflate(inflater, parent, false)
                ShimmerViewHolder(binding.root)
            }

            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (isLoading || holder is ShimmerViewHolder) return   // <<< IMPORTANT FIX

        val currentItem = arrayList1[position]

        when (holder) {
            is MyViewHolder -> holder.bind(currentItem, selectListener, position)
            is MyRecommendedViewHolder -> holder.bind(currentItem, selectListener, position)
        }
    }


    // ---------------------------------------
    // SHIMMER HOLDER
    // ---------------------------------------
    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    // ---------------------------------------
    // NORMAL VIEW HOLDER
    // ---------------------------------------
    class MyViewHolder(private val binding: ItemProgramRecomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record, selectListener: select, position: Int) {

            binding.apply {

                tvApdProgramName.text = record.program?.name ?: ""

                val intakeText = record.program?.intakes?.getOrNull(0)?.intake_name ?: ""
                tvIADetailIntake.text = intakeText

                val isLanguageProgram = record.program?.additional_items?.duration_range != null
                val isCareer = record.program?.additional_items?.duration != null
                val isHigherEducation = record.program?.duration != null
                val durationType = record.program.duration_type ?: ""

                val tuitionFee =
                    record.program?.additional_items?.tuition_fee ?: record.tuition_fee.toString()

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

                ivItemProgramRecomCountry.text =
                    record.program?.institution?.country?.name ?: ""

                tvIADetailIntake.text = record.program?.institution?.name ?: ""

                // TAGS
                if (!record.program?.tags.isNullOrEmpty()) {
                    recyclerLay.visibility = View.VISIBLE
                    recyclerTags.visibility = View.VISIBLE
                    val tagsAdapter = ProgramTagAdapter(record.program.tags)
                    recyclerTags.layoutManager =
                        LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                    recyclerTags.adapter = tagsAdapter
                } else {
                    recyclerLay.visibility = View.GONE
                    recyclerTags.visibility = View.GONE
                }

                // HEART ACTIONS
                if (sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                    ibHeart.visibility = View.GONE
                    cardNew.visibility = View.GONE
                    ibHeart2.visibility = View.GONE
                } else {
                    if (record.is_shortlisted == 0) {
                        ibHeart.visibility = View.VISIBLE
                        cardNew.visibility = View.VISIBLE
                        ibHeart2.visibility = View.GONE
                    } else {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                    }

                    ibHeart.setOnClickListener {
                        ibHeart.visibility = View.GONE
                        ibHeart2.visibility = View.VISIBLE
                        record.is_shortlisted = 1
                        selectListener.likeClick(record, position)
                    }

                    ibHeart2.setOnClickListener {
                        ibHeart2.visibility = View.GONE
                        ibHeart.visibility = View.VISIBLE
                        record.is_shortlisted = 0
                        selectListener.disLikeCLick(record, position)
                    }
                }

                itemView.setOnClickListener { selectListener.onCLick(record) }

                menuApplications.setOnClickListener {
                    selectListener.openDialogCLick(record, position)
                }
            }
        }
    }

    // ---------------------------------------
    // RECOMMENDED VIEW HOLDER
    // ---------------------------------------

    class MyRecommendedViewHolder(private val binding: ItemRecommendedProgramssBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record, selectListener: select, position: Int) {

            binding.apply {

                tvApdCollegeName.text = record.program?.name ?: ""
                location.text = record.program?.institution?.country?.name ?: ""
                tvCollege.text = record.program?.institution?.name ?: ""

                val logoUrl = record.program?.institution?.logo
                if (!logoUrl.isNullOrEmpty()) {
                    Glide.with(binding.root)
                        .load(logoUrl)
                        .placeholder(R.drawable.z_el)
                        .into(binding.iv)
                } else binding.iv.setImageResource(R.drawable.z_el)

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
                        record.is_shortlisted = 1
                        selectListener.likeClick(record, position)
                    }

                    ibHeart2.setOnClickListener {
                        ibHeart2.visibility = View.GONE
                        ibHeart.visibility = View.VISIBLE
                        record.is_shortlisted = 0
                        selectListener.disLikeCLick(record, position)
                    }
                }

                itemView.setOnClickListener { selectListener.onCLick(record) }
            }
        }
    }
}
