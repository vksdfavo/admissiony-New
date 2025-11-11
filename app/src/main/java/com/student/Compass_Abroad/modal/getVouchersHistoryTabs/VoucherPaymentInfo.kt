package com.student.Compass_Abroad.modal.getVouchersHistoryTabs

data class VoucherPaymentInfo(
    val hasPayment: Int,
    val id: Int,
    val payment_link_id: Int,
    val quantity: Int,
    val relation_identifier: String,
    val status: String
)