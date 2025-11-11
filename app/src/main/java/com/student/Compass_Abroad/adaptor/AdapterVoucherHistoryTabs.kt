package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemHistoryTabsBinding

class AdapterVoucherHistoryTabs(
    private val tabList: List<com.student.Compass_Abroad.modal.getVouchersHistoryTabs.Record>,
    private val onTabClick: ((com.student.Compass_Abroad.modal.getVouchersHistoryTabs.Record) -> Unit)? = null
) : RecyclerView.Adapter<AdapterVoucherHistoryTabs.TabViewHolder>() {

    inner class TabViewHolder(val binding: ItemHistoryTabsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding = ItemHistoryTabsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val item = tabList[position]
        with(holder.binding) {
            tvTestName.text = item.name
            tvSoldInfo.text =item.sold_voucher

            root.setOnClickListener {
                onTabClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = tabList.size
}