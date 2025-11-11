package com.student.Compass_Abroad.modal.applicationProgramDetails

data class ProgramInfo(
    val application_fee: Int,
    val campus: Campus,
    val campus_id: Int,
    val id: Int,
    val identifier: String,
    val program: Program,
    val program_code: String,
    val tuition_fee: Int
)