package com.student.Compass_Abroad.adaptor

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItempaymentotherBinding
import com.student.Compass_Abroad.databinding.ItempaymentpaidBinding
import com.student.Compass_Abroad.fragments.home.QrFragment
import com.stripe.android.paymentsheet.PaymentSheet
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.PaymentDetailBinding
import com.student.Compass_Abroad.fragments.PaymentDetailFragment
import java.util.Locale
import androidx.navigation.findNavController

class AdapterFragmentPayments(
    private val context: FragmentActivity?,
    private var leadPaymentList: MutableList<com.student.Compass_Abroad.modal.getPaymentApplication.Record>,
    var paymentSheet: PaymentSheet,
    var selector: select
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        const val VIEW_TYPE_PAID = 1
        const val VIEW_TYPE_OTHER = 2
    }

    interface select {
        fun click(
            record: FragmentActivity,
            identifier: String,
            wisePayment: com.student.Compass_Abroad.modal.getPaymentApplication.Record,
            binding: ItempaymentotherBinding,
            s1: String
        )

        fun click(
            context: FragmentActivity,
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
    ): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_PAID) {
            val binding =
                ItempaymentpaidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PaidViewHolder(binding)
        } else {
            val binding =
                ItempaymentotherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun bind(record: com.student.Compass_Abroad.modal.getPaymentApplication.Record) {


            binding.tvviewdetails.setOnClickListener { v ->
                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {


                    PaymentDetailFragment.data = record.identifier
                    binding.root.findNavController().navigate(R.id.paymentDetailFragment2)

                } else {


                    PaymentDetailFragment.data = record.identifier
                    binding.root.findNavController().navigate(R.id.paymentDetailFragment)

                }
            }

            if (!record.payment_info.payment_type_info.name.isNullOrEmpty()) {
                binding.tvLabel.text = record.payment_info.payment_type_info.name
            } else {
                binding.tvLabel.text = "----"
            }

            if (!record.payment_info.currency.isNullOrEmpty() && record.payment_info.price > 0) {
                binding.tvapplicationfee.text =
                    "${record.payment_info.currency} ${record.payment_info.price}"
            } else {
                binding.tvapplicationfee.text = "--------"
            }

            if (!record.payment_info.created_at.isNullOrEmpty()) {
                binding.tvdateTime.text = CommonUtils.convertDate3(
                    record.payment_info.created_at,
                    "dd-MMM-yy hh:mm:ss a"
                )
            } else {
                binding.tvdateTime.text = "--------"
            }
        }
    }

    inner class OtherViewHolder(
        private val binding: ItempaymentotherBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            record: com.student.Compass_Abroad.modal.getPaymentApplication.Record,
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

                "Wise" -> {
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

            if (!record.payment_info.currency.isNullOrEmpty() && record.payment_info.price > 0) {
                binding.tvApplicationfee.text =
                    "${record.payment_info.currency} ${record.payment_info.price}"
            } else {
                binding.tvApplicationfee.text = "--------"
            }

            if (!record.payment_gateway_info.name.isNullOrEmpty()) {
                binding.tvName.text = "${record.payment_gateway_info.name}"
            } else {
                binding.tvName.text = "--------"
            }



            when (record.payment_gateway_info.name.toLowerCase(Locale.getDefault())) {
                "stripe" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        selector.click(context!!, record.identifier, binding, "stripe")

                    }

                }

                "razorpay" -> {
                    binding.tvViewDetails.setOnClickListener { v: View ->
                        selector.click(context!!, record.identifier, binding, "razorpay")

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

                        QrFragment.data = record.payment_gateway_info.url
                        v.findNavController().navigate(R.id.qrFragment)
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
    // Set background color and text based on payment gateway name


}

fun showBankTransferDialog(
    context: FragmentActivity,
    record: com.student.Compass_Abroad.modal.getPaymentApplication.Record,

    ) {
    // Inflate the custom dialog layout
    val dialogView = LayoutInflater.from(context).inflate(R.layout.item_bank_transfer_layout, null)

    // Example static reasons for the Spinner

    // Initialize buttons
    val btn_ok: Button = dialogView.findViewById(R.id.btn_ok)
    val btnCopy: Button = dialogView.findViewById(R.id.btn_copy)
    val tv_details: TextView = dialogView.findViewById(R.id.tv_details)
    val tv_dismiss: ImageView = dialogView.findViewById(R.id.tv_dismiss)

    tv_details.setText(record.payment_gateway_info.gateway_content_info)

    // Create the AlertDialog
    val dialogBuilder = AlertDialog.Builder(context)
        .setView(dialogView)


    val alertDialog = dialogBuilder.create()
    alertDialog.show()

    btn_ok.setOnClickListener { v: View ->
        alertDialog.dismiss()

    }

    tv_dismiss.setOnClickListener { v: View ->
        alertDialog.dismiss()

    }

    btnCopy.setOnClickListener {
        val textToCopy = tv_details.text.toString()

        // Get the ClipboardManager
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a ClipData with the text to copy
        val clip = ClipData.newPlainText("Copied Text", textToCopy)

        // Set the ClipData to the ClipboardManager
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        // Optionally, show a toast message to inform the user

    }
}

fun showCashPaymentDialog(
    context: FragmentActivity,
) {
    // Inflate the custom dialog layout
    val dialogView = LayoutInflater.from(context).inflate(R.layout.item_cash_payment_layout, null)

    // Example static reasons for the Spinner

    // Initialize buttons
    val btn_ok: Button = dialogView.findViewById(R.id.btn_ok)

    val tv_dismiss: ImageView = dialogView.findViewById(R.id.tv_dismiss)


    // Create the AlertDialog
    val dialogBuilder = AlertDialog.Builder(context)
        .setView(dialogView)


    val alertDialog = dialogBuilder.create()
    alertDialog.show()

    btn_ok.setOnClickListener { v: View ->
        alertDialog.dismiss()

    }

    tv_dismiss.setOnClickListener { v: View ->
        alertDialog.dismiss()

    }
}