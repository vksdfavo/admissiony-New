package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.modal.applicationProgramDetails.ProgramChecklistInfo

class ChecklistAdapter(var data: ArrayList<ProgramChecklistInfo>) :
    RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_program_checklist, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val checklist11 = data[position]
       val checklistItem = data[position].items


        holder.label.text= checklist11.name


        val contentAdapter = ChecklistContentAdapter(checklistItem ?: emptyList())
        holder.recyclerViewContent.adapter = contentAdapter
        holder.recyclerViewContent.layoutManager = LinearLayoutManager(holder.itemView.context)
    }

    override fun getItemCount() = data.size

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recyclerViewContent: RecyclerView = itemView.findViewById(R.id.rv)
        val label: TextView = itemView.findViewById(R.id.tv_Header)
    }
}