package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.student.Compass_Abroad.modal.getDocumentChecklistModal.LeadDocument
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R

import com.student.Compass_Abroad.databinding.ItemcheklistdocumentsBinding

import com.student.Compass_Abroad.modal.getDocumentChecklistModal.Data




class DocumentChecklistAdaptor(
    private val context: Context,
    private val applicationDocumentList: List<Data>,
    var data: String?
) : RecyclerView.Adapter<DocumentChecklistAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemcheklistdocumentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = applicationDocumentList[position]
        holder.bind(context, item.documentType.name, item.documentType.lead_documents,item,data)
    }

    override fun getItemCount(): Int = applicationDocumentList.size

    class MyViewHolder(private val binding: ItemcheklistdocumentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            docName: String,
            attachments: List<LeadDocument>,
            item: Data,
            data: String?
        ) {
            binding.textViewFileName.text = docName

            if (attachments.isNotEmpty()) {


                binding.tvViewDocument.visibility = View.GONE
                binding.tvPending.visibility = View.GONE



                binding.imageViewPdf.setImageResource(R.drawable.tick_svgrepo_com)
                binding.imageViewPdf.setColorFilter(ContextCompat.getColor(context, R.color.btnColorGreen))

                /*binding.tvViewDocument.setOnClickListener { v: View ->
                    EditDocumentChecklist.data = item
                    EditDocumentChecklist.identifier = data
                    Navigation.findNavController(v).navigate(R.id.editDocumentChecklist)
                }*/

            } else {
                // Hide View, show Pending
                binding.tvViewDocument.visibility = View.GONE
                binding.tvPending.visibility = View.VISIBLE

                // Info icon
                binding.imageViewPdf.setImageResource(R.drawable.ic_info2)
                binding.imageViewPdf.setColorFilter(ContextCompat.getColor(context, R.color.rippleColor))
            }

        }
    }
}

