package com.student.Compass_Abroad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.ShimmerTopDestinationBinding
import com.student.Compass_Abroad.databinding.TopDestinationLayoutBinding
import com.student.Compass_Abroad.modal.top_destinations.Data

class TopDestinationAdapter(
    private var destinationList: List<Data>,
    private val onItemClick: ((Data) -> Unit)? = null,
    private var isLoading: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SHIMMER = 0
    private val VIEW_TYPE_DATA = 1

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    class DataViewHolder(val binding: TopDestinationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ShimmerViewHolder(val binding: ShimmerTopDestinationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ShimmerTopDestinationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = TopDestinationLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            DataViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) 6 else destinationList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ShimmerViewHolder) {
            holder.binding.shimmerTopDestination.startShimmer()
            return
        }

        holder as DataViewHolder
        val item = destinationList[position]

        holder.binding.apply {
            countryName.text = item.country_name
            totalIns.text = "${item.total_institutions} institutions"

            Glide.with(root.context)
                .load(item.institution_logo)
                .placeholder(R.drawable.circle_img)
                .into(imgDestination)

            Glide.with(root.context)
                .load(item.country_logo)
                .placeholder(R.drawable.circle_img)
                .into(flag)

            itemContainers.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    fun updateList(newList: List<Data>) {
        isLoading = false
        destinationList = newList
        notifyDataSetChanged()
    }
}
