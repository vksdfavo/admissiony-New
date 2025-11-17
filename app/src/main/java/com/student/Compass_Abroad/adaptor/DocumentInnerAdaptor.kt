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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.WebViewActivity
import com.student.Compass_Abroad.databinding.IteminnerdocumentsBinding
import com.student.Compass_Abroad.fragments.home.ViewDocFragment
import com.student.Compass_Abroad.fragments.home.WebViewFragment
import com.student.Compass_Abroad.modal.getApplicationDocuments.File
import java.util.Locale

class DocumentInnerAdaptor(
    private val context: Context,
    private val applicationDocumentList: List<File>
) : RecyclerView.Adapter<DocumentInnerAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            IteminnerdocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val attachment = applicationDocumentList[position]
        holder.bind(context, attachment)
    }

    override fun getItemCount(): Int = applicationDocumentList.size

    class MyViewHolder(
        private val binding: IteminnerdocumentsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, file: File) {
            binding.textViewFileName.text = "${file.filealias}"

            // --- Set correct icon based on file type ---

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

            // --- Open in-app viewer (no download) ---
            binding.ivViewDocument.setOnClickListener { v: View ->
                if (file.view_page.isNullOrEmpty()) {
                    Toast.makeText(context, "Invalid file URL", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val intent = Intent(context, WebViewActivity::class.java).apply {
                    putExtra("url", file.view_page)
                    putExtra("extension", file.file_extension)
                }

                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                    // Previously navigating to viewDocFragment2 — same logic but now activity
                    intent.putExtra("from", "scout")
                } else {
                    // Previously navigating to viewDocFragment — same logic but now activity
                    intent.putExtra("from", "normal")
                }

                context.startActivity(intent)
            }


            binding.ivDownload.setOnClickListener { v: View ->
                val context = v.context
                val url = file.view_page
                val extension = file.file_extension

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
                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(it))
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
}
