package com.student.Compass_Abroad.modal.paymentDetails

data class PaymentInfo(
    val created_at: String,
    val id: Int,
    val payment_identifier: String,
    val payment_type_id: Int,
    val payment_type_info: PaymentTypeInfo,
    val status: String,
    val to_be_paid_amount: Int,
    val to_be_paid_currency: String,
    val updated_at: String
)