package com.student.Compass_Abroad.adaptor.setPrefrences

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemDisciplineLayoutBinding
import com.student.Compass_Abroad.modal.preferCountryList.Data

class DisciplineAdaptor(
    private val context: Context,
    private val list: List<Data>,
    preselectedItems: Set<Data>,
    private val callback: Select
) : RecyclerView.Adapter<DisciplineAdaptor.MyViewHolder>() {

    private val selectedItems = preselectedItems.toMutableSet()

    interface Select {
        fun onItemToggled(item: Data, isSelected: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDisciplineLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.countryName.text = currentItem.label

        Glide.with(context)
            .load(currentItem.icon_url)
            .into(holder.binding.imageviewData)

        val isSelected = selectedItems.contains(currentItem)
        holder.binding.selectedImage.setBackgroundResource(
            if (isSelected) R.drawable.zz_select_green
            else android.R.color.transparent
        )

        holder.itemView.setOnClickListener {
            val wasSelected = selectedItems.contains(currentItem)
            if (wasSelected) {
                selectedItems.remove(currentItem)
            } else {
                selectedItems.add(currentItem)
            }

            notifyItemChanged(position)
            callback.onItemToggled(currentItem, !wasSelected)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getSelectedItems(): List<Data> = selectedItems.toList()

    class MyViewHolder(val binding: ItemDisciplineLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
