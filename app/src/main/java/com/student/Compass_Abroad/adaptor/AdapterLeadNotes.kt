package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.ItemleadnotesBinding
import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.modal.getLeadNotes.Record

class AdapterLeadNotes(var context: Context?,var  leadNotesList: MutableList<Record>,):RecyclerView.Adapter<AdapterLeadNotes.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterLeadNotes.MyViewHolder {
        val binding =
            ItemleadnotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterLeadNotes.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterLeadNotes.MyViewHolder, position: Int) {
       var record=leadNotesList[position]

        holder.bind(record,context)
    }

    override fun getItemCount(): Int {
     return leadNotesList.size
    }

    class MyViewHolder(

        var binding: ItemleadnotesBinding

    ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(record: Record, context: Context?) {

            binding.tvTitle.text = record.title ?: ""

            val publicKey = record.content_key
            val privateKey = AppConstants.privateKey
            val appSecret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"
            val descriptionString = decryptData(record.content, appSecret, ivHexString)
            val data = CommonUtils.removeHtmlTags(descriptionString?.toString() ?: "")
            binding.tvDescription.text = data

            val color = try {
                Color.parseColor(record.color)
            } catch (e: IllegalArgumentException) {
                Color.WHITE // Default color if parsing fails
            }
            binding.cdView.setCardBackgroundColor(color)

            val image = record.created_by_info?.profile_picture_url
            if (!image.isNullOrEmpty()) {
                context?.let {
                    Glide.with(it)
                        .load(image)
                        .placeholder(R.drawable.test_image) // Optional placeholder image
                        .error(R.drawable.test_image) // Optional error image
                        .circleCrop() // Apply circle transformation if needed
                        .into(binding.civProfilePic)
                }
            } else {
                // Handle case when image URL is empty or null
                // You may want to set a default image or hide the ImageView
                binding.civProfilePic.setImageResource(R.drawable.test_image2)
            }

            val firstName = record.created_by_info?.first_name ?: ""
            val lastName = record.created_by_info?.last_name ?: ""
            binding.tvName.text = "$firstName $lastName"

            val createdDate = CommonUtils.convertDate3(record.created_at ?: "", "dd-MM-yy hh:mm:ss a")
            binding.tvDate.text = createdDate



        }
    }


}