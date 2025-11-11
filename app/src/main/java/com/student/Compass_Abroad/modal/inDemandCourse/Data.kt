package com.student.Compass_Abroad.modal.inDemandCourse

data class Data(
    val campus_id: Int,
    val campus_identifier: String,
    val campus_name: String,
    val country_id: Int,
    val country_logo: String,
    val institution_id: Int,
    val institution_logo: String,
    val institution_name: String,
    val is_shortlisted: Int,
    val name: String,
    val program_campus_identifier: String,
    val program_id: Int,
    val program_identifier: String,
    val program_name: String,
    val short_name: String,
    val tuition_fee: Int,
    val tuition_fee_usd: Double,
    val tution_fee_curr: String
)