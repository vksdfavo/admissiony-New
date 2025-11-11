package com.student.Compass_Abroad.adaptor

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupCountrySelectorBinding

class AdapterFilterDisplineSelector(
    var requireActivity: FragmentActivity,
    var arraylist: ArrayList<com.student.Compass_Abroad.modal.discipline.Data>,
    var popupBinding: View
) : RecyclerView.Adapter<AdapterFilterDisplineSelector.MyViewHolder>() {

    private var filteredList: MutableList<com.student.Compass_Abroad.modal.discipline.Data> = arraylist.toMutableList() // Maintain filtered list
    private var selectedItems: MutableList<com.student.Compass_Abroad.modal.discipline.Data> = mutableListOf()

    var onItemClickListener: ((List<com.student.Compass_Abroad.modal.discipline.Data>) -> Unit)? = null

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

    fun getSelectedItems(): List<com.student.Compass_Abroad.modal.discipline.Data> {
        return selectedItems
    }

    fun setInitialSelection(selectedItems: List<com.student.Compass_Abroad.modal.discipline.Data>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class MyViewHolder(private val binding: ItemCustomPopupCountrySelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            // Click listener for CheckBox
            binding.cbProgramSelect.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = filteredList[position]
                    if (isChecked) {
                        if (!selectedItems.contains(item)) {
                            selectedItems.add(item)
                        }
                    } else {
                        selectedItems.remove(item)
                    }
                    onItemClickListener?.invoke(selectedItems)
                }
            }

            // Click listener for TextView
            binding.tvItemCountrySelector.setOnClickListener {
                toggleSelection()
            }

            // Click listener for LinearLayout
            binding.llCountrySelector.setOnClickListener {
                toggleSelection()
            }
        }
        private fun toggleSelection() {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = filteredList[position]
                // Toggle CheckBox state based on LinearLayout click
                if (binding.cbProgramSelect.isChecked) {
                    binding.cbProgramSelect.isChecked = false
                    selectedItems.remove(item)
                } else {
                    binding.cbProgramSelect.isChecked = true
                    if (!selectedItems.contains(item)) {
                        selectedItems.add(item)
                    }
                }
                onItemClickListener?.invoke(selectedItems)
            }
        }
        fun bind(item: com.student.Compass_Abroad.modal.discipline.Data) {
            binding.tvItemCountrySelector.text = item.label
            binding.cbProgramSelect.isChecked = selectedItems.contains(item)

            // Ensure that CheckBox and LinearLayout have consistent states
            binding.llCountrySelector.isClickable = true
        }
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint.isNullOrEmpty()) {
                // Reset to original list if search bar is empty
                filteredList.clear()
                filteredList.addAll(arraylist)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                filteredList.clear()
                for (item in arraylist) {
                    if (item.label.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            results.values = filteredList
            return results
        }




        @RequiresApi(Build.VERSION_CODES.P)
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<com.student.Compass_Abroad.modal.discipline.Data>
            notifyDataSetChanged()

            // Handle "No results found" scenario (optional)
            if (filteredList.isEmpty()) {
                popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE
                popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).text = "No Discipline Found"
            } else {
                popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).visibility = View.GONE
            }
        }
    }

}