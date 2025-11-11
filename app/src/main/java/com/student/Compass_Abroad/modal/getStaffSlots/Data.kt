package com.student.Compass_Abroad.modal.getStaffSlots

data class Data(
    val slot_date: String,
    val slots: List<Slot>,
    val time_zone: String,
    val user_id: String
)