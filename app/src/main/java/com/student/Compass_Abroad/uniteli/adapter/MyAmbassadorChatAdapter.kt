package com.student.Compass_Abroad.uniteli.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.NewConversationsLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MyAmbassadorChatAdapter(
    var requireActivity: FragmentActivity,
    var arrayList1: MutableList<com.student.Compass_Abroad.modal.ambassadroChatList.Record>,
    var selector: OnChatClick
) : RecyclerView.Adapter<MyAmbassadorChatAdapter.ViewHolder>() {

    interface OnChatClick {

        fun onClick(recordInfo: com.student.Compass_Abroad.modal.ambassadroChatList.Record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NewConversationsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = arrayList1[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return arrayList1.size

    }

    inner class ViewHolder(private val binding: NewConversationsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: com.student.Compass_Abroad.modal.ambassadroChatList.Record) {

            binding.root.setOnClickListener {
                selector?.onClick(data)
            }
            val fullName = data.ambassadorInfo?.let { info ->
                val firstName = info.first_name?.takeIf { it.isNotBlank() }
                val lastName = info.last_name?.takeIf { it.isNotBlank() }
                listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { "---" }
            } ?: "---"

            binding.name.text = fullName

            if (data.lastMessage.isNotEmpty()) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                outputFormat.timeZone = TimeZone.getDefault()

                val dateString = data.lastMessage[0].created_at
                val date = inputFormat.parse(dateString)
                if (date != null) {
                    binding.time.text = outputFormat.format(date)
                } else {
                    binding.time.text = "" // or "Invalid time"
                }
            } else {
                binding.time.text = "" // or "No time available"
            }


            if (data.lastMessage.isNotEmpty()) {
                binding.lastMessage.text = data.lastMessage[0].content
            } else {
                binding.lastMessage.text = "" // or "No messages yet", as appropriate
            }

            data.ambassadorInfo?.profile_picture_url?.let { url ->
                Glide.with(requireActivity)
                    .load(url)
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .into(binding.profileImage)
            } ?: run {
                // Load default image if ambassadorInfo or profile_picture_url is null
                binding.profileImage.setImageResource(R.drawable.user_profile)
            }


        }
    }
}
