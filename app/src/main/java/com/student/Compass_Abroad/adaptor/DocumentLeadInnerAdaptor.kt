package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemleadinnerdocumentsBinding
import com.student.Compass_Abroad.fragments.home.WebViewFragment

class DocumentLeadInnerAdaptor(
    var context: Context,
    var leadDocumentList: List<com.student.Compass_Abroad.modal.getLeadsDocuments.File>
) : RecyclerView.Adapter<DocumentLeadInnerAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemleadinnerdocumentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val attachment = leadDocumentList[position]

        holder.bind(context, attachment)

    }

    override fun getItemCount(): Int {
        return leadDocumentList.size
    }

    class MyViewHolder(
// Use the generated ViewBinding class
        var binding: ItemleadinnerdocumentsBinding,


        ) : RecyclerView.ViewHolder(

        binding.getRoot()


    ) {
        fun bind(context: Context, file: com.student.Compass_Abroad.modal.getLeadsDocuments.File) {
            binding.textViewFileName.text = "${file.filename}.${file.file_extension}"

            Glide.with(context)
                .load(file.thumb_info.view_page)
                .into(binding.imageViewPdf)

            binding.tvViewDocument.setOnClickListener { v: View ->
                WebViewFragment.data = file.view_page
                WebViewFragment.extension = file.file_extension
                Navigation.findNavController(v).navigate(R.id.viewDocFragment)

            }
            binding.tvDownload.setOnClickListener { v: View ->

                WebViewFragment.data = file.view_page
                WebViewFragment.extension = file.file_extension
                Navigation.findNavController(v).navigate(R.id.webViewFragment)

            }
        }


    }


}