package com.student.Compass_Abroad.modal.getProgramDetails

data class ProgramInfo(
    val application_fee: Int,
    val campus: Campus,
    val campus_id: Int,
    val id: Int,
    val identifier: String,
    val program: Program,
    val program_code: Any,
    val tuition_fee: Int
)