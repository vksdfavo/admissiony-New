package com.student.Compass_Abroad.modal.ambassadorAddConversation

data class Data(
    val ambassador_id: Int,
    val ambassadors_users_id: Int,
    val `by`: String,
    val category: String,
    val content: String,
    val created_at: String,
    val from_role_id: Int,
    val from_user_id: Int,
    val group: String,
    val id: Int,
    val identifier: String,
    val published_at: Any,
    val status: String,
    val title: Any,
    val to_role_id: Int,
    val to_user_id: Int,
    val updated_at: String
)