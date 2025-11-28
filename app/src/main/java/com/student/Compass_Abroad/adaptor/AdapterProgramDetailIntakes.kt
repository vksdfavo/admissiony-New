package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemIntakesBinding
import com.student.Compass_Abroad.modal.getProgramDetails.Intake

class AdapterProgramDetailIntakes (var requireActivity: FragmentActivity, var intakes: List<Intake>?) :
    RecyclerView.Adapter<AdapterProgramDetailIntakes.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemIntakesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        var curentItem= intakes!![position]
        holder.binding.tvItemIntakes.text = curentItem.intake_name
    }

    override fun getItemCount(): Int {
        return intakes!!.size
    }
    class MyViewHolder(
        var binding: ItemIntakesBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()

    )
}