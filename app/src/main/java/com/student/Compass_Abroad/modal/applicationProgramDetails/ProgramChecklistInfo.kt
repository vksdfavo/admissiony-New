package com.student.Compass_Abroad.modal.applicationProgramDetails

data class ProgramChecklistInfo(
    val id: Int,
    val identifier: String,
    val items: List<Item>,
    val name: String
)