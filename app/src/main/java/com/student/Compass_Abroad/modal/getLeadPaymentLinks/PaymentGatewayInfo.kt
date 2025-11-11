package com.student.Compass_Abroad.modal.getLeadPaymentLinks

data class PaymentGatewayInfo(
    val app_gateway_identifier: Any,
    val default_currency: String,
    val gateway_identifier: String,
    val name: String,
    val type: String,
    val url: Any
)