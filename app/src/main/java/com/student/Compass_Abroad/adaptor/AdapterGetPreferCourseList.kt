package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemCustomPopupEtsBinding

class AdapterGetPreferCourseList(
    private val requireActivity: FragmentActivity,
    private var arraylist: MutableList<com.student.Compass_Abroad.modal.GetCampusModal.Data>,
    private val popupBinding: View,
    private val selectedCourses: MutableList<com.student.Compass_Abroad.modal.GetCampusModal.Data>
) : RecyclerView.Adapter<AdapterGetPreferCourseList.MyViewHolder>() {

    private var filteredList = arraylist.toMutableList()
    var onItemClickListener: ((com.student.Compass_Abroad.modal.GetCampusModal.Data) -> Unit)? = null

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

        holder.binding.ivRemove.visibility = if (selectedCourses.contains(item)) View.VISIBLE else View.GONE
        holder.binding.ivRemove.setOnClickListener {
            selectedCourses.remove(item)
            notifyDataSetChanged()
            onItemClickListener?.invoke(item)
        }

        holder.itemView.setOnClickListener {
            if (selectedCourses.contains(item)) {
                selectedCourses.remove(item)
            } else {
                if (selectedCourses.size < 3) {
                    selectedCourses.add(item)
                } else {
                    CommonUtils.toast(requireActivity, "You can select up to 3 courses")
                    return@setOnClickListener
                }
            }
            notifyDataSetChanged()
            onItemClickListener?.invoke(item)
        }
    }

    fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredResults = if (constraint.isNullOrEmpty()) {
                arraylist.toMutableList()
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                arraylist.filter {
                    it.label.toLowerCase().contains(filterPattern)
                }.toMutableList()
            }

            val results = FilterResults()
            results.values = filteredResults
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<com.student.Compass_Abroad.modal.GetCampusModal.Data>
            notifyDataSetChanged()

            // Handle "No results found" scenario
            if (filteredList.isEmpty()) {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).apply {
                    visibility = View.VISIBLE
                    text = "No Category Found"
                }
            } else {
                popupBinding.findViewById<TextView>(R.id.tvSelect_noData).visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class MyViewHolder(val binding: ItemCustomPopupEtsBinding) : RecyclerView.ViewHolder(binding.root)
}
