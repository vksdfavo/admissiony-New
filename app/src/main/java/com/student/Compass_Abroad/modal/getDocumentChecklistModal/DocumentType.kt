package  com.student.Compass_Abroad.modal.getDocumentChecklistModal

data class DocumentType(
    val id: Int,
    val identifier: String,
    val lead_documents: List<LeadDocument>,
    val module: String,
    val name: String
)