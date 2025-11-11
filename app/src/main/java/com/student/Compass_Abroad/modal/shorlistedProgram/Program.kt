package com.student.Compass_Abroad.modal.shorlistedProgram

data class Program(
    val attendance_on: String,
    val discipline: Discipline,
    val duration: Int,
    val identifier: String,
    val institution: Institution,
    val intakes: List<Intake>,
    val is_fulltime: String,
    val is_visible: String,
    val min_requirement: Any,
    val name: String,
    val studylevel: Studylevel,
    val testscores: List<Testscore>,
    val unique_string: String,
    val url: String
)