package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupCountrySelectorBinding

class AdapterFilterCollegeCitySelector(
    private val requireActivity: FragmentActivity,
    private val arraylist: ArrayList<com.student.Compass_Abroad.modal.cityModel.Data>,
    private val popupBinding: View
) : RecyclerView.Adapter<AdapterFilterCollegeCitySelector.MyViewHolder>() {

    private var filteredList: MutableList<com.student.Compass_Abroad.modal.cityModel.Data> = arraylist.toMutableList() // Maintain filtered list
    private var selectedItem:com.student.Compass_Abroad.modal.cityModel.Data? = null // Store only one selected item

    var onItemClickListener: ((com.student.Compass_Abroad.modal.cityModel.Data?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCustomPopupCountrySelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = filteredList[position]
        holder.bind(item)
    }

    fun getFilter(): Filter {
        return exampleFilter
    }

    fun getSelectedItem(): com.student.Compass_Abroad.modal.cityModel.Data? {
        return selectedItem
    }

    fun setInitialSelection(selectedItem:com.student.Compass_Abroad.modal.cityModel.Data?) {
        this.selectedItem = selectedItem
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItem = null
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class MyViewHolder(private val binding: ItemCustomPopupCountrySelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // CheckBox Change Listener
            binding.cbProgramSelect.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = filteredList[position]
                    if (isChecked) {
                        // Handle single selection
                        if (selectedItem != item) {
                            selectedItem = item
                            notifyDataSetChanged() // Refresh RecyclerView
                            onItemClickListener?.invoke(selectedItem)
                        }
                    } else {
                        if (selectedItem == item) {
                            selectedItem = null
                            notifyDataSetChanged() // Refresh RecyclerView
                            onItemClickListener?.invoke(selectedItem)
                        }
                    }
                }
            }

            // TextView Click Listener
            binding.tvItemCountrySelector.setOnClickListener {
                toggleSelection()
            }

            // LinearLayout Click Listener
            binding.llCountrySelector.setOnClickListener {
                toggleSelection()
            }
        }

        private fun toggleSelection() {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = filteredList[position]
                // Toggle CheckBox state based on click
                if (binding.cbProgramSelect.isChecked) {
                    binding.cbProgramSelect.isChecked = false
                    if (selectedItem == item) {
                        selectedItem = null
                        notifyDataSetChanged() // Refresh RecyclerView
                        onItemClickListener?.invoke(selectedItem)
                    }
                } else {
                    binding.cbProgramSelect.isChecked = true
                    if (selectedItem != item) {
                        selectedItem = item
                        notifyDataSetChanged() // Refresh RecyclerView
                        onItemClickListener?.invoke(selectedItem)
                    }
                }
            }
        }

        fun bind(item:com.student.Compass_Abroad.modal.cityModel.Data) {
            binding.tvItemCountrySelector.text = item.label
            binding.cbProgramSelect.isChecked = (selectedItem == item)
        }
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filterList = if (constraint.isNullOrEmpty()) {
                arraylist
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                arraylist.filter { it.label.toLowerCase().contains(filterPattern) }
            }
            results.values = filterList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<com.student.Compass_Abroad.modal.cityModel.Data>
            notifyDataSetChanged()

            if (filteredList.isEmpty()) {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).text = "No Country Found"
            } else {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.GONE
            }
        }
    }
}