package com.student.Compass_Abroad.adaptor

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.modal.applicationProgramDetails.Item

class ChecklistContentAdapter(var items: List<Item>) :
    RecyclerView.Adapter<ChecklistContentAdapter.ChecklistContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistContentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checklist_content, parent, false)
        return ChecklistContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistContentViewHolder, position: Int) {
       val data=items[position].name
        holder.tvLabel.text = data
        for(i in 0 until items.get(position).items.size ){
            val dataii=items.get(position).items.get(i).text
            holder.tvData.text = Html.fromHtml(dataii, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun getItemCount() = items.size

    inner class ChecklistContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      //  val rvSub: RecyclerView = itemView.findViewById(R.id.rvSub)
        val tvLabel: TextView = itemView.findViewById(R.id.tvstudylevel)
        val tvData: TextView = itemView.findViewById(R.id.tvData)
    }
}