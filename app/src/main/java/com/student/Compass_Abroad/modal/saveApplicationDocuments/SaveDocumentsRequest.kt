package com.student.Compass_Abroad.modal.saveApplicationDocuments

data class SaveDocumentsRequest(
    var files: List<FileData>,
    var document_type_identifier: String
)