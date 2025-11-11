package com.student.Compass_Abroad.Utils

import com.student.Compass_Abroad.R
import org.json.JSONException
import org.json.JSONObject

object AppConstants {
    const val LoginModel = "saveModel"
    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val SCOUtLOGIN = "scoutLogin"
    var profileStatus = "0"
    var PROGRAM_STATUS = "0"

    val fiClientNumber: String
        get() = App.context.getString(R.string.fiClientNumber)

    const val LAST_NAME = "last name"
    const val FIRST_NAME = "first name"
    const val USER_IMAGE = "user image"
    const val USER_PREFERENCES = "user preferences"
    const val USER_DISCIPLINES = "user disciplines"
    const val GENDER = "gender"
    const val MARITAL_STATUS = "marital status"
    const val DOB = "dob"
    const val privateKey = "3cd8d4b8fca07709"
    const val appSecret = "1b7f65a9b9548b163cd8d4b8fca077097357c95d3e5cf169fc46f96164e3da4d"
    const val LOGIN_STATUS = "login status"
    const val USER_EMAIL = "user email"
    const val publicKey = "public key"
    const val USER_ID = "user id"
    const val USER_NAME = "user name"
    const val USER_IDENTIFIER = "USER_IDENTIFIER"
    const val Device_IDENTIFIER = "Device_IDENTIFIER"
    const val USER_DEVICES = "user_devices"
    const val OTP_IDENTIFIER = "OTP_IDENTIFIER"
    const val OTP = "OTP"
    const val Profile_URL = "Profile_URL"
    const val User_IDENTIFIER = "User_IDENTIFIER"
    const val Client_Number = "clent_number"
    const val PHONE = "phone"
    const val USER_ROLE = "user_role"
    const val SAVE_MODAL = "save model"
    const val ISLOggedIn = "false"
    const val countryId = "countryId"
    var COUNTRY_CODE = "test"
    const val EnglishLevelList = "EnglishLevelList"
    const val AgeList = "AgeList"
    const val Accomodation = "Accomodation"
    const val CATEGORY = "category"
    const val PROGRAM_CATEGORY = "program_category"
    const val STUDY_LEVEL = "studyLevel"
    const val RELATION_IDENTIFIER = "relation_identifier"

    //program filters

    const val CountryList = "selectedCountry1"
    const val StateList = "selectedStates1"
    const val CityList = "selectedCity1"
    const val institutionList = "selectedInstitution"
    const val studyLevelList = "selectedStudyLevel"
    const val disciplineList = "selectedDiscipline"
    const val IntakeList = "selectedIntake"
    const val PGWP_KEY = "PGWP"
    const val ATTENDANCE_KEY = "Attendance"
    const val PROGRAM_TYPE_KEY = "ProgramType"
    const val MIN_TUTION_KEY = "minTutionFee"
    const val MAX_TUTION_KEY = "maxTutionFee"
    const val MIN_APPLICATION_KEY = "minApplicationFee"
    const val MAX_APPLICATION_KEY = "maxApplicationFee"


    object SocketIO {

      val BASE_URL: String get() = App.context.getString(R.string.socket_base_url)

        const val ACTION_KEY = "action"
        const val ROOM_KEY = "room"
        const val JOIN_ROOM = "joinRoom"
        const val USD_VALUE = "USD-CAD"

        val DEFAULT_VALUES: JSONObject = JSONObject().apply {
            try {
                put(ACTION_KEY, JOIN_ROOM)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            try {
                put(ROOM_KEY, USD_VALUE)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }
}
