package com.student.Compass_Abroad.uniteli.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.formatToLocalTime
import com.student.Compass_Abroad.Utils.formatUtcToLocalTime
import com.student.Compass_Abroad.databinding.ItemReceivedChatBinding
import com.student.Compass_Abroad.databinding.ItemSentChatBinding
import com.student.Compass_Abroad.modal.ambassadorGetChat.Record


class AmbassadorAdapterDetailConversation(
    private val context: Context?,
    private val chatRecords: MutableList<Record>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SENT = 0
        const val VIEW_TYPE_RECEIVED = 1
    }

    override fun getItemViewType(position: Int): Int {
        val record = chatRecords[position]
        val currentUserId = App.sharedPre?.getString(AppConstants.User_IDENTIFIER, "") ?: ""
        val senderId = record.recieverInfo?.identifier.orEmpty()

        Log.d("getItemViewType", "Position $position - CurrentUser: $currentUserId, Receiver: $senderId")

        return if (senderId == currentUserId) VIEW_TYPE_RECEIVED else VIEW_TYPE_SENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_RECEIVED -> {
                val binding = ItemReceivedChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderReceived(binding, context)
            }
            VIEW_TYPE_SENT -> {
                val binding = ItemSentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderSent(binding, context)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = chatRecords[position]
        Log.d("BindView", "Position $position, ViewType ${holder.itemViewType}")

        when (holder.itemViewType) {
            VIEW_TYPE_RECEIVED -> (holder as ViewHolderReceived).bind(record)
            VIEW_TYPE_SENT -> (holder as ViewHolderSent).bind(record)
        }
    }

    override fun getItemCount(): Int = chatRecords.size

    class ViewHolderReceived(
        private val binding: ItemReceivedChatBinding,
        private var context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(record: Record) {
            resetViews()

            // Corrected: Use senderInfo for received message
            val fullName = "${record.senderInfo?.first_name.orEmpty()} ${record.senderInfo?.last_name.orEmpty()}".trim()
            binding.tvItemCoName1.text = if (fullName.isNotEmpty()) fullName else "Unknown Name"

            binding.tvItemCoDesignation1.text = "ambassador"

            val content = record.content?.takeIf { it.isNotEmpty() } ?: "No content available"
            val spannedText = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvItemCoMsg1.text = spannedText.trim()
            binding.tvItemCoMsg1.movementMethod = LinkMovementMethod.getInstance()
            binding.tvItemCoMsg1.setLinkTextColor(ContextCompat.getColor(context!!, R.color.theme_color))

            binding.tvItemCoTime1.text = formatUtcToLocalTime(record.created_at)

            // Corrected: Load profile image from senderInfo
            record.senderInfo?.profile_picture_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.civItemCoImage1)
            } ?: binding.civItemCoImage1.setImageResource(R.drawable.ic_profile)

            // Optional badge logic (if needed)
        }

        private fun resetViews() {
            binding.tvItemCoName1.text = ""
            binding.tvItemCoDesignation1.text = ""
            binding.tvItemCoMsg1.text = ""
            binding.tvItemCoTime1.text = ""
            binding.civItemCoImage1.setImageResource(R.drawable.ic_profile)
        }
    }

    class ViewHolderSent(
        private val binding: ItemSentChatBinding,
        var context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record) {
            resetViews()


            val fullName = "${record.senderInfo?.first_name.orEmpty()} ${record.senderInfo?.last_name.orEmpty()}"
            binding.tvItemCoName0.text = fullName
            binding.tvItemCoDesignation0.text = "student" ?: "No name"

            val content = record.content?.takeIf { it.isNotEmpty() } ?: "No content available"
            val spannedText = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvItemCoMsg0.text = spannedText.trim()
            binding.tvItemCoMsg0.movementMethod = LinkMovementMethod.getInstance()
            binding.tvItemCoMsg0.setLinkTextColor(ContextCompat.getColor(context!!, R.color.theme_color))

            binding.tvItemCoTime0.text = formatUtcToLocalTime(record.created_at)



            record.senderInfo?.profile_picture_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.civItemCoImage0)
            } ?: binding.civItemCoImage0.setImageResource(R.drawable.ic_profile)
        }
        private fun resetViews() {
            binding.tvItemCoName0.text = ""
            binding.tvItemCoDesignation0.text = ""
            binding.tvItemCoMsg0.text = ""
            binding.tvItemCoTime0.text = ""
            binding.civItemCoImage0.setImageResource(R.drawable.ic_profile)
        }
    }
}
