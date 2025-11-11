package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemProgramCategoryBinding
import com.student.Compass_Abroad.modal.getCategoryProgramModel.Data

class CategoryProgramAdapter(var requireActivity: FragmentActivity, var items: List<Data>, var selectListener: Select?):
    RecyclerView.Adapter<CategoryProgramAdapter.MyViewHolder>() {
    private var selectedItemPosition = 0
    interface Select{
        fun onCLick(record: Data, position: Int);
        //fun onClick1(record: String, position: Int);
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemProgramCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        var recordItem=items[position]

        holder.binding.rrl1.setOnClickListener {
            selectListener?.onCLick(recordItem, position)
            selectedItemPosition=position

            sharedPre!!.saveString(AppConstants.CATEGORY,recordItem.name)

            sharedPre!!.saveString(AppConstants.PROGRAM_CATEGORY,recordItem.name)

            notifyDataSetChanged()


        }

        if(recordItem.name.isNotEmpty()){
            holder.binding.tvCategories.text=recordItem.label
        }else{
            holder.binding.tvCategories.text="Not Found"
        }


        if(selectedItemPosition == position){
            holder.binding.rr2.isSelected = true

        }else{
            holder.binding.rr2.isSelected = false
        }
    }

    override fun getItemCount(): Int {
        return items.size

    }
    fun setSelectedItem(position: Int) {
        if (position in items.indices) { // Ensure position is valid
            val selectedRecord = items[position]
           // selectListener?.onClick1(selectedRecord.name, position)
            selectedItemPosition = position
            notifyDataSetChanged()
        }
    }

    class MyViewHolder(

        var binding: ItemProgramCategoryBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    )
}