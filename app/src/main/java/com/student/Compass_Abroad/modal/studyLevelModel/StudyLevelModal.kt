package com.student.Compass_Abroad.modal.studyLevelModel

data class StudyLevelModal(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    var success: Boolean = false
)