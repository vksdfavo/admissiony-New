package com.student.Compass_Abroad

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.databinding.ItemTimeBinding
import com.student.Compass_Abroad.modal.getStaffSlots.Slot
import java.text.SimpleDateFormat
import java.util.Locale

class TimeSlotAdapter(
    private val slots: List<Slot>,
    private val onSelectionChanged: (Slot?) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.SlotViewHolder>() {

    private var selectedPosition: Int = -1

    inner class SlotViewHolder(val binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Slot, position: Int) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                val startLabel = item.start_time?.let { time ->
                    inputFormat.parse(time)?.let { outputFormat.format(it) }
                } ?: ""

                val endLabel = item.end_time?.let { time ->
                    inputFormat.parse(time)?.let { outputFormat.format(it) }
                } ?: ""

                binding.textTimeSlot.text = "$startLabel - $endLabel"
            } catch (e: Exception) {
                e.printStackTrace()
                binding.textTimeSlot.text = "${item.start_time} - ${item.end_time}"
            }


//
//            if (item.is_booked == 1) {
//                binding.cardTimeSlot.setCardBackgroundColor(Color.parseColor("#E57373"))
//                binding.cardTimeSlot.setOnClickListener {
//                    Toast.makeText(binding.root.context, "This slot is already booked", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//
//            if (item.is_available == 0) {
//                binding.cardTimeSlot.setCardBackgroundColor(Color.LTGRAY)
//                binding.cardTimeSlot.setOnClickListener {
//                    Toast.makeText(binding.root.context, "This slot is not available", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//
//            if (selectedPosition == position) {
//                binding.cardTimeSlot.setCardBackgroundColor(Color.parseColor("#FF9800")) // orange selected
//            } else {
//                binding.cardTimeSlot.setCardBackgroundColor(Color.WHITE) // default white
//            }

            binding.cardTimeSlot.setOnClickListener {
                // Toggle selection
                selectedPosition = if (selectedPosition == position) -1 else position
                notifyDataSetChanged()

                val selectedSlot = if (selectedPosition != -1) slots[selectedPosition] else null
                onSelectionChanged(selectedSlot)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val binding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        holder.bind(slots[position], position)
    }

    override fun getItemCount(): Int = slots.size

    fun getSelectedSlot(): Slot? {
        return if (selectedPosition != -1) slots[selectedPosition] else null
    }

    fun clearSelection() {
        selectedPosition = -1
        notifyDataSetChanged()
    }
}
