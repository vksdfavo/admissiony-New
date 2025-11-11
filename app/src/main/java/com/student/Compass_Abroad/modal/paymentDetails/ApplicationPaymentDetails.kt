package com.student.Compass_Abroad.modal.paymentDetails

data class ApplicationPaymentDetails(
    val `data`: Data? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    val statusInfo: StatusInfo? = null,
    val success: Boolean = false
)