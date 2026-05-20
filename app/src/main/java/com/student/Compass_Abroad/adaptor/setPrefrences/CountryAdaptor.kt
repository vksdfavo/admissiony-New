package com.student.Compass_Abroad.adaptor.setPrefrences

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.CompleteProfileRecyclerviewBinding
import com.student.Compass_Abroad.modal.preferCountryList.Data

class CountryAdaptor(
    private val context: Context,
    private val list: List<Data>,
    private val country: Select
) : RecyclerView.Adapter<CountryAdaptor.MyViewHolder>() {

    private var selectedPosition: Int = -1

    interface Select {
        fun click(selectedCountries: List<Data>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CompleteProfileRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.countryName.text = currentItem.label

        Glide.with(context)
            .load(currentItem.icon_url)
            .into(holder.binding.imageviewData)

        // ✅ Selection UI
        if (position == selectedPosition) {
            holder.binding.selectedImage.setBackgroundResource(R.drawable.zz_select_green)
        } else {
            holder.binding.selectedImage.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            // Refresh old + new item
            if (previousPosition != -1) notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            country.click(getSelectedCountries())
        }
    }

    override fun getItemCount(): Int = list.size

    fun getSelectedCountries(): List<Data> {
        return if (selectedPosition != -1) {
            listOf(list[selectedPosition])
        } else {
            emptyList()
        }
    }

    // ✅ Pre-select single item
    fun setSelectedByValues(values: List<String>) {
        selectedPosition = list.indexOfFirst { it.value in values }
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding: CompleteProfileRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}