package com.student.Compass_Abroad


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.ItemInDemandIntuitionsBinding
import com.student.Compass_Abroad.databinding.TopDestinationLayoutBinding

class TopInDemandIntuitionsAdapter(
    private val destinationList: List<com.student.Compass_Abroad.modal.in_demandInstitution.Data>,
    private val onItemClick: ((com.student.Compass_Abroad.modal.in_demandInstitution.Data) -> Unit)? = null
) : RecyclerView.Adapter<TopInDemandIntuitionsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInDemandIntuitionsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInDemandIntuitionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = destinationList[position]
        holder.binding.tvApdCollegeName.text = item.name
        holder.binding.countryName.text = item.country_name
        Glide.with(holder.itemView.context)
            .load(item.logo)
            .into(holder.binding.imgDestination)

        Glide.with(holder.itemView.context)
            .load(item.country_logo)
            .into(holder.binding.countryLogo)

        holder.binding.itemContainer.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = destinationList.size
}
