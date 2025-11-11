package com.student.Compass_Abroad.adaptor

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.student.Compass_Abroad.R


class ProgramTypeAdapter(
    private val items: List<com.student.Compass_Abroad.modal.getProgramFilters.ProgramType>, // Dynamic list from backend
    private val onItemClick: (com.student.Compass_Abroad.modal.getProgramFilters.ProgramType) -> Unit // Callback for selection
) : RecyclerView.Adapter<ProgramTypeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewItem)

        fun bind(item: com.student.Compass_Abroad.modal.getProgramFilters.ProgramType) {
            textView.text = item.label
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
