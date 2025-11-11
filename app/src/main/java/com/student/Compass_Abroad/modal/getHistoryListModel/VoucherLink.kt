package com.student.Compass_Abroad.modal.getHistoryListModel

data class VoucherLink(
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val payment_link_id: Int,
    val quantity: Int,
    val relation_identifier: String,
    val status: String,
    val updated_at: String,
    val voucher_id: Int
)