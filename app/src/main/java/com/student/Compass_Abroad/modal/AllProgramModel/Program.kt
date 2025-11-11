package com.student.Compass_Abroad.modal.AllProgramModel

data class Program(
    val attendance_on: String,
    val discipline: Discipline,
    val duration: String?,        // Only applicable for higher education programs
    val duration_type: String?,

    val institution_id: Int,
    val identifier: String,
    val institution: Institution,
    val intakes: List<Intake>,
    val is_fulltime: String,
    val is_visible: String,
    val id: Int,
    val additional_items: AdditionalItems?,
    val min_requirement: Any,
    val name: String,
    val tags: List<Tags>,
    val studylevel: Studylevel,
    val testscores: List<Testscore>,
    val unique_string: String,
    val url: String
)