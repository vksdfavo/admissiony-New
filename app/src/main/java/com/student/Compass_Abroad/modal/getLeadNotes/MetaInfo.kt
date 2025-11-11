package com.student.Compass_Abroad.modal.getLeadNotes

data class MetaInfo(
    val page: Int,
    val perPage: Int,
    val pageStartOffset: Int,
    val pageEndOffset: Int,
    val previousPage: Int,
    val nextPage: Int,
    val firstPage: Int,
    val lastPage: Int,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val currentPageRecords: Int,
    val totalRecords: Int
)