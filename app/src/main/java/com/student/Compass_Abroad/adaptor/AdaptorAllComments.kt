package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.CommonUtils.getTimeAgo
import com.student.Compass_Abroad.Utils.CommonUtils.removeHtmlTags
import com.student.Compass_Abroad.databinding.ItemCommentsBinding
import com.student.Compass_Abroad.encrytion.decryptData
import com.student.Compass_Abroad.fragments.home.AddFragmentReply
import com.student.Compass_Abroad.fragments.home.FragmentCommentEdit
import com.student.Compass_Abroad.modal.getAllComments.Record
import com.student.Compass_Abroad.modal.getCommentReplies.getCommentReplies
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.util.ArrayList
import androidx.navigation.findNavController

class AdaptorAllComments(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<Record>,
    var post: com.student.Compass_Abroad.modal.getAllPosts.Record?,
    var selectListener: select?,
) : RecyclerView.Adapter<AdaptorAllComments.MyViewHolder>() {

    private val commentRepliesMap =
        HashMap<String, ArrayList<com.student.Compass_Abroad.modal.getCommentReplies.Record>>()
    private val isReplyVisibleMap = HashMap<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = arrayList1[position]
        holder.bind(currentItem)

        val isReplyVisible = isReplyVisibleMap[currentItem.identifier] ?: false
        holder.binding.rvCa.visibility = if (isReplyVisible) View.VISIBLE else View.GONE
        val arrowDrawable =
            if (isReplyVisible) R.drawable.avd_up_to_down else R.drawable.avd_down_to_up
        holder.binding.ivArrow.setImageResource(arrowDrawable)
        (holder.binding.ivArrow.drawable as? Animatable)?.start()
        holder.binding.ivArrow.visibility=View.GONE
        if(currentItem.commentReplyCount>0){
            holder.binding.ivArrow.visibility=View.VISIBLE
        }

        holder.binding.tvReplyCount.setOnClickListener {
            val postIdentifier = post!!.identifier
            val commentIdentifier = currentItem.identifier

            // Toggle the visibility state for the clicked comment's replies
            val currentVisibility = isReplyVisibleMap[commentIdentifier] ?: false
            val newVisibility = !currentVisibility
            isReplyVisibleMap[commentIdentifier] = newVisibility

            // Notify the specific item changed only if the visibility has changed
            if (newVisibility != currentVisibility) {
                notifyItemChanged(position)
            }

            if (newVisibility) {

                onGetAllCommentsReplies(requireActivity, postIdentifier, commentIdentifier)
            }
        }

        val replies = commentRepliesMap[currentItem.identifier]
        if (!replies.isNullOrEmpty()) {
            val layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.binding.rvCa.layoutManager = layoutManager
            val adapterCommunityPostReply = AdapterCommunityPostReply(
                requireActivity,
                replies,
                post,
                object : AdapterCommunityPostReply.select {
                    override fun onCLick(

                        record: com.student.Compass_Abroad.modal.getCommentReplies.Record,
                        post: com.student.Compass_Abroad.modal.getAllPosts.Record?

                    ) {
                        selectListener?.onReplyClick(record, post,currentItem.identifier)
                    }
                })
            holder.binding.rvCa.adapter = adapterCommunityPostReply
        } else {
            // If no replies, clear the RecyclerView
            holder.binding.rvCa.adapter = null
        }

        holder.binding.tvReply.setOnClickListener { v: View ->
            AddFragmentReply.postId = post
            AddFragmentReply.commentId = currentItem
            v.findNavController().navigate(R.id.addFragmentReply)
        }

        val publicKey = currentItem.content_key
        val privateKey = AppConstants.privateKey
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val descriptionString = decryptData(currentItem.content, appSecret, ivHexString)
        val data = removeHtmlTags(descriptionString.toString())

        holder.binding.ibCommunityMenu.setOnClickListener { view ->
            // If clicked from "Following" fragment, perform edit and delete actions directly
            val popupMenu = PopupMenu(requireActivity, view)
            popupMenu.menuInflater.inflate(R.menu.menu_edit_post, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "Edit") {
                    FragmentCommentEdit.postIdentifier = post
                    FragmentCommentEdit.commentIdentifier = currentItem
                    FragmentCommentEdit.comment = data
                    // Navigate to edit fragment specific to "Following" fragment
                    // Pass necessary data if needed
                    view.findNavController().navigate(R.id.fragmentCommentEdit)
                    true
                } else if (menuItem.title == "Delete") {
                    selectListener!!.onCLick(currentItem, post)
                    notifyDataSetChanged()
                    true
                } else {
                    false
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return arrayList1.size
    }

    class MyViewHolder(var binding: ItemCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(record: Record) {
            val profilePictureUrl = record.userInfo.profile_picture_url

            // Check if profilePictureUrl is null or empty
            if (profilePictureUrl.isEmpty()) {
                // Load a default image using Glide's placeholder functionality
                Glide.with(binding.root)
                    .load(R.drawable.test_image2) // Set your default image resource here
                    .into(binding.civItemComments)
            } else {
                // Load the profile picture using Glide
                Glide.with(binding.root)
                    .load(profilePictureUrl)
                    .into(binding.civItemComments)
            }

            binding.tvItemCommentsName.text = record.userInfo?.first_name
            val timeAgo = getTimeAgo(record.created_at)
            binding.tvItemCommentsTime.text = timeAgo

            val publicKey = record.content_key
            val privateKey = AppConstants.privateKey
            val appSecret = AppConstants.appSecret
            val ivHexString = "$privateKey$publicKey"
            val descriptionString = decryptData(record.content, appSecret, ivHexString)
            val data = removeHtmlTags(descriptionString.toString())

            binding.tvItemCommentsMsg.text = data

            binding.tvReplyCount.text = "${record.commentReplyCount} Replies"
        }
    }

    interface select {
        fun onCLick(record: Record, post: com.student.Compass_Abroad.modal.getAllPosts.Record?)

        fun onReplyClick(
            replyRecord: com.student.Compass_Abroad.modal.getCommentReplies.Record,
            post: com.student.Compass_Abroad.modal.getAllPosts.Record?,
            identifier: String
        )

    }

    private fun onGetAllCommentsReplies(
        requireActivity: FragmentActivity,
        postIdentifier: String,
        commentIdentifier: String,
    ) {
        ViewModalClass().getAllCommentRepliesLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, postIdentifier, commentIdentifier
        ).observe(requireActivity) { getAllCommentsReplies: getCommentReplies? ->
            getAllCommentsReplies?.let { nonNullAllCommentModal ->
                if (getAllCommentsReplies.statusCode == 200) {
                    if (getAllCommentsReplies.data!!.records.isEmpty()) {
                        Toast.makeText(requireActivity, "No Replies Found", Toast.LENGTH_LONG).show()
                    } else {
                        commentRepliesMap[commentIdentifier] = getAllCommentsReplies.data!!.records
                        val position =
                            arrayList1.indexOfFirst { it.identifier == commentIdentifier }
                        if (position != -1) {
                            notifyItemChanged(position)
                        }
                    }
                } else {
                    CommonUtils.toast(
                        requireActivity,
                        nonNullAllCommentModal.message ?: "Failed"
                    )
                }
            }
        }
    }

    fun refreshData(postIdentifier: String, identifier: String) {
        commentRepliesMap.clear();
        onGetAllCommentsReplies(this.requireActivity,postIdentifier,identifier )
    }
}


