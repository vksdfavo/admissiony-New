package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemIntakesBinding
import com.student.Compass_Abroad.modal.AllProgramModel.Testscore

class AdapterProgramDetailDetailsETS(var requireActivity: FragmentActivity, var intakes: List<Testscore>?) :
    RecyclerView.Adapter<AdapterProgramDetailDetailsETS.MyViewHolder>(){
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
        var curentItem=intakes!!.get(position)


        val formattedString = "${curentItem.score_name}: " +
                "${curentItem.program_testscore.score}" +
                (curentItem.program_testscore.min_score?.let { "($it)" } ?: "")

        holder.binding.tvItemIntakes.text = formattedString

    }

    override fun getItemCount(): Int {
        return intakes!!.size
    }
    class MyViewHolder(// Use the generated ViewBinding class
        var binding: ItemIntakesBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()



    ) {

    }


}