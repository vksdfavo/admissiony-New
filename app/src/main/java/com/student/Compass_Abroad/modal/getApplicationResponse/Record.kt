package com.student.Compass_Abroad.modal.getApplicationResponse

import android.os.Parcel
import android.os.Parcelable

data class Record(
    val allProgramInfo: List<AllProgramInfo>,
    val applicationFeeInfo: List<ApplicationFeeInfo>,
    val applicationTimelineInfo: List<ApplicationTimelineInfo>,
    val id: Int,
    val destination_country_id: Int,
    val identifier: String,
    val intakeInfo: IntakeInfo,
    val latestInstitutionInfo: LatestInstitutionInfo,
    val leadInfo: LeadInfo,
    val statusInfo: StatusInfo
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }
}