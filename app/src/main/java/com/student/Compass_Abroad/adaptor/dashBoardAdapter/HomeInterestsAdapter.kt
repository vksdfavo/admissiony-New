package com.student.Compass_Abroad.adaptor.dashBoardAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemInterestsBinding

class HomeInterestsAdapter : RecyclerView.Adapter<HomeInterestsAdapter.ViewHolder>() {
    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use ViewBinding to inflate the layout
        val binding =
                ItemInterestsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // bind view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return 15
    }

    // view holder
    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemInterestsBinding
    ) : RecyclerView.ViewHolder(
            binding.getRoot()
    )
}
