package com.student.Compass_Abroad.modal.createTimeSlots

data class SlotResponse(
    val statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val data: SlotDataResponse?? = null,
    val message: String? = null,
    val success: Boolean= false
)

data class StatusInfo(
    val id: String? = null,
    val statusCode: Int? = null,
    val statusMessage: String? = null,
    val description: String? = null,
    val category: String? = null,
)

data class SlotDataResponse(
    val inserted_slots: List<InsertedSlot>? = null,
    val rejected_slots: List<RejectedSlot>? = null,
)

data class InsertedSlot(
    val start_time: String? = null,
    val end_time: String? = null,
    val date: String? = null,
    val identifier: String? = null,
    val created_by_user: Int? = null,
)

data class RejectedSlot(
    val start_time: String? = null,
    val end_time: String? = null,
    val reason: String? = null
)
