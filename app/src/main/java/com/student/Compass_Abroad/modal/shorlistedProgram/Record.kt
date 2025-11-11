package com.student.Compass_Abroad.modal.shorlistedProgram

data class Record(
    val application_fee: Int,
    val campus: Campus,
    val identifier: String,
    var is_shortlisted: Int,
    val program: Program,
    val program_code: Any,
    val tuition_fee: Int
)