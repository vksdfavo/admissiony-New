package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.LatestUpdatesLayoutBinding
import com.student.Compass_Abroad.modal.getTestimonials.Row

class LatestUpdateAdapter(
    private val destinationList: List<Row>,
    private val onItemClick: ((Row) -> Unit)? = null
) : RecyclerView.Adapter<LatestUpdateAdapter.ViewHolder>() {

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
        // ✅ safety check — avoid crash if list is empty or index invalid
        if (position < 0 || position >= destinationList.size) return

        val item = destinationList[position]

        // Bind description text
//        holder.binding.btnTestimonials.text = item.description ?: ""
//
//        // Bind date safely
//        holder.binding.date.text = item.created_at?.takeIf { it.length >= 10 }?.take(10) ?: ""
//
//        // Bind image safely using Glide
//        val imageUrl = item.media_url
//        Glide.with(holder.itemView.context)
//            .load(imageUrl?.takeIf { it.isNotBlank() })
//            .placeholder(R.drawable.test_banner)
//            .error(R.drawable.test_banner)
//            .into(holder.binding.iv)
//
//        // Handle click
//        holder.binding.cvBase.setOnClickListener {
//            onItemClick?.invoke(item)
//        }
    }

    // ✅ Always return actual list size
    override fun getItemCount(): Int = destinationList.size
}
