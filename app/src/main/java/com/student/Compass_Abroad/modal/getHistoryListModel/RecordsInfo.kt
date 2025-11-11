package com.student.Compass_Abroad.modal.getHistoryListModel

data class RecordsInfo(
    val created_at: String,
    val feePaymentLinkInfo: FeePaymentLinkInfo,
    val identifier: String,
    val paid_amount: Int,
    val paid_currency: String,
    val payment_link_id: Int,
    val reason: Any,
    val status: String,
    val transaction_by_info: TransactionByInfo,
    val transaction_by_role_info: TransactionByRoleInfo,
    val updated_at: String,
    val voucherLink: VoucherLink
)