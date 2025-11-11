package com.student.Compass_Abroad.modal.getChatResponse

data class Record(
    val category: String?,
    val content: String?,
    val created_at: String?,
    val group: String?,
    val identifier: String?,
    val identityInfo: IdentityInfo?,
    val is_sent: Int?,
    val published_at: String?,
    val sent_by: String?,
    val status: String?,
    val title: String?,
    val type: String?,
    val updated_at: String?,
    val userInfo: UserInfo?,
    val has_attachments: String?
)

