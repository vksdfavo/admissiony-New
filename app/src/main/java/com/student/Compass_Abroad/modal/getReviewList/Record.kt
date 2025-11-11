package com.student.Compass_Abroad.modal.getReviewList

data class Record(
    val content: String,
    val content_key: String,
    val created_at: String,
    val identifier: String,
    val is_helpful: Int,
    val is_my_review: Int,
    val is_not_helpful: Int,
    val module_type: String,
    val rating: Int,
    val review_by_user_info: ReviewByUserInfo,
    val review_by_user_role_info: ReviewByUserRoleInfo,
    val reviewed_user_info: ReviewedUserInfo,
    val status: String,
    val updated_at: String
)