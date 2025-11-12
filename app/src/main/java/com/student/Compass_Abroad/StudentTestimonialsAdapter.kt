package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.TestimonialsLayoutBinding
import com.student.Compass_Abroad.modal.getTestimonials.Row

class StudentTestimonialsAdapter(
    private val testimonialList: List<Row>,
    private val onItemClick: ((Row) -> Unit)? = null
) : RecyclerView.Adapter<StudentTestimonialsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TestimonialsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TestimonialsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = testimonialList[position]

        // Bind description text
        holder.binding.btnTestimonials.text = item.description

        // Bind date (take first 10 chars for YYYY-MM-DD)
        holder.binding.date.text = item.created_at.take(10)

        // Bind image using Glide
        if (!item.media_url.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.media_url)
                .placeholder(R.drawable.test_banner)
                .into(holder.binding.iv)
        } else {
            holder.binding.iv.setImageResource(R.drawable.test_banner)
        }

        // Handle click on the card
        holder.binding.cvBase.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = testimonialList.size
}
