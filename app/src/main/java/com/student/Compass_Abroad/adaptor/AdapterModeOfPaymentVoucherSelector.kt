package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterResults
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupEtsBinding

class AdapterModeOfPaymentVoucherSelector (var requireActivity: FragmentActivity, var arraylist: MutableList<com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo>, var popupBinding: View) : RecyclerView.Adapter<AdapterModeOfPaymentVoucherSelector.MyViewHolder>() {

    private var filteredList = arraylist.toMutableList()
    private var selectedItem:com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo? = null

    var onItemClickListener: ((com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemCustomPopupEtsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val item = filteredList[position]
        holder.binding.tvItemCountrySelector.text = item.label

        holder.itemView.isSelected = item == selectedItem // Set selection state
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item) // Handle click with callback
            selectedItem = item
            notifyDataSetChanged()
        }
    }

    fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) {
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

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo>
            notifyDataSetChanged()

            // Handle "No results found" scenario (optional)
            if (filteredList.isEmpty()) { popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE

                popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).setText("No Payment For Found")
            }
            else { popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).visibility =
                View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class MyViewHolder(
        var binding: ItemCustomPopupEtsBinding
    ) : RecyclerView.ViewHolder(binding.root)
}