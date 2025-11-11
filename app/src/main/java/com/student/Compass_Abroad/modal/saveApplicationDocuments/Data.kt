package com.student.Compass_Abroad.modal.saveApplicationDocuments

data class Data(
    val documentTypeInfo: DocumentTypeInfo,
    val filesInfo: List<FilesInfo>,
    val filesThumbInfo: List<FilesThumbInfo>,
    val leadDocumentInfo: List<LeadDocumentInfo>
)