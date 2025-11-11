package com.student.Compass_Abroad.modal.getLeadShorlistedProgram

data class Program(
    val attendance_on: String,
    val discipline: Discipline,
    val duration: Int,
    val identifier: Any,
    val institution: Institution,
    val intakes: List<Intake>,
    val is_fulltime: String,
    val is_visible: String,
    val min_requirement: String,
    val name: String,
    val studylevel: Studylevel,
    val testscores: List<Any>,
    val unique_string: String,
    val url: String
)