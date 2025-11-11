package com.student.Compass_Abroad.modal.getTestimonials

data class Row(
    val created_at: String,
    val created_by_id: Int,
    val created_by_role_id: Int,
    val description: String,
    val identifier: String,
    val is_visible: Boolean,
    val media_type: String,
    val media_url: String,
    val title: String,
    val updated_at: String
)