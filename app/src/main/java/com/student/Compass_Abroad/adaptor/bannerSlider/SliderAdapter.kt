package com.student.Compass_Abroad.adaptor.bannerSlider


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App.Companion.context
import com.student.Compass_Abroad.databinding.SlideItemContainerBinding


class SliderAdapter(private val sliderItems: List<String>, private val viewPager2: ViewPager2) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SlideItemContainerBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        var image=sliderItems[position]
        holder.setImage(image)
        Runnable {
            if (viewPager2.currentItem == sliderItems.size - 1) {
                viewPager2.currentItem = 0
            } else {
                viewPager2.currentItem = viewPager2.currentItem + 1
            }
        }

    }

    override fun getItemCount(): Int {

        return sliderItems.size
    }

    inner class SliderViewHolder(private val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setImage(sliderItem: String) {
            Glide.with(context)
                .load(sliderItem)
                .into(binding.imageSlide)
        }
    }
}
