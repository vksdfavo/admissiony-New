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
import com.student.Compass_Abroad.databinding.ItemCustomPopupEtsBinding
import com.student.Compass_Abroad.modal.documentType.RecordsInfo

class AdapterModuleType(
    private val requireActivity: FragmentActivity,
    private var arraylist: MutableList<RecordsInfo>,
    private val popupBinding: View
) : RecyclerView.Adapter<AdapterModuleType.MyViewHolder>() {

    private var filteredList = arraylist.toMutableList()
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

        holder.binding.tvItemCountrySelector.text = item.label ?: ""

        holder.itemView.isSelected = item == selectedItem
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
            selectedItem = item
            notifyDataSetChanged()
        }
    }

    fun getFilter(): Filter = exampleFilter

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) {
                filteredList = arraylist.toMutableList()
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                filteredList = arraylist.filter {
                    it.label?.lowercase()?.contains(filterPattern) == true
                }.toMutableList()
            }
            results.values = filteredList
            return results
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as? MutableList<RecordsInfo> ?: mutableListOf()
            notifyDataSetChanged()

            val noDataView = popupBinding.findViewById<TextView>(R.id.tvSelect_noData)
            if (filteredList.isEmpty()) {
                noDataView.visibility = View.VISIBLE
                noDataView.text = "No Category Found"
            } else {
                noDataView.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = filteredList.size

    class MyViewHolder(val binding: ItemCustomPopupEtsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
