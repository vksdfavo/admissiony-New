package com.student.Compass_Abroad

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.databinding.TestimonialsLayoutsBinding
import com.student.Compass_Abroad.modal.getTestimonials.Row

class StudentTestimonialsAdapter(private val testimonialList: List<Row>,
    private val onItemClick: ((Row) -> Unit)? = null
) : RecyclerView.Adapter<StudentTestimonialsAdapter.ViewHolder>() {

    class ViewHolder(val binding: TestimonialsLayoutsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TestimonialsLayoutsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = testimonialList[position]
        val url = item.media_url ?: ""

        holder.binding.btnTestimonials.text = item.description ?: ""
        holder.binding.date.text = item.created_at?.take(10) ?: ""

        when (item.media_type) {

            // -------------------------------------------------------------
            // ✅ YOUTUBE VIDEO THUMBNAIL
            // -------------------------------------------------------------
            "video" -> {
                holder.binding.player.visibility = View.VISIBLE

                if (url.contains("youtu.be") || url.contains("youtube.com")) {

                    val videoId = extractYouTubeId(url)
                    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                    Glide.with(holder.itemView.context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.test_banner)
                        .error(R.drawable.test_banner)
                        .into(holder.binding.iv)

                } else {

                    // -------------------------------------------------------------
                    // ✅ OTHER VIDEO (MP4 / M3U8 / Other Media)
                    // -------------------------------------------------------------

                    val thumb = getVideoFrame(url)

                    if (thumb != null) {
                        Glide.with(holder.itemView.context)
                            .load(thumb)
                            .placeholder(R.drawable.test_banner)
                            .into(holder.binding.iv)
                    } else {
                        holder.binding.iv.setImageResource(R.drawable.test_banner)
                    }
                }
            }

            // -------------------------------------------------------------
            // ✅ IMAGE
            // -------------------------------------------------------------

            "image" -> {
                holder.binding.player.visibility = View.GONE
                Glide.with(holder.itemView.context)
                    .load(url)
                    .placeholder(R.drawable.test_banner)
                    .error(R.drawable.test_banner)
                    .into(holder.binding.iv)
            }

            else -> {
                // Unknown type → fallback
                holder.binding.player.visibility = View.GONE
                holder.binding.iv.setImageResource(R.drawable.test_banner)
            }
        }

        // -------------------------------------------------------------
        // CLICK EVENT
        // -------------------------------------------------------------
        holder.binding.player.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    // -------------------------------------------------------------
    // YOUTUBE ID extractor
    // -------------------------------------------------------------
    private fun extractYouTubeId(url: String): String {
        return when {
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
            url.contains("embed/") -> url.substringAfter("embed/").substringBefore("?")
            else -> ""
        }
    }

    // -------------------------------------------------------------
    // Extract frame from video (MP4, M3U8 etc.)
    // -------------------------------------------------------------
    @Suppress("DEPRECATION")
    private fun getVideoFrame(videoUrl: String): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoUrl, HashMap())
            val bitmap = retriever.getFrameAtTime(1_000_000) // 1 sec
            retriever.release()
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    override fun getItemCount(): Int = testimonialList.size
}
