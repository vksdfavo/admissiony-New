package com.student.Compass_Abroad.modal.staffProfile

import java.io.Serializable

data class GroupInfo(
    val alias: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val type: String
) : Serializable