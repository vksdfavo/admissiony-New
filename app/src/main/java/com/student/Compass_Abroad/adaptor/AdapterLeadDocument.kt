package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.student.Compass_Abroad.databinding.ItemLeadDocumentBinding
import com.student.Compass_Abroad.modal.getLeadsDocuments.Data


class AdapterLeadDocument(
    private val context: Context,
    var recordInfoList: MutableList<Data>,

    ) : RecyclerView.Adapter<AdapterLeadDocument.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeadDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var recordInfo=recordInfoList[position]
        holder.bind(recordInfo)
    }

    override fun getItemCount(): Int {
        return recordInfoList.size
    }

    inner class ViewHolder(private val binding: ItemLeadDocumentBinding) : RecyclerView.ViewHolder(binding.root) {


        private lateinit var adapterLeadInnerAdaptor:DocumentLeadInnerAdaptor
        fun bind(recordInfo: Data) {

            binding.tvAttachments.setText(recordInfo.name,)
            adapterLeadInnerAdaptor = DocumentLeadInnerAdaptor(context,recordInfoList.get(position).files)
            binding.rvAttachments.layoutManager = LinearLayoutManager(context)
            binding.rvAttachments.adapter = adapterLeadInnerAdaptor
        }
    }
}