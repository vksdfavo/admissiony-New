package com.student.Compass_Abroad.modal.generatingPaymentLinkforApplication

data class FeePaymentInfo(
    val created_at: String,
    val created_by_info: CreatedByInfo,
    val created_by_role_info: CreatedByRoleInfo,
    val identifier: String,
    val payment_gateway_info: PaymentGatewayInfo,
    val payment_info: PaymentInfo,
    val payment_link_currency: String,
    val payment_link_price: Int,
    val updated_at: String
)