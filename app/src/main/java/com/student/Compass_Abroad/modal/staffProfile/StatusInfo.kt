package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class StatusInfo(
    val category: String,
    val description: String,
    val id: String,
    val statusCode: Int,
    val statusMessage: String
) : Serializable