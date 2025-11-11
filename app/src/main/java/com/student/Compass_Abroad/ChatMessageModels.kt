package com.student.Compass_Abroad

import com.google.gson.annotations.SerializedName

class ChatMessageModels {
    data class FileData(
        @SerializedName("file_identifier") val fileIdentifier: String,
        @SerializedName("file_thumb_identifier") val fileThumbIdentifier: String
    )

    data class ChatMessageRequest(
        val title: String?,
        val content: String?,
        val type: String?,
        val has_attachments: String?,
        val attachments: List<FileData>?
    )

}