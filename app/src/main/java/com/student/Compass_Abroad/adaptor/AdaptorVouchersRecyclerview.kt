package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemVoucherBinding

import com.student.Compass_Abroad.modal.getVoucherModel.Record

class AdaptorVouchersRecyclerview(var requireActivity: FragmentActivity?, var  arrayList1: ArrayList<Record>, private val maxItemCount: Int,var selector:Select) :
RecyclerView.Adapter<AdaptorVouchersRecyclerview.ViewHolder>() {

    var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVoucherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    interface Select{
        fun select(position: Record, position1: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = arrayList1[position]
        with(holder.binding) {
//            tvVoucherTitle.text = currentItem.name ?: "N/A"
//            tvVoucherDesc.text = currentItem.description ?: ""
            tvVoucherPrice.text = "${currentItem.display_currency.uppercase()} ${currentItem.display_amount}"
            Glide.with(imgVoucher.context)
                .load(currentItem.file_url)
                .placeholder(R.drawable.placeholder)
                .into(imgVoucher)
            btnBuy.setOnClickListener {
                selector.select(currentItem,position)
            }
        }
    }
    override fun getItemCount(): Int {
        return if (arrayList1.size > maxItemCount) maxItemCount else arrayList1.size

    }

    class ViewHolder(
        var binding: ItemVoucherBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()



    )
}