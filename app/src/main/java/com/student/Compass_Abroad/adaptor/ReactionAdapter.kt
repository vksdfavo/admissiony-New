package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemReactionBinding
import com.student.Compass_Abroad.modal.reactionModel.Data
import java.util.ArrayList


class ReactionAdapter(private val reactions: ArrayList<Data>) :
    RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val binding = ItemReactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val reaction = reactions[position]
        holder.bind(reaction,position)
    }

    override fun getItemCount(): Int {
        return reactions.size
    }

    fun updateReactions(newReactions: ArrayList<Data>?) {
        reactions.clear()
        if (newReactions != null) {
            reactions.addAll(newReactions)
        }
        notifyDataSetChanged()
    }

    class ReactionViewHolder(private val binding: ItemReactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reaction: Data, position: Int) {
            // Bind your reaction data to the UI elements here

            val profilePictureUrl = reaction.rows.get(position).userInfo?.profile_picture_url

            // Check if profilePictureUrl is null or empty
            if (profilePictureUrl.isNullOrEmpty()) {
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

            binding.tvItemCommentsName.text = reaction.rows.get(position).userInfo?.first_name
            // Add other UI bindings as needed
        }
    }
}