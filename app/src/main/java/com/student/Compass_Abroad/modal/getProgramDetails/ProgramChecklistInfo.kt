package com.student.Compass_Abroad.modal.getProgramDetails

data class ProgramChecklistInfo(
    val id: Int,
    val identifier: String,
    val items: List<Item>,
    val name: String
)