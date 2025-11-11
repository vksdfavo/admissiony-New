package com.student.Compass_Abroad.modal.intakeModel

data class IntakeModel(
    var `data`: List<Data>? = null,
    var message: String? = null,
    var statusCode: Int = 0,
    var statusInfo: StatusInfo? = null,
    val success: Boolean = false
)