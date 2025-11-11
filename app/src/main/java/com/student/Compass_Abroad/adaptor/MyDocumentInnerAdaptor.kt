package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.activities.DownLoadDocActivity
import com.student.Compass_Abroad.activities.WebViewActivity
import com.student.Compass_Abroad.databinding.IteminnermydocumentsBinding

class MyDocumentInner(var context: Context, var myDocumentList: List<com.student.Compass_Abroad.modal.getLeadsDocuments.File>):
    RecyclerView.Adapter<MyDocumentInner.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            IteminnermydocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val attachment = myDocumentList[position]

        holder.bind(context, attachment)

    }

    override fun getItemCount(): Int {
        return myDocumentList.size
    }

    class MyViewHolder(

        var binding: IteminnermydocumentsBinding,

        ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(context: Context, file: com.student.Compass_Abroad.modal.getLeadsDocuments.File) {
            binding.textViewFileName.text = "${file.filename}.${file.file_extension}"

            Glide.with(context)
                .load(file.thumb_info.view_page)
                .into(binding.imageViewPdf)


                binding.tvViewDocument.setOnClickListener { v: View ->
                    val intent = Intent(v.context, WebViewActivity::class.java).apply {
                        putExtra("view_page", file.view_page)
                        putExtra("file_extension", file.file_extension)
                    }
                    v.context.startActivity(intent)

            }
            binding.tvDownload.setOnClickListener {v: View ->

                DownLoadDocActivity.data=file.view_page
                DownLoadDocActivity.extension=file.file_extension

                val intent = Intent(v.context, DownLoadDocActivity::class.java)
                v.context.startActivity(intent)

            }
        }
        private fun openUrl(url: String?, context: Context) {
            url?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }

}