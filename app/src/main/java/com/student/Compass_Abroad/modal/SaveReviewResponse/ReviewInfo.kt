package com.student.Compass_Abroad.modal.SaveReviewResponse

data class ReviewInfo(
    val by_role_id: Int,
    val by_user_id: Int,
    val content: String,
    val content_key: String,
    val created_at: String,
    val device_id: Int,
    val id: Int,
    val identifier: String,
    val is_helpful: Int,
    val is_not_helpful: Int,
    val module_id: Int,
    val module_type: String,
    val rating: Int,
    val status: String,
    val to_user_id: Int,
    val updated_at: String
)