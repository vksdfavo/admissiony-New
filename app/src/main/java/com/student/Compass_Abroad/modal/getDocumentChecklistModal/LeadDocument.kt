package  com.student.Compass_Abroad.modal.getDocumentChecklistModal

data class LeadDocument(
    val application_id: Int,
    val document_type_id: Int,
    val file_id: Int,
    val file_info: FileInfo,
    val id: Int,
    val lead_id: Int,
    val status: String
)