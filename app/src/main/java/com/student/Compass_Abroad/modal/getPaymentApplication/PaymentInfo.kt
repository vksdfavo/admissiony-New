package com.student.Compass_Abroad.modal.getPaymentApplication

data class PaymentInfo(
    val created_at: String,
    val currency: String,
    val id: Int,
    val payment_identifier: String,
    val payment_type_id: Int,
    val payment_type_info: PaymentTypeInfo,
    val price: Int,
    val status: String,
    val updated_at: String
)