package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemleadpaymentBinding
import com.student.Compass_Abroad.modal.getLeadPaymentLinks.Record

class AdapterLeadPayment(
    private val context: Context,
    private val leadPaymentList: MutableList<com.student.Compass_Abroad.modal.getLeadPaymentLinks.Record>
) : RecyclerView.Adapter<AdapterLeadPayment.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemleadpaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val payment = leadPaymentList[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int {
        return leadPaymentList.size
    }

    inner class MyViewHolder(private val binding: ItemleadpaymentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record) {
            // Payment Type Info Name
            if (record.payment_info != null && record.payment_info.payment_type_info != null && !record.payment_info.payment_type_info.name.isNullOrEmpty()) {
                binding.tvVisaFeeLabel.text = record.payment_info.payment_type_info.name
            } else {
                binding.tvVisaFeeLabel.text = "----"
                // or handle as per your application's requirements
            }

            // Price and Currency
            if (!record.payment_info.price.isNullOrEmpty() && !record.payment_info.currency.isNullOrEmpty()) {
                binding.tvApplicationfee.text = "${record.payment_info.price} ${record.payment_info.currency}"
            } else {
                binding.tvApplicationfee.text = "----"
                // or handle as per your application's requirements
            }

            // Created At Date Time
            if (!record.created_at.isNullOrEmpty()) {
                val formattedDate = CommonUtils.convertDate3(record.created_at, "dd-MMM-yy hh:mm:ss a")
                if (formattedDate != null) {
                    binding.tvDateTime.text = formattedDate
                } else {

                    binding.tvDateTime.text = "---"
                    // or handle as per your application's requirements
                }
            } else {
                binding.tvDateTime.text = "---"
                // or handle as per your application's requirements
            }

            // Created By


            // Paid Status
            if (record.payment_info.status == "created") {
                binding.tvCreated.visibility = View.VISIBLE
                binding.tvCreated.text = record.payment_info.status
                binding.tvCreated.setBackgroundResource(R.drawable.ic_paid_back)
                binding.tvCreated.setTextColor(ContextCompat.getColor(context, R.color.PopUpcolorPrimary))
            } else {
                binding.tvpaid.visibility = View.GONE
            }

            // Item Click Listener
            binding.root.setOnClickListener {
                // Handle item click here
                // Example: Toast.makeText(context, "Item clicked: ${record.identifier}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}