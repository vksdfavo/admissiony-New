package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.ItemIndemandCoursesBinding
import com.student.Compass_Abroad.modal.inDemandCourse.Data

class InDemandCoursesAdapter(
    private val destinationList: List<Data>,
    private val listener: OnCourseClickListener? = null
) : RecyclerView.Adapter<InDemandCoursesAdapter.ViewHolder>() {

    // ✅ Interface for 3 click events
    interface OnCourseClickListener {
        fun onItemClick(data: Data, position: Int)
        fun onLikeClick(data: Data, position: Int)
        fun onDislikeClick(data: Data, position: Int)
    }

    inner class ViewHolder(val binding: ItemIndemandCoursesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIndemandCoursesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = destinationList[position]

        // ✅ Bind data
        holder.binding.tvApdCollegeName.text = item.program_name
        holder.binding.location.text = item.campus_name
        holder.binding.price.text = "$ ${item.tuition_fee_usd}"

        Glide.with(holder.itemView.context)
            .load(item.institution_logo)
            .into(holder.binding.iv)

        holder.binding.cvBase.setOnClickListener {
            listener?.onItemClick(item, position)

        }


        if (item.is_shortlisted == 0) {
            holder.binding.ibHeart.visibility = View.VISIBLE
            holder.binding.ibHeart2.visibility = View.GONE
        } else {
            holder.binding.ibHeart.visibility = View.GONE
            holder.binding.ibHeart2.visibility = View.VISIBLE
        }

        holder.binding.ibHeart.setOnClickListener {
            listener?.onLikeClick(item, position)
            holder.binding.ibHeart.visibility = View.GONE
            holder.binding.ibHeart2.visibility = View.VISIBLE

        }

        holder.binding.ibHeart2.setOnClickListener {
            listener?.onDislikeClick(item, position)
            holder.binding.ibHeart2.visibility = View.GONE
            holder.binding.ibHeart.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = destinationList.size
}
