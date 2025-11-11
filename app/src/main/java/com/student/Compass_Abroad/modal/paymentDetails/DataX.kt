package com.student.Compass_Abroad.modal.paymentDetails

data class DataX(
    val country_code: String,
    val first_name: String,
    val id: Int,
    val identifier: String,
    val institution_info: List<InstitutionInfo>,
    val last_name: String,
    val lead_id: Int,
    val lead_identifier: String,
    val mobile: String,
    val program_info: List<ProgramInfo>
)