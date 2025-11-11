package com.student.Compass_Abroad.modal.getPaymentApplicationPay

data class GatewayInfo(
    val content: Content,
    val gateway_name: String,
    val order_info: OrderInfo,
    val payment_info: PaymentInfo,
    val theme: Theme
)