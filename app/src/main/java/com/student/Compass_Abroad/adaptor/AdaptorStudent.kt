package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemStudentsBinding

class AdaptorStudent(context: Context?) : RecyclerView.Adapter<AdaptorStudent.ViewHolder>() {
    private val context: Context? = null
    private var binding: ItemStudentsBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemStudentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!.getRoot())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemStudentsBinding

        init {
            binding = ItemStudentsBinding.bind(itemView)
        }
    }
}
