package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemDateBinding
import androidx.core.graphics.toColorInt

class DateAdapter( private val dates: List<DateItem>, private val onDateClick: (Int) -> Unit ) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem, position: Int) {
            binding.dateLabel.text = item.label
            binding.dateText.text = item.date
            binding.cardViews.isSelected = item.isSelected

            val selectedColor = "#DDEEFF".toColorInt()
            val defaultColor = android.graphics.Color.WHITE
            val sundayColor = "#FFEEEE".toColorInt()

//            when {
//                item.isSelected -> binding.cardView.setCardBackgroundColor(selectedColor)
//
//                item.isSunday -> binding.cardView.setCardBackgroundColor(sundayColor)
//
//                else -> binding.cardView.setCardBackgroundColor(defaultColor)
//            }

            binding.cardViews.setOnClickListener {

                onDateClick(position)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position], position)
    }

    override fun getItemCount(): Int = dates.size
}
