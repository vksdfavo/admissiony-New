package com.student.Compass_Abroad.adaptor.leadToApplication

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItempaymentotherBinding
import com.student.Compass_Abroad.databinding.ItempaymentpaidBinding
import com.student.Compass_Abroad.fragments.home.QrFragment
import com.student.Compass_Abroad.modal.getLeadPaymentLinks.Record
import com.stripe.android.paymentsheet.PaymentSheet
import java.util.Locale
class AdapterPayments(
    private val context: FragmentActivity?,
    private var leadPaymentList: MutableList<Record>,
    var  paymentSheet: PaymentSheet,
    var selector:select
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var secretKey:String=""
    companion object {

        const val VIEW_TYPE_PAID = 1
        const val VIEW_TYPE_OTHER = 2
    }

    interface select{
        fun click(
            record: FragmentActivity,
            identifier: String,
            binding: ItempaymentotherBinding,
            s1: String
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (leadPaymentList[position].payment_info.status == "paid") {
            VIEW_TYPE_PAID
        } else {
            VIEW_TYPE_OTHER
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ):RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_PAID) {
            val binding = ItempaymentpaidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PaidViewHolder(binding)
        } else {
            val binding = ItempaymentotherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OtherViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = leadPaymentList[position]
        if (holder is PaidViewHolder) {
            holder.bind(record)
        } else if (holder is OtherViewHolder) {
            holder.bind(record, context)
        }

    }

    override fun getItemCount(): Int {
        return leadPaymentList.size
    }

    inner class PaidViewHolder(
        private val binding: ItempaymentpaidBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record) {


            if (!record.payment_info.payment_type_info.name.isNullOrEmpty()) {
                binding.tvLabel.text = record.payment_info.payment_type_info.name
            } else {
                binding.tvLabel.text = "----"
            }

            if (!record.payment_info.currency.isNullOrEmpty() && record.payment_info.price > 0.toString()) {
                binding.tvapplicationfee.text = "${record.payment_info.currency} ${record.payment_info.price}"
            } else {
                binding.tvapplicationfee.text = "--------"
            }

            if (!record.payment_info.created_at.isNullOrEmpty()) {
                binding.tvdateTime.text = "${CommonUtils.convertDate3(record.payment_info.created_at,"dd-MMM-yy hh:mm:ss a")}"
            } else {
                binding.tvdateTime.text = "--------"
            }
        }

    }

    inner class OtherViewHolder(
        private val binding: ItempaymentotherBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            record: Record,
            context: FragmentActivity?
        ) {


            when (record.payment_gateway_info?.type ?: "") {
                "cash" -> {
                    binding.tvName.setBackgroundResource(R.drawable.ic_cash_background)
                    binding.tvViewDetails.text = "View Details"
                }

                "qr" -> {
                    binding.tvName.setBackgroundResource(R.drawable.ic_qr_background)
                    binding.tvViewDetails.text = "Get QR Code"
                }

                "bank" -> {
                    binding.tvName.setBackgroundResource(R.drawable.ic_bank)
                    binding.tvViewDetails.text = "View Details"
                }

                "gateway" -> {
                    binding.tvName.setBackgroundResource(R.drawable.ic_gateway)
                    binding.tvViewDetails.text = "Pay Now"
                }

                else -> {
                    binding.tvName.setBackgroundResource(R.drawable.ic_other_back)
                    binding.tvViewDetails.text = ""
                }
            }


            if (!record.payment_info.payment_type_info.name.isNullOrEmpty()) {
                binding.tvVisaFeeLabel.text = record.payment_info.payment_type_info.name
            } else {
                binding.tvVisaFeeLabel.text = "----"
            }

            if (!record.payment_info.currency.isNullOrEmpty() && record.payment_info.price > 0.toString()) {
                binding.tvApplicationfee.text =
                    "${record.payment_info.currency} ${record.payment_info.price}"
            } else {
                binding.tvApplicationfee.text = "--------"
            }

            if (!record.payment_gateway_info.name.isNullOrEmpty()) {
                binding.tvName.text = "${record.payment_gateway_info.name}"
            } else {
                binding.tvName.text = ""
            }



            when (record.payment_gateway_info.name?.toLowerCase(Locale.getDefault())) {
                "stripe" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        selector.click(context!!, record.identifier,binding,"stripe")

                    }

                }

                "razorpay" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        selector.click(context!!, record.identifier,binding,"razorpay")

                    }
                }


                "bank transfer" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        showBankTransferDialog(
                            context!!,
                            record
                        )
                    }
                }

                "qr payment" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->

                        QrFragment.data= record.payment_gateway_info.url.toString()
                        Navigation.findNavController(v).navigate(R.id.qrFragment)
                    }
                }

                "cash payments" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        showCashPaymentDialog(context!!)
                    }
                }


                else -> {


                }
            }


        }


    }

}

fun showBankTransferDialog(
    context: FragmentActivity,
    record: Record, )
{
    val dialogView = LayoutInflater.from(context).inflate(R.layout.item_bank_transfer_layout, null)
    val btn_ok: Button = dialogView.findViewById(R.id.btn_ok)
    val btnCopy: Button = dialogView.findViewById(R.id.btn_copy)
    val tv_details: TextView = dialogView.findViewById(R.id.tv_details)
    val tv_dismiss: ImageView = dialogView.findViewById(R.id.tv_dismiss)
    val dialogBuilder = AlertDialog.Builder(context)
        .setView(dialogView)


    val alertDialog = dialogBuilder.create()
    alertDialog.show()

    btn_ok.setOnClickListener { v:View->
        alertDialog.dismiss()

    }

    tv_dismiss.setOnClickListener { v:View->
        alertDialog.dismiss()

    }

    btnCopy.setOnClickListener {
        val textToCopy = tv_details.text.toString()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", textToCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

    }
}

fun showCashPaymentDialog(
    context: FragmentActivity, ) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.item_cash_payment_layout, null)
    val btn_ok: Button = dialogView.findViewById(R.id.btn_ok)
    val tv_dismiss: ImageView = dialogView.findViewById(R.id.tv_dismiss)
    val dialogBuilder = AlertDialog.Builder(context).setView(dialogView)
    val alertDialog = dialogBuilder.create()
    alertDialog.show()
    btn_ok.setOnClickListener {
        alertDialog.dismiss()
    }
    tv_dismiss.setOnClickListener {
        alertDialog.dismiss()
    }
}