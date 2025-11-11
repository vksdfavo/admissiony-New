package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ItemCustomPopupCountrySelectorBinding

class AdapterFilterEnglishLevelSelector(val attendanceList: List<com.student.Compass_Abroad.modal.getProgramFilters.EnglishLevel>,
                                        private val onItemClick: (com.student.Compass_Abroad.modal.getProgramFilters.EnglishLevel) -> Unit
) : RecyclerView.Adapter<AdapterFilterEnglishLevelSelector.AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.bind(attendance)
    }

    override fun getItemCount(): Int = attendanceList.size

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAttendance: TextView = itemView.findViewById(R.id.tvAttendance)

        fun bind(attendance: com.student.Compass_Abroad.modal.getProgramFilters.EnglishLevel) {
            tvAttendance.text = attendance.label
            itemView.setOnClickListener {
                onItemClick(attendance) // Pass the selected attendance to the callback
            }
        }
    }
}