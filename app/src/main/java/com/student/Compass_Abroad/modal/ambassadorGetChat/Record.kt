package com.student.Compass_Abroad.modal.ambassadorGetChat

data class Record(
    val ambassador_id: Int,
    val ambassadors_users_id: Int,
    val `by`: String,
    val category: String,
    val content: String,
    val created_at: String,
    val deleted_at: Any,
    val from_role_id: Int,
    val from_user_id: Int,
    val group: String,
    val id: Int,
    val identifier: String,
    val published_at: String,
    val recieverInfo: RecieverInfo,
    val recieverRoleInfo: RecieverRoleInfo,
    val senderInfo: SenderInfo,
    val senderRoleInfo: SenderRoleInfo,
    val status: String,
    val title: Any,
    val to_role_id: Int,
    val to_user_id: Int,
    val updated_at: String
)