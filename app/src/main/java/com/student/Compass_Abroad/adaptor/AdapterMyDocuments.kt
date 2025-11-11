package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemmydocumentsBinding
import com.student.Compass_Abroad.modal.getLeadsDocuments.Data

class AdapterMyDocuments(private val context: Context,
var recordInfoList: MutableList<Data>,

) : RecyclerView.Adapter<AdapterMyDocuments.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemmydocumentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var recordInfo=recordInfoList[position]
        holder.bind(recordInfo)
    }

    override fun getItemCount(): Int {
        return recordInfoList.size
    }

    inner class ViewHolder(private val binding: ItemmydocumentsBinding) : RecyclerView.ViewHolder(binding.root) {


        private lateinit var adapterLeadInnerAdaptor:MyDocumentInner
        fun bind(recordInfo: Data) {

            binding.tvAttachments.setText(recordInfo.name,)
            adapterLeadInnerAdaptor = MyDocumentInner(context,recordInfoList.get(position).files)
            binding.rvAttachments.layoutManager = LinearLayoutManager(context)
            binding.rvAttachments.adapter = adapterLeadInnerAdaptor
        }
    }
}