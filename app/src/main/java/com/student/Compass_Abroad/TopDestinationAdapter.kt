package com.student.Compass_Abroad


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.TopDestinationLayoutBinding
import com.student.Compass_Abroad.modal.top_destinations.Data

class TopDestinationAdapter(
    private val destinationList: List<Data>,
    private val onItemClick: ((Data) -> Unit)? = null
) : RecyclerView.Adapter<TopDestinationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TopDestinationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TopDestinationLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = destinationList[position]
        holder.binding.countryName.text = item.country_name
        holder.binding.totalIns.text = item.total_institutions.toString()+ " institutions"


        Glide.with(holder.itemView.context).load(item.institution_logo).error(R.drawable.circle_img)
            .into(holder.binding.imgDestination)

        Glide.with(holder.itemView.context).load(item.country_logo).error(R.drawable.circle_img).into(holder.binding.flag)

        // Optional click listener
        holder.binding.itemContainer.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = destinationList.size
}
