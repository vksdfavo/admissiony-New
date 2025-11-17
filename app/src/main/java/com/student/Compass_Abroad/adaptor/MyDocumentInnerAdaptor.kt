package com.student.Compass_Abroad.adaptor

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.DownLoadDocActivity
import com.student.Compass_Abroad.activities.WebViewActivity
import com.student.Compass_Abroad.databinding.IteminnermydocumentsBinding
import java.util.Locale

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
            binding.textViewFileName.text = "${file.filealias}"

            val context = binding.root.context
            val fileExt = file.file_extension?.lowercase(Locale.ROOT)?.trim() ?: ""

            if (fileExt in listOf("jpg", "jpeg", "png")) {
                binding.imageViewPdf.setImageResource(R.drawable.image)
            } else {
                when (fileExt) {
                    "pdf" -> binding.imageViewPdf.setImageResource(R.drawable.z_pdf)
                    "doc", "docx" -> binding.imageViewPdf.setImageResource(R.drawable.docx)
                    "xls", "xlsx", "csv" -> binding.imageViewPdf.setImageResource(R.drawable.csv)
                    else -> binding.imageViewPdf.setImageResource(R.drawable.folder)
                }
            }




            binding.ivViewDocument.setOnClickListener { v: View ->
                val intent = Intent(context, WebViewActivity::class.java).apply {
                    putExtra("url", file.view_page)
                    putExtra("extension", file.file_extension)
                }
                context.startActivity(intent)

            }
            binding.ivDownload.setOnClickListener { v: View ->
                val context = v.context
                val url = file.view_page

                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "Invalid file URL", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                try {
                    val fileName = file.filealias ?: "downloaded_file.${file.file_extension}"

                    val request = DownloadManager.Request(Uri.parse(url)).apply {
                        setTitle(fileName)
                        setDescription("Downloading $fileName...")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                        )
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                        allowScanningByMediaScanner()
                    }

                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)

                    Toast.makeText(context, "Downloading $fileName", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

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