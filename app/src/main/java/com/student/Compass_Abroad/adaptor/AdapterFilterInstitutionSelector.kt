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
import com.student.Compass_Abroad.modal.institutionModel.RecordsInfo


class AdapterFilterInstitutionSelector(
    private val requireActivity: FragmentActivity,
    private val arraylist: ArrayList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>,
    private val popupBinding: View
) : RecyclerView.Adapter<AdapterFilterInstitutionSelector.MyViewHolder>() {

    private var filteredList: MutableList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo> = arraylist.toMutableList() // Maintain filtered list
    private var selectedItems: MutableList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo> = mutableListOf()

    var onItemClickListener: ((List<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>) -> Unit)? = null

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

    fun getSelectedItems(): List<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo> {
        return selectedItems
    }

    fun setInitialSelection(selectedItems: List<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class MyViewHolder(private val binding: ItemCustomPopupCountrySelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
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

            binding.tvItemCountrySelector.setOnClickListener {
                toggleSelection()
            }

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

        fun bind(item: com.student.Compass_Abroad.modal.institutionModel.RecordsInfo) {
            binding.tvItemCountrySelector.text = item.label
            binding.cbProgramSelect.isChecked = selectedItems.contains(item)
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
            filteredList = results?.values as MutableList<com.student.Compass_Abroad.modal.institutionModel.RecordsInfo>
            notifyDataSetChanged()

            if (filteredList.isEmpty()) {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).text = "No Institution Found"
            } else {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.GONE
            }
        }
    }
}