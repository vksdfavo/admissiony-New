package com.student.Compass_Abroad.Scout.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemscoutapplicationactiveBinding

class AdapterScoutApplicationActive(): RecyclerView.Adapter<AdapterScoutApplicationActive.ViewHolder>() {
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemscoutapplicationactiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.scoutApplicationDetail)
        }
    }

    override fun getItemCount(): Int {
        return 15
    }

    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemscoutapplicationactiveBinding
    ) : RecyclerView.ViewHolder(
        binding.getRoot()
    )
}