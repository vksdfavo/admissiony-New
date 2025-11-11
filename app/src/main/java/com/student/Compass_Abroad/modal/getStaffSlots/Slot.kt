package com.student.Compass_Abroad.modal.getStaffSlots

data class Slot(
    val date: String,
    val end_time: String,
    val is_booked: Int,
    val is_available: Int,
    val start_time: String
)