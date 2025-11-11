package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemUploadChatDcumentsBinding
import java.io.File
import java.util.ArrayList

class AdapterUploadDocumentsChats(
    private val context: Context,
    private var files: ArrayList<File>,
    private val recyclerView: RecyclerView,
    private val selectedImagesList: ArrayList<File>
) : RecyclerView.Adapter<AdapterUploadDocumentsChats.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUploadChatDcumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file, context, position)
    }

    override fun getItemCount(): Int = files.size

    inner class MyViewHolder(private val binding: ItemUploadChatDcumentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File, context: Context, position: Int) {
            binding.imType.post { loadImageFromFile(file, context) }

            binding.deleteButton.setOnClickListener {
                val removedFile = files[position]
                files.removeAt(position) // Remove from adapter list
                selectedImagesList.remove(removedFile) // Remove from main list
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, files.size)

                if (files.isEmpty()) {
                    recyclerView.visibility = View.GONE
                }
            }
        }

        private fun loadImageFromFile(file: File, context: Context) {
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(file.absolutePath, options)

                val reqWidth = binding.imType.width
                val reqHeight = binding.imType.height
                val inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

                options.inJustDecodeBounds = false
                options.inSampleSize = inSampleSize

                val bitmap: Bitmap? = if (file.extension.equals("pdf", ignoreCase = true)) {
                    binding.imType.setImageResource(R.drawable.z_pdf)
                    null
                } else {
                    BitmapFactory.decodeFile(file.absolutePath, options)
                }

                bitmap?.let {
                    binding.imType.setImageBitmap(it)
                    binding.imType.invalidate()
                } ?: Log.e("AdapterUploadDocuments", "File not found: ${file.absolutePath}")

            } catch (e: Exception) {
                Log.e("AdapterUploadDocuments", "Error loading image: ${file.absolutePath}", e)
            }
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            return inSampleSize
        }
    }
}
