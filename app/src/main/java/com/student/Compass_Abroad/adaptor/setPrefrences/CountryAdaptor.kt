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

    var selectedItemPosition: Int = RecyclerView.NO_POSITION

    interface Select {

        fun click(selectCountry: Data?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CompleteProfileRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = list[position]
        holder.binding.countryName.text = currentItem.label

        Glide.with(context)
            .load(currentItem.icon_url)
            .into(holder.binding.imageviewData)

        val isSelected = selectedItemPosition == position
        if (isSelected) {
            holder.binding.selectedImage.setBackgroundResource(R.drawable.zz_select_green)
        } else {
            holder.binding.selectedImage.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = position
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)

            country.click(currentItem)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getSelectedCountry(): Data? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            list[selectedItemPosition]
        } else null
    }

    class MyViewHolder(val binding: CompleteProfileRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
