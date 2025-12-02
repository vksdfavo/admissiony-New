package com.student.Compass_Abroad.adaptor.bannerSlider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.Utils.App.Companion.context
import com.student.Compass_Abroad.databinding.SlideItemContainerBinding
import com.student.Compass_Abroad.modal.getBannerModel.FileInfo
import com.student.Compass_Abroad.modal.getBannerModel.Record

class SliderAdapter(
    private val sliderItems: List<Record>,
    private val viewPager2: ViewPager2,
    private val listener: OnSliderClickListener
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {


    interface OnSliderClickListener {
        fun onSliderClick(item: Record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SlideItemContainerBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = sliderItems[position]
        holder.bind(item.fileInfo)

        holder.itemView.setOnClickListener {
            listener.onSliderClick(item)
        }
    }

    override fun getItemCount(): Int = sliderItems.size

    class SliderViewHolder(private val binding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FileInfo) {

            Glide.with(context)
                .load(item.view_page)
                .into(binding.imageSlide)

            // If you want description text in future:
            // binding.descriptionText.text = item.description
        }
    }
}
