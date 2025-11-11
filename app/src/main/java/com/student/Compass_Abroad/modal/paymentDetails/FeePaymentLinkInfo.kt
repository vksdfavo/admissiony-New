package com.student.Compass_Abroad.modal.paymentDetails

data class FeePaymentLinkInfo(
    val created_at: String,
    val created_by_info: CreatedByInfo,
    val created_by_role_info: CreatedByRoleInfo,
    val identifier: String,
    val module_info: ModuleInfo,
    val payment_gateway_info: PaymentGatewayInfo,
    val payment_info: PaymentInfo,
    val to_be_paid_amount: Int,
    val to_be_paid_currency: String,
    val updated_at: String
)