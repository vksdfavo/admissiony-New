package com.student.Compass_Abroad.modal.applicationProgramDetails

data class Program(
    val attendance_on: String,
    val discipline: Discipline,
    val discipline_id: Int,
    val duration: Int,
    val id: Int,
    val identifier: String,
    val institution: Institution,
    val institution_id: Int,
    val is_fulltime: String,
    val is_visible: String,
    val min_requirement: String,
    val name: String,
    val study_level_id: Int,
    val studylevel: Studylevel,
    val testscores: List<Testscore>,
    val unique_string: String,
    val url: String
)