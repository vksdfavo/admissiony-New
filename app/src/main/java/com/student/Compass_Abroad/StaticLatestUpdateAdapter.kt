package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.LatestUpdatesLayoutBinding

class StaticLatestUpdateAdapter(
    private val updateList: List<StaticLatestUpdate> // ✅ now using static model
) : RecyclerView.Adapter<StaticLatestUpdateAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LatestUpdatesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LatestUpdatesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = updateList[position]

        // ✅ Bind static data
        holder.binding.tvTitle.text = item.title
        holder.binding.btnTestimonials.text = item.description
        holder.binding.date.text = item.date

        Glide.with(holder.itemView.context)
            .load(item.imageResId)
            .placeholder(R.drawable.latest)
            .into(holder.binding.iv)
    }

    override fun getItemCount(): Int = updateList.size
}
