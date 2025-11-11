package com.student.Compass_Abroad.adaptor.leadToApplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.formatToLocalTime
import com.student.Compass_Abroad.databinding.PaymentDetailBinding
import com.student.Compass_Abroad.modal.paymentDetails.RecordsInfo

class AdapterApplicationPaymentDetails(
    var activity: Context,
    var applicationList: MutableList<RecordsInfo>
) :
    RecyclerView.Adapter<AdapterApplicationPaymentDetails.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PaymentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applicationList[position]

        holder.binding.apply {

            holder.binding.tvDate.text = formatToLocalTime(application.created_at)
            holder.binding.tvPaymentType.text =
                application.feePaymentLinkInfo.payment_info.payment_type_info.name
            holder.binding.tvPaidBy.text =
                application.transaction_by_info.first_name + " " + application.transaction_by_info.last_name
            holder.binding.tvTransactionId.text = application.identifier
            holder.binding.tvCurrency.text = application.paid_currency
            holder.binding.tvAmount.text = application.paid_amount.toString()
            holder.binding.tvStatus.text = application.status

            if (application.status == "success") {
                holder.binding.tvStatus.setBackgroundResource(R.drawable.ic_sucess)
                holder.binding.tvStatus.setTextColor(Color.WHITE)

            } else if (application.status == "failed") {
                holder.binding.tvStatus.setBackgroundResource(R.drawable.ic_failed)
                holder.binding.tvStatus.setTextColor(Color.WHITE)


            } else if (application.status == "progress") {
                holder.binding.tvStatus.setTextColor(Color.BLACK)

                holder.binding.tvStatus.setBackgroundResource(R.drawable.ic_progress)

            }

        }
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }

    class ViewHolder(var binding: PaymentDetailBinding) : RecyclerView.ViewHolder(binding.root)
}
