package com.student.Compass_Abroad.modal.findAmbassadorModal

data class ProgramData(
    val discipline_data: DisciplineData,
    val id: Int,
    val identifier: String,
    val institution_data: InstitutionDataX,
    val name: String,
    val study_level_data: StudyLevelData
)