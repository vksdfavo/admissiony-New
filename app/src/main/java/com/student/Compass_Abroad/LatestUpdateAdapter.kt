package com.student.Compass_Abroad

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.LatestUpdatesLayoutBinding
import com.student.Compass_Abroad.modal.getTestimonials.Row

class  LatestUpdateAdapter(
    private val destinationList: List<Row>,
    private val onItemClick: ((Row) -> Unit)? = null
) : RecyclerView.Adapter<LatestUpdateAdapter.ViewHolder>() {

    class ViewHolder(val binding: LatestUpdatesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LatestUpdatesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = destinationList[position]
        val url = item.media_url ?: ""
        holder.binding.btnTestimonials.text = item.description ?: ""
        holder.binding.tvTitle.text = item.title ?: ""
        holder.binding.date.text = item.created_at?.take(10) ?: ""

        when (item.media_type) {

            "video" -> {
                holder.binding.player.visibility = View.VISIBLE

                if (url.contains("youtube.com") || url.contains("youtu.be")) {

                    val videoId = extractYouTubeId(url)
                    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                    Glide.with(holder.itemView.context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.test_banner)
                        .error(R.drawable.test_banner)
                        .into(holder.binding.iv)

                } else {
                    val thumb = getVideoFrame(url)

                    if (thumb != null) {
                        Glide.with(holder.itemView.context)
                            .load(thumb)
                            .placeholder(R.drawable.test_banner)
                            .error(R.drawable.test_banner)
                            .into(holder.binding.iv)
                    } else {
                        holder.binding.iv.setImageResource(R.drawable.test_banner)
                    }
                }
            }

            "image" -> {
                holder.binding.player.visibility = View.GONE
                Glide.with(holder.itemView.context)
                    .load(url)
                    .placeholder(R.drawable.test_banner)
                    .error(R.drawable.test_banner)
                    .into(holder.binding.iv)
            }

            else -> {
                holder.binding.player.visibility = View.GONE
                holder.binding.iv.setImageResource(R.drawable.test_banner)
            }
        }

        holder.binding.player.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = destinationList.size

    private fun extractYouTubeId(url: String): String {
        return when {
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
            url.contains("embed/") -> url.substringAfter("embed/").substringBefore("?")
            else -> ""
        }
    }

    @Suppress("DEPRECATION")
    private fun getVideoFrame(videoUrl: String): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoUrl, HashMap())
            val bitmap = retriever.getFrameAtTime(1_000_000) // 1 sec frame
            retriever.release()
            bitmap
        } catch (e: Exception) {
            null
        }
    }
}