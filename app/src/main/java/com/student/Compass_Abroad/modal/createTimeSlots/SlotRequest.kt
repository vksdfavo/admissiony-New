package com.student.Compass_Abroad.modal.createTimeSlots

data class SlotRequest(
    val date: String,
    val slots: List<SlotData>
)

data class SlotData(
    val start_time: String,
    val end_time: String
)
