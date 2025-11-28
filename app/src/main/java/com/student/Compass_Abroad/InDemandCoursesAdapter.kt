package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.ItemIndemandCoursesBinding
import com.student.Compass_Abroad.databinding.ShimmerItemIndemandBinding
import com.student.Compass_Abroad.modal.inDemandCourse.Data

class InDemandCoursesAdapter(
    private var courseList: List<Data>,
    private val listener: OnCourseClickListener? = null,
    private var isLoading: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SHIMMER = 0
    private val VIEW_TYPE_DATA = 1

    interface OnCourseClickListener {
        fun onItemClick(data: Data, position: Int)
        fun onLikeClick(data: Data, position: Int)
        fun onDislikeClick(data: Data, position: Int)
    }

    class DataViewHolder(val binding: ItemIndemandCoursesBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ShimmerViewHolder(val binding: ShimmerItemIndemandBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ShimmerItemIndemandBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemIndemandCoursesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            DataViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) 5 else courseList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShimmerViewHolder) {
            holder.binding.shimmerIndemand.startShimmer()
            return
        }

        holder as DataViewHolder
        val item = courseList[position]

        holder.binding.apply {
            tvApdCollegeName.text = item.program_name
            location.text = item.campus_name
            price.text = "$ ${item.tuition_fee_usd}"

            Glide.with(root.context)
                .load(item.institution_logo)
                .into(iv)

            cvBases.setOnClickListener {
                listener?.onItemClick(item, position)
            }

            if (item.is_shortlisted == 0) {
                ibHeart.visibility = View.VISIBLE
                ibHeart2.visibility = View.GONE
            } else {
                ibHeart.visibility = View.GONE
                ibHeart2.visibility = View.VISIBLE
            }

            ibHeart.setOnClickListener {
                item.is_shortlisted = 1
                ibHeart.visibility = View.GONE
                ibHeart2.visibility = View.VISIBLE
                listener?.onLikeClick(item, position)
            }

            ibHeart2.setOnClickListener {
                item.is_shortlisted = 0
                ibHeart2.visibility = View.GONE
                ibHeart.visibility = View.VISIBLE
                listener?.onDislikeClick(item, position)
            }
        }
    }

    /** Update list after API response and stop shimmer */
    fun updateList(newList: List<Data>) {
        isLoading = false
        courseList = newList
        notifyDataSetChanged()
    }
}
