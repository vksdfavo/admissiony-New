package com.student.Compass_Abroad.modal.CreateApplication

data class ApplicationInfo(
    val allProgramInfo: List<AllProgramInfo>,
    val applicationTimelineInfo: List<ApplicationTimelineInfo>,
    val id: Int,
    val identifier: String,
    val intakeInfo: IntakeInfo,
    val latestInstitutionInfo: LatestInstitutionInfo,
    val leadInfo: LeadInfo,
    val statusInfo: StatusInfo
)