package com.student.Compass_Abroad


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.QuickActionLayoutBinding
import com.student.Compass_Abroad.databinding.TestimonialsLayoutBinding

class QuickActionsAdapter(
    private val destinationList: List<TopDestinationModel>,
    private val listener: QuickActionClickListener
) : RecyclerView.Adapter<QuickActionsAdapter.ViewHolder>() {

    interface QuickActionClickListener {
        fun onQuickActionClick(item: TopDestinationModel)
    }

    inner class ViewHolder(val binding: QuickActionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TopDestinationModel) {
            binding.name.text = item.name.replace(" ", "\n")

            binding.image.setImageResource(item.imageRes)

            binding.cvBase.setOnClickListener {
                listener.onQuickActionClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuickActionLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(destinationList[position])
    }

    override fun getItemCount(): Int = destinationList.size
}
