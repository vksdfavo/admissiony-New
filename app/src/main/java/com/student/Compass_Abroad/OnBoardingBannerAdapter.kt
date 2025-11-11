package com.student.Compass_Abroad
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemBannerBinding
import com.student.Compass_Abroad.databinding.ItemBannerOnboradingBinding

class OnBoardingBannerAdapter(
    private val items: List<BannerItem>
) : RecyclerView.Adapter<OnBoardingBannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(val binding: ItemBannerOnboradingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerOnboradingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = items[position]
        holder.binding.imgBanner.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}
