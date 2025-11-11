package com.student.Compass_Abroad.Scout.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemscoutshortlistedprogramBinding

class ScoutShortListedAdapter():RecyclerView.Adapter<ScoutShortListedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemscoutshortlistedprogramBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to views here if needed
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(private val binding: ItemscoutshortlistedprogramBinding) : RecyclerView.ViewHolder(binding.root) {
        // Initialize views here if necessary
    }
}
