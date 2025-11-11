package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemleadreminderBinding
import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.modal.getLeadReminderResponse.Record

class AdapterLeadReminder(var context: Context?, var leadReminderList: MutableList<com.student.Compass_Abroad.modal.getLeadReminderResponse.Record>): RecyclerView.Adapter<AdapterLeadReminder.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterLeadReminder.MyViewHolder {
        val binding =
            ItemleadreminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterLeadReminder.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterLeadReminder.MyViewHolder, position: Int) {
        val data = leadReminderList[position]
        holder.bind(data,context)
    }
    override fun getItemCount(): Int {
        return  leadReminderList.size
    }

    class MyViewHolder(var binding: ItemleadreminderBinding) : RecyclerView.ViewHolder(

        binding.getRoot()
    ) {
        fun bind( data: Record, context: Context?) {
          //  Toast.makeText(context,data.created_at,Toast.LENGTH_LONG).show()

            val publicKey = data.remarkInfo.content_key
            val privateKey = AppConstants.privateKey
            val appSecret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"


            val firstName = data.created_by_info?.first_name ?: "Name not available"
            val lastName = data.created_by_info?.last_name ?: ""
            val createdDate = if (data.created_at != null) CommonUtils.convertDate3(data.created_at, "yyyy-MM-dd hh:mm:ss a") else "NA"
            val title = data.title ?: "title Not Available"
            val status = data.status ?: "Status Not Available"
            val identifier = data.identifier ?: "Id Not Available"

            binding.tvName.text = "$firstName $lastName, $createdDate"
            binding.tvTitle.text = title
            binding.tvStatus.text = status
            binding.tvIdentifier.text = "ID: $identifier"

            val descriptionString = decryptData(data.remarkInfo.content, appSecret, ivHexString)
            val data = CommonUtils.removeHtmlTags(descriptionString?.toString() ?: "")
            binding.tvLeadReminderNotes.text=data

        }
    }
}