package com.student.Compass_Abroad.modal.getPaymentApplication

data class PaymentGatewayInfo(
    val app_gateway_identifier: String,
    val default_currency: String,
    val gateway_identifier: String,
    val gateway_content_info: String,
    val name: String,
    val type: String,
    val url: String
)