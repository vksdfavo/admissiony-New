package com.student.Compass_Abroad.modal.findAmbassadorModal

data class RecordsInfo(
    val agent_id: Int,
    val ambassadorTypeInfo: AmbassadorTypeInfo,
    val ambassador_type_id: Int,
    val bio: String,
    val city_of_origin: CityOfOrigin,
    val client_id: Int,
    val country_of_origin: CountryOfOrigin,
    val created_at: String,
    val current_year: String,
    val deleted_at: Any,
    val id: Int,
    val identifier: String,
    val incentiveInfo: IncentiveInfo,
    val institution_data: InstitutionData,
    val is_visible_in_widget: Int,
    val languages_spoken: List<LanguagesSpoken>,
    val program_data: ProgramData,
    val region: Any,
    val state_of_origin: StateOfOrigin,
    val updated_at: String,
    val userInfo: UserInfo,
    val user_id: Int
)