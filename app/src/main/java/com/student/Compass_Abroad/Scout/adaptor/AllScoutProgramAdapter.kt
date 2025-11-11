package com.student.Compass_Abroad.Scout.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemscoutprogramBinding

class AllScoutProgramAdapter(): RecyclerView.Adapter<AllScoutProgramAdapter.ViewHolder>() {
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemscoutprogramBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 15
    }

    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemscoutprogramBinding
    ) : RecyclerView.ViewHolder(
        binding.getRoot()
    )

}