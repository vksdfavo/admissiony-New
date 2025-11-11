package com.student.Compass_Abroad.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.StudyLevelLayoutBinding
import com.student.Compass_Abroad.modal.studyLevelModel.Data

class StudyLevelAdapter(
    private val requireActivity: FragmentActivity,
    private val arrayList1: MutableList<Data>,
    private val selector: SelectStudyLevel
) : RecyclerView.Adapter<StudyLevelAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1
    private var preSelectedLabel: String? = null

    interface SelectStudyLevel {
        fun onSelect(data: Data?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudyLevelLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList1[position]

        holder.binding.studyLevel.text = currentItem.label
        holder.binding.studyLevel.isChecked = (position == selectedPosition)

        holder.binding.studyLevel.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            selector.onSelect(currentItem, position)
        }
    }

    override fun getItemCount(): Int = arrayList1.size

    fun getSelectedItem(): Data? {
        return if (selectedPosition != -1) arrayList1[selectedPosition] else null
    }

    fun setPreSelectedLabel(label: String?) {
        this.preSelectedLabel = label
        this.selectedPosition = arrayList1.indexOfFirst { it.label == label }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: StudyLevelLayoutBinding) : RecyclerView.ViewHolder(binding.root)


}
