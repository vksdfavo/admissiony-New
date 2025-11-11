package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupEtsBinding
import com.student.Compass_Abroad.modal.getStaffList.RecordsInfo

class AdapterGetStaffList(
    private val requireActivity: FragmentActivity,
    private val arrayList: MutableList<RecordsInfo>,
    private val popupBinding: View
) : RecyclerView.Adapter<AdapterGetStaffList.MyViewHolder>() {

    private var filteredList = arrayList.toMutableList()
    private var selectedItem: RecordsInfo? = null

    var onItemClickListener: ((RecordsInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCustomPopupEtsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = filteredList[position]
        holder.binding.tvItemCountrySelector.text = item.label

        // Highlight selection if needed
        holder.itemView.isSelected = item == selectedItem

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
            selectedItem = item
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun getFilter(): Filter = exampleFilter

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filtered = if (constraint.isNullOrEmpty()) {
                arrayList.toMutableList()
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                arrayList.filter {
                    it.label!!.lowercase().contains(filterPattern)
                }.toMutableList()
            }

            results.values = filtered
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<RecordsInfo>
            notifyDataSetChanged()

            val noDataTextView = popupBinding.findViewById<TextView>(R.id.tvSelect_noData)

            if (filteredList.isEmpty()) {
                noDataTextView.visibility = View.VISIBLE
                noDataTextView.text = "No Category Found"
            } else {
                noDataTextView.visibility = View.GONE
            }
        }
    }

    class MyViewHolder(val binding: ItemCustomPopupEtsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
