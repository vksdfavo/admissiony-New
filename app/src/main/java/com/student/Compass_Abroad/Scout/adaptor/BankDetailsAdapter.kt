package com.student.Compass_Abroad.Scout.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.Info
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.RecordsInfo
import com.student.Compass_Abroad.databinding.ItemBanksBinding

class BankDetailsAdapter(
    var context: Context?,
    private var recordsList: List<RecordsInfo>,
    private var record: List<RecordsInfo>,
    var listener: SetOnClickListener
) : RecyclerView.Adapter<BankDetailsAdapter.ViewHolder>() {

    interface SetOnClickListener{

        fun listener(record: RecordsInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBanksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = recordsList[position].info
        val fields = record[position]


        if (fields.is_primary == 1) {
            holder.binding.isPrimary.visibility = View.VISIBLE
        } else {
            holder.binding.isPrimary.visibility = View.GONE
        }

        holder.binding.setIsPrimary.setOnClickListener {

            listener?.listener(fields)
        }


        holder.bind(field)
    }

    override fun getItemCount(): Int = recordsList.size

    inner class ViewHolder(val binding: ItemBanksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: List<Info>) {
            binding.tvCountry.text = "India"
            binding.tvBankName.text = "No Bank Name"
            binding.tvBankAddress.text = "N/A"
            binding.tvAccountHolderName.text = "N/A"
            binding.tvAccountNumber.text = "N/A"
            binding.tvSwiftCode.text = "N/A"

            for (info in record) {
                when (info.name) {
                    "bank_name" -> binding.tvBankName.text = info.value ?: "No Bank Name"
                    "bank_address" -> binding.tvBankAddress.text = "${info.value ?: "N/A"}"
                    "account_name" -> binding.tvAccountHolderName.text = info.value ?: "N/A"
                    "account_number" -> binding.tvAccountNumber.text = info.value ?: "N/A"
                    "ifsc_code" -> binding.tvIfscCode.text =
                        if (info.value.isNullOrEmpty()) "----" else info.value

                    "swift_code" -> binding.tvSwiftCode.text = info.value ?: "----"
                }
            }
        }


    }
}




