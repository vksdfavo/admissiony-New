package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
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
import com.student.Compass_Abroad.modal.getApplicationResponse.Record

class AdapterApplicationsDropdown(var requireActivity: FragmentActivity, var arraylist: MutableList<Record>, var popupBinding: View, ) : RecyclerView.Adapter<AdapterFilterTestScoreSelector.MyViewHolder>() {
    private var filteredList = arraylist.toMutableList()
    private var selectedItem: Record? = null

    var onItemClickListener: ((Record) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterFilterTestScoreSelector.MyViewHolder {
        val binding =
            ItemCustomPopupEtsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AdapterFilterTestScoreSelector.MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: AdapterFilterTestScoreSelector.MyViewHolder,
        position: Int
    ) {
        val item = filteredList[position]
        holder.binding.tvItemCountrySelector.text = "Application ${position + 1}"

        holder.itemView.isSelected = item == selectedItem
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
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
                    if (item.id.toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            results.values = filteredList
            return results
        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as MutableList<Record>
            notifyDataSetChanged()

            if (filteredList.isEmpty()) { popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).visibility = View.VISIBLE

                popupBinding.requireViewById<TextView>(R.id.tvSelect_noData).setText("No Category Found")
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