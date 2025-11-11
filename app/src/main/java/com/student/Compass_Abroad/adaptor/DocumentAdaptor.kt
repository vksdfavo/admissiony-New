package com.student.Compass_Abroad.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemapplicationdocumentsBinding
import com.student.Compass_Abroad.modal.getApplicationDocuments.RecordsInfo

class DocumentAdaptor(
    private val context: Context,
    private val recordInfoList: List<RecordsInfo>
) : RecyclerView.Adapter<DocumentAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemapplicationdocumentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recordInfo = recordInfoList[position]
        holder.bind(recordInfo)
    }

    override fun getItemCount(): Int {
        return recordInfoList.size
    }

    inner class MyViewHolder(private val binding: ItemapplicationdocumentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var adapterInnerAdaptor: DocumentInnerAdaptor

        fun bind(recordInfo: RecordsInfo) {
            binding.tvAttachments.text = recordInfo.name
            adapterInnerAdaptor = DocumentInnerAdaptor(context, recordInfoList.get(position).files)
            binding.rvAttachments.layoutManager = LinearLayoutManager(context)
            binding.rvAttachments.adapter = adapterInnerAdaptor

        }
    }
}