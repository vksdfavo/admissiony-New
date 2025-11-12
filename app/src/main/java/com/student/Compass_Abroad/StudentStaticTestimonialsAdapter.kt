package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.TestimonialsLayoutBinding

class StudentStaticTestimonialsAdapter(
    private val testimonialList: List<StaticTestimonial>,
    private val onItemClick: ((StaticTestimonial) -> Unit)? = null
) : RecyclerView.Adapter<StudentStaticTestimonialsAdapter.ViewHolder>() {

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

        holder.binding.btnTestimonials.text = item.description
        holder.binding.date.text = item.date

        Glide.with(holder.itemView.context)
            .load(item.imageResId)
            .into(holder.binding.iv)

        holder.binding.cvBase.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = testimonialList.size
}
