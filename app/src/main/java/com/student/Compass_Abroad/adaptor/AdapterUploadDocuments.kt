package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemUploadDcumentsBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList

class AdapterUploadDocuments(var context: Context,var  files: ArrayList<File>):RecyclerView.Adapter<AdapterUploadDocuments.MyViewHolder>(){
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding = ItemUploadDcumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file,context)
    }

    override fun getItemCount(): Int {
        return  files.size
    }
    class MyViewHolder(// Use the generated ViewBinding class
        var binding: ItemUploadDcumentsBinding,


        ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(file: File, context: Context) {

            binding.fileNameTextView.text = file.name
            binding.imType.post {
                loadImageFromFile(file,context)
            }
            binding.deleteButton.setOnClickListener { v: View ->



            }
        }

        private fun loadImageFromFile(file: File, context: Context) {
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(file.absolutePath, options)
                val imageWidth = options.outWidth
                val imageHeight = options.outHeight

                // Calculate inSampleSize based on the required dimensions
                val reqWidth = binding.imType.width
                val reqHeight = binding.imType.height
                val inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

                options.inJustDecodeBounds = false
                options.inSampleSize = inSampleSize

                var bitmap: Bitmap? = null
                if (file.extension.equals("pdf", ignoreCase = true)) {
                    // Load PDF icon or thumbnail
                    binding.imType.setImageResource(R.drawable.z_pdf)
                } else {
                    bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

                    // Load image from file
                    if (bitmap != null) {
                        binding.imType.setImageBitmap(bitmap)
                        // Force layout invalidation to ensure the image is displayed
                        binding.imType.invalidate()
                    } else {

                        Toast.makeText(context,file.absolutePath,Toast.LENGTH_LONG).show()
                        Log.e("AdapterUploadDocuments", "File not found: ${file.absolutePath}")
                    }
                }
            } catch (e: FileNotFoundException) {
                Log.e("AdapterUploadDocuments", "File not found: ${file.absolutePath}", e)
            } catch (e: IOException) {
                Log.e("AdapterUploadDocuments", "Error reading file: ${file.absolutePath}", e)
            } catch (e: Exception) {
                Log.e("AdapterUploadDocuments", "Exception loading image from file: ${file.absolutePath}", e)
            }
        }



        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                // Calculate ratios of height and width to requested height and width
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            return inSampleSize
        }
    }



}