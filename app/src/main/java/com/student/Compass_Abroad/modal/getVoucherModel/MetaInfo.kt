package com.student.Compass_Abroad.modal.getVoucherModel

data class MetaInfo(
    val currentPageRecords: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val nextPage: Int,
    val page: Int,
    val pageEndOffset: Int,
    val pageStartOffset: Int,
    val perPage: Int,
    val previousPage: Int,
    val totalRecords: Int
)