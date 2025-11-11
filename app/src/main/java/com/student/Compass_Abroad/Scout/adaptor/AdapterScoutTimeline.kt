package com.student.Compass_Abroad.Scout.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemscouttimelineBinding

class AdapterScoutTimeline() : RecyclerView.Adapter<AdapterScoutTimeline.MyViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemscouttimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 14
    }

    class MyViewHolder(
        var binding: ItemscouttimelineBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {





    }
}