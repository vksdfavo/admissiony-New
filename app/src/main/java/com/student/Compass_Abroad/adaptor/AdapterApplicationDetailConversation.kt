package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils.getTimeAgo
import com.student.Compass_Abroad.Utils.formatToLocalTime
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.ItemReceivedBinding
import com.student.Compass_Abroad.databinding.ItemSentBinding
import com.student.Compass_Abroad.databinding.ItemwithbadgeBinding
import com.student.Compass_Abroad.modal.getChatResponse.Record

class AdapterApplicationDetailConversation(
    private val context: Context?,
    private val chatRecords: MutableList<Record>,
    private val entity: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_RECEIVED = 0
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_BADGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val record = chatRecords[position]

        return when {
            record.category == "conversation_notification" -> VIEW_TYPE_BADGE
            record.userInfo != null && record.userInfo.identifier == App.sharedPre!!.getString(
                AppConstants.User_IDENTIFIER,
                ""
            ) -> VIEW_TYPE_RECEIVED

            else -> VIEW_TYPE_SENT
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_RECEIVED -> {
                val binding =
                    ItemReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderReceived(binding, context)
            }

            VIEW_TYPE_SENT -> {
                val binding =
                    ItemSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderSent(binding, context)
            }

            VIEW_TYPE_BADGE -> {
                val binding =
                    ItemwithbadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderBadge(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = chatRecords[position]
        Log.d("BindView", "Position $position, ViewType ${holder.itemViewType}")

        when (holder.itemViewType) {
            VIEW_TYPE_RECEIVED -> (holder as ViewHolderReceived).bind(record, entity)
            VIEW_TYPE_SENT -> (holder as ViewHolderSent).bind(record, entity)
            VIEW_TYPE_BADGE -> (holder as ViewHolderBadge).bind(record)
        }
    }

    override fun getItemCount(): Int {
        return chatRecords.size
    }

    class ViewHolderReceived(private val binding: ItemReceivedBinding, var context: Context?) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(record: Record, entity: String?) {
            resetViews()

            Log.d("getItemViewType", App.singleton?.idetity.toString())


            binding.rvAttachments.visibility =
                if (record.has_attachments == "yes") View.VISIBLE else View.GONE

            binding.rvAttachments.setOnClickListener {

                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                    App.singleton?.chatidentifier = record.identifier
                    App.singleton?.idetity = entity
                    Navigation.findNavController(binding.root).navigate(R.id.viewAttachmentFragment2)
                } else {

                    App.singleton?.chatidentifier = record.identifier
                    App.singleton?.idetity = entity
                    Navigation.findNavController(binding.root).navigate(R.id.viewAttachmentFragment)
                }

            }

            val fullName =
                "${record.userInfo!!.first_name?.takeIf { it.isNotEmpty() } ?: ""} ${record.userInfo.last_name?.takeIf { it.isNotEmpty() } ?: ""}".trim()
            binding.tvItemCoName0.text = if (fullName.isNotEmpty()) fullName else "Unknown Name"
            binding.tvItemCoDesignation0.text =
                record.identityInfo!!.name?.takeIf { it.isNotEmpty() } ?: "Unknown Designation"

            val content = record.content?.takeIf { it.isNotEmpty() } ?: "No content available"
            val spannedText = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvItemCoMsg0.text = spannedText.trim()


            binding.tvItemCoMsg0.movementMethod = LinkMovementMethod.getInstance()
            binding.tvItemCoMsg0.setLinkTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.theme_color
                )
            )

            binding.tvItemCoTime0.text = formatToLocalTime(record.published_at.toString())

            record.userInfo.profile_picture_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.civItemCoImage0)
            }
        }

        private fun resetViews() {
            binding.rvAttachments.visibility = View.GONE
            binding.tvItemCoName0.text = ""
            binding.tvItemCoDesignation0.text = ""
            binding.tvItemCoMsg0.text = ""
            binding.tvItemCoTime0.text = ""
            binding.civItemCoImage0.setImageResource(R.drawable.ic_profile)
        }
    }

    class ViewHolderSent(private val binding: ItemSentBinding, var context: Context?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record, entity: String?) {
            resetViews()
            Log.d("getItemViewType", App.singleton?.idetity.toString())

            binding.rvAttachmentsReceived.visibility =
                if (record.has_attachments == "yes") View.VISIBLE else View.GONE

            binding.rvAttachmentsReceived.setOnClickListener {
                App.singleton?.chatidentifier = record.identifier
                App.singleton?.idetity = entity
                Navigation.findNavController(binding.root).navigate(R.id.viewAttachmentFragment)
            }

            val fullName = "${record.userInfo?.first_name ?: ""} ${record.userInfo?.last_name ?: ""}"
            binding.tvItemCoName1.text = fullName
            binding.tvItemCoDesignation1.text = record.identityInfo?.name ?: "No name"


            val content = record.content?.takeIf { it.isNotEmpty() } ?: "No content available"
            val spannedText = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvItemCoMsg1.text = spannedText.trim()

            binding.tvItemCoMsg1.movementMethod = LinkMovementMethod.getInstance()

            binding.tvItemCoMsg1.setLinkTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.theme_color
                )
            )

            binding.tvItemCoTime1.text = formatToLocalTime(record.published_at.toString())


            Log.d("get_time", record.published_at.toString())

            record.userInfo?.profile_picture_url?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.civItemCoImage1)
            } ?: run {
                binding.civItemCoImage1.setImageResource(R.drawable.ic_profile)
            }
        }

        private fun resetViews() {
            binding.tvItemCoName1.text = ""
            binding.tvItemCoDesignation1.text = ""
            binding.tvItemCoMsg1.text = ""
            binding.tvItemCoTime1.text = ""
        }
    }

    class ViewHolderBadge(private val binding: ItemwithbadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record) {
            binding.tvItembadge.visibility = View.VISIBLE
            binding.tvItembadge.text = record.content
        }
    }
}
