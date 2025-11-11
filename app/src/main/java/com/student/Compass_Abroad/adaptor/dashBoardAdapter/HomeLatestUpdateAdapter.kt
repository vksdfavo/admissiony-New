package com.student.Compass_Abroad.adaptor.dashBoardAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemLatestStudyBinding

class HomeLatestUpdateAdapter : RecyclerView.Adapter<HomeLatestUpdateAdapter.ViewHolder>() {
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use ViewBinding to inflate the layout

        val binding = ItemLatestStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return 15
    }

    class ViewHolder(// Use the generated ViewBinding class
        var binding: ItemLatestStudyBinding
    ) : RecyclerView.ViewHolder(
            binding.getRoot()
    )
}
