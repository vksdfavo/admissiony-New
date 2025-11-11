package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.IteminnerdocumentsBinding
import com.student.Compass_Abroad.fragments.home.ViewDocFragment
import com.student.Compass_Abroad.fragments.home.WebViewFragment
import com.student.Compass_Abroad.modal.getApplicationDocuments.File
import androidx.navigation.findNavController
import com.student.Compass_Abroad.Utils.CommonUtils

class DocumentInnerAdaptor(var context: Context, var applicationDocumentList: List<File>):RecyclerView.Adapter<DocumentInnerAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            IteminnerdocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val attachment = applicationDocumentList[position]

            holder.bind(context, attachment)

    }

    override fun getItemCount(): Int {
           return applicationDocumentList.size
    }

    class MyViewHolder(
// Use the generated ViewBinding class
        var binding: IteminnerdocumentsBinding,


        ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(context: Context, file: File) {
            binding.textViewFileName.text = "${file.filealias}"

            val thumbnailUrl = file.thumb_info?.view_page
            if (!thumbnailUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.z_pdf) // while loading
                    .error(R.drawable.z_pdf)       // if load fails
                    .into(binding.imageViewPdf)
            } else {
                binding.imageViewPdf.setImageResource(R.drawable.z_pdf)
            }

            binding.textViewUploadDate.text =
                "Updated on ${CommonUtils.convertUTCToLocal1(file.created_at)}"


            binding.ivViewDocument.setOnClickListener {v: View ->
                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                    ViewDocFragment.data=file.view_page
                    ViewDocFragment.extension=file.file_extension
                    v.findNavController().navigate(R.id.viewDocFragment2)

                } else {

                    ViewDocFragment.data=file.view_page
                    ViewDocFragment.extension=file.file_extension
                    v.findNavController().navigate(R.id.viewDocFragment)
                }


            }

            binding.ivDownload.setOnClickListener {v: View ->

                if (App.sharedPre?.getString(AppConstants.SCOUtLOGIN, "") == "true") {

                    WebViewFragment.data=file.view_page
                    WebViewFragment.extension=file.file_extension
                    v.findNavController().navigate(R.id.webViewFragment3)

                } else {

                    WebViewFragment.data=file.view_page
                    WebViewFragment.extension=file.file_extension
                    v.findNavController().navigate(R.id.webViewFragment)
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