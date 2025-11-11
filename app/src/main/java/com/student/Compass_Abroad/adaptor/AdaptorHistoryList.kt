package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemvoucherhistoryBinding
import com.student.Compass_Abroad.modal.getHistoryListModel.RecordsInfo


class AdaptorHistoryList(
    private val requireActivity: FragmentActivity,
    private val historyList: ArrayList<RecordsInfo>
) : RecyclerView.Adapter<AdaptorHistoryList.ViewHolder>() {

    inner class ViewHolder(val binding: ItemvoucherhistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemvoucherhistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        val b = holder.binding



        // Name and Type
        val moduleData = item.feePaymentLinkInfo?.module_info?.data
        b.tvName.text = moduleData?.name ?: "N/A"
        b.tvType.text = moduleData?.voucher_type_info?.name ?: "N/A"

        //total
        val total = item.feePaymentLinkInfo?.to_be_paid_amount ?: 0
        val Currency = item.feePaymentLinkInfo?.to_be_paid_currency ?: ""
        b.tvTotal.text = "$Currency $total"

        // Qty
        val quantity = item.voucherLink?.quantity ?: 0
        b.tvQty.text = quantity.toString()

        // Unit Price
        val unitPrice = total/quantity
        b.tvUnitPrice.text = "$Currency$unitPrice"


        // Status
        // Status Text
        val statusText = when (item.voucherLink?.status?.lowercase()) {
            "paid" -> "Paid"
            "unpaid" -> "Unpaid"
            else -> "In Progress"
        }
        b.tvPaymentMode.text = statusText

// Status Color
        when (statusText) {
            "Paid" -> {
                b.tvPaymentMode.setTextColor(android.graphics.Color.parseColor("#c85c00"))
                b.tvPaymentMode.setBackgroundResource(R.drawable.bg_status_paid)
            }
            "Unpaid" -> {
                b.tvPaymentMode.setTextColor(android.graphics.Color.parseColor("#F57C00"))
                b.tvPaymentMode.setBackgroundResource(R.drawable.bg_status_unpaid)
            }
            else -> {
                b.tvPaymentMode.setTextColor(android.graphics.Color.parseColor("#546d6b"))
                b.tvPaymentMode.setBackgroundResource(R.drawable.bg_status_in_progress)
            }
        }

    }


    override fun getItemCount(): Int = historyList.size
}