package com.student.Compass_Abroad.Scout.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemReceivedBinding
import com.student.Compass_Abroad.databinding.ItemSentBinding
import com.student.Compass_Abroad.databinding.ItemwithbadgeBinding

class AdapterScoutApplicationDetailConversation( var  context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_RECEIVED = 0
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_BADGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return 0
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

    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolderReceived(private val binding: ItemReceivedBinding, var context: Context?) :
        RecyclerView.ViewHolder(binding.root) {



    }

    class ViewHolderSent(private val binding: ItemSentBinding, var context: Context?) :
        RecyclerView.ViewHolder(binding.root) {




    }

    class ViewHolderBadge(private val binding: ItemwithbadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}