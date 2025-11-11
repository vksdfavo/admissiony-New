package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R

class PGWPAdapter(
    private val items: List<com.student.Compass_Abroad.modal.getProgramFilters.IsPGWP>, // List of dynamic items (e.g., "yes", "no")
    private val onItemClick: (com.student.Compass_Abroad.modal.getProgramFilters.IsPGWP) -> Unit // Callback for item selection
) : RecyclerView.Adapter<PGWPAdapter.ViewHolder>() {

    // ViewHolder to hold each item in the list
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewItem)

        fun bind(item: com.student.Compass_Abroad.modal.getProgramFilters.IsPGWP) {
            textView.text = item.label // Display the label of the item
            itemView.setOnClickListener { onItemClick(item) } // Handle item click
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pgwp_option, parent, false) // Inflate item layout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position]) // Bind data to the ViewHolder
    }

    override fun getItemCount(): Int = items.size // Return the total number of items
}
