package com.student.Compass_Abroad.adaptor

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.student.Compass_Abroad.databinding.OfferandupdatesBinding
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.Record

class AdapterOffersandUpdate(
    private val requireActivity: FragmentActivity,
    private val items1: List<Record>,
    private val viewPager: ViewPager2,
    var  listener:OnClickData
) : RecyclerView.Adapter<AdapterOffersandUpdate.SliderViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    interface OnClickData{

        fun onClickData(position: Int, record: Record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OfferandupdatesBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val record = items1[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return items1.size
    }

    inner class SliderViewHolder(private val binding: OfferandupdatesBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(record: Record) {
            // Set data to views
            binding.tvtitle.text = record.title?.takeIf { !it.isNullOrEmpty() } ?: ""

            // Check if content is null or empty and set an empty string if it is
            binding.tvdetails.text = record.content?.takeIf { !it.isNullOrEmpty() } ?: ""

            binding.root.setOnClickListener {
                listener.onClickData(adapterPosition,record)
            }
        }
    }

    fun startAutoScroll() {
        runnable = Runnable {
            val currentItem = viewPager.currentItem
            viewPager.currentItem = (currentItem + 1) % items1.size
            handler.postDelayed(runnable!!, 2000)
        }
        handler.postDelayed(runnable!!, 2000)
    }

    fun stopAutoScroll() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
    }
}
