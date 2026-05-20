package com.student.Compass_Abroad.modal.getStudentPref

data class PreferencesInfo(
    val discipline: Any? = null,
    val disciplines: Any? = null,           // ✅ Any? handles both String and List
    val preferred_study_level: Any? = null,
    val destination_country: Any? = null,
)