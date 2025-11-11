package com.student.Compass_Abroad.retrofit

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.modal.LoginResponseModel.LoginResponseModel
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.forgotPasswordModel.ForgotPasswordModel
import com.student.Compass_Abroad.modal.submitSinUp.SubmitSinUpForm
import com.student.Compass_Abroad.modal.verifyOtp.VerifyOtp
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.modal.inDemandCourse.InDemandCourse
import com.student.Compass_Abroad.modal.SaveReviewResponse.SaveReviewResponse
import com.student.Compass_Abroad.modal.admissionStatus.AdmissionStatus
import com.student.Compass_Abroad.modal.createTimeSlots.SlotData
import com.student.Compass_Abroad.modal.createTimeSlots.SlotRequest
import com.student.Compass_Abroad.modal.createTimeSlots.SlotResponse
import com.student.Compass_Abroad.modal.createTimeSlots.StatusInfo
import com.student.Compass_Abroad.modal.documentType.DocumentTypeModal
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.getApplicationAssignedStaff
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.getStaffList.StaffDropdownResponse
import com.student.Compass_Abroad.modal.getStaffSlots.GetStaffSlots
import com.student.Compass_Abroad.modal.getTestimonials.getTestimonials
import com.student.Compass_Abroad.modal.in_demandInstitution.InDemandInstitution
import com.student.Compass_Abroad.modal.top_destinations.TopDestinations
import org.json.JSONException
import org.json.JSONObject

class LoginViewModal : ViewModel() {

    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)

    /** for check User**/

    var checkUserModalMutableLiveData: MutableLiveData<CheckUserModel?>? = null

    fun checkUserModelLiveData(
        activity: Activity?,
        content: String
    ): LiveData<CheckUserModel?> {
        checkUserModalMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {

                CommonUtils.showProgress(it)

                apiInterface.checkUser(AppConstants.fiClientNumber, content)!!
                    .enqueue(object : Callback<CheckUserModel?> {
                        override fun onResponse(
                            call: Call<CheckUserModel?>,
                            response: Response<CheckUserModel?>
                        ) {
                            CommonUtils.dismissProgress()

                            if (response.isSuccessful && response.body() != null) {
                                checkUserModalMutableLiveData!!.postValue(response.body())
                            } else {
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<CheckUserModel?>, t: Throwable) {
                            CommonUtils.dismissProgress()
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })
            } else {
                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return checkUserModalMutableLiveData!!
    }


    /**For Login**/

    var loginModalMutableLiveData: MutableLiveData<LoginResponseModel?>? = null

    fun loginModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        content: String
    ): LiveData<LoginResponseModel?> {
        loginModalMutableLiveData = MutableLiveData()
        activity?.let {
            val apiErrorHandler = ApiErrorHandler(it.applicationContext)
            if (CommonUtils.isNetworkConnected(it)) {
                CommonUtils.showProgress(it)
                apiInterface.loginUser(client_number, device_number, content)
                    .enqueue(object : Callback<LoginResponseModel?> {
                        override fun onResponse(
                            call: Call<LoginResponseModel?>,
                            response: Response<LoginResponseModel?>
                        ) {
                            CommonUtils.dismissProgress()

                            if (response.isSuccessful && response.body() != null) {
                                loginModalMutableLiveData!!.postValue(response.body())
                            } else {
                                apiErrorHandler.handleError(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<LoginResponseModel?>, t: Throwable) {
                            CommonUtils.dismissProgress()
                            apiErrorHandler.handleError(t)
                        }
                    })
            } else {

                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return loginModalMutableLiveData!!
    }


    /** For Check OTP **/

    var verifyOTPMutableLiveData: MutableLiveData<VerifyOtp?>? = null

    fun verifyOTPModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        content: String
    ): LiveData<VerifyOtp?> {
        verifyOTPMutableLiveData = MutableLiveData()
        activity?.let {
            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                CommonUtils.showProgress(it)
                apiInterface.verifyOTP(client_number, device_number, content)
                    .enqueue(object : Callback<VerifyOtp?> {
                        override fun onResponse(
                            call: Call<VerifyOtp?>,
                            response: Response<VerifyOtp?>
                        ) {
                            CommonUtils.dismissProgress()
                            if (response.isSuccessful && response.body() != null) {
                                verifyOTPMutableLiveData!!.postValue(response.body())
                            } else {
                                // Handle unsuccessful responses using the ApiErrorHandler
                                apiErrorHandler.handleError(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<VerifyOtp?>, t: Throwable) {
                            CommonUtils.dismissProgress()

                            // Handle network failure or other exceptions
                            apiErrorHandler.handleError(t)
                        }
                    })
            } else {
                // Handle no internet connection
                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return verifyOTPMutableLiveData!!
    }


    /** Forgot Password **/


    var forgotPasscodeMutableLiveData: MutableLiveData<ForgotPasswordModel?>? = null

    fun forgetPasswordModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        content: String
    ): LiveData<ForgotPasswordModel?> {
        forgotPasscodeMutableLiveData = MutableLiveData()
        activity?.let {
            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {

                CommonUtils.showProgress(it)

                apiInterface.forgetPassword(client_number, device_number, content)
                    .enqueue(object : Callback<ForgotPasswordModel?> {
                        override fun onResponse(
                            call: Call<ForgotPasswordModel?>,
                            response: Response<ForgotPasswordModel?>
                        ) {
                            CommonUtils.dismissProgress()
                            if (response.isSuccessful && response.body() != null) {
                                forgotPasscodeMutableLiveData!!.postValue(response.body())
                            } else {

                                apiErrorHandler.handleError(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<ForgotPasswordModel?>, t: Throwable) {
                            CommonUtils.dismissProgress()

                            apiErrorHandler.handleError(t)
                        }
                    })
            } else {
                val noInternetMessage = "No internet connection"
                errorDialogOpen(activity, noInternetMessage)
            }
        }

        return forgotPasscodeMutableLiveData!!
    }

    /**For Submit SignUp From**/

    var submitDataModalMutableLiveData: MutableLiveData<SubmitSinUpForm?>? = null

    fun signUpFormModalLiveData(
        activity: Activity?,
        client_number: String,
        content: String
    ): LiveData<SubmitSinUpForm?> {

        submitDataModalMutableLiveData = MutableLiveData()

        activity?.let {
            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                apiInterface.submitLeadForm(client_number, content)
                    ?.enqueue(object : Callback<SubmitSinUpForm?> {
                        override fun onResponse(
                            call: Call<SubmitSinUpForm?>,
                            response: Response<SubmitSinUpForm?>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                submitDataModalMutableLiveData!!.postValue(response.body())
                            } else {
                                handleErrorSubmitLead(
                                    code = response.code(),
                                    backendMessage = "Error occurred",
                                    response = response,
                                    activity = activity
                                )
                                /*val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)*/
                            }
                        }

                        override fun onFailure(call: Call<SubmitSinUpForm?>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })
            } else {
                val noInternetMessage = "No internet connection"
                errorDialogOpen(activity, noInternetMessage)
            }
        }

        return submitDataModalMutableLiveData!!
    }


    private fun handleErrorSubmitLead(
        code: Int,
        backendMessage: String?,
        response: Response<SubmitSinUpForm?>? = null,
        activity: Activity?
    ) {
        val submitLeadResponse = SubmitSinUpForm().apply {
            statusCode = code
            message = when (code) {
                401 -> backendMessage ?: "Please check your credentials."
                500 -> backendMessage ?: "Internal Server Error"
                422 -> backendMessage ?: "Unprocessable Entity"
                else -> backendMessage ?: "Error $code"
            }
        }

        // If there's an error body in the response, attempt to parse it
        response?.errorBody()?.let { errorBodyResponse ->
            try {
                val errorBody = errorBodyResponse.string() ?: "No error body"
                Log.e("ErrorBody", errorBody) // Log the raw error body for debugging
                val errorList = mutableListOf<SubmitSinUpForm>()
                val errorJson = JSONObject(errorBody)
                val errorsArray = errorJson.optJSONArray("errors")
                submitLeadResponse.errors = errorsArray

                if (errorsArray != null && errorsArray.length() > 0) {

                    val errorMessages = StringBuilder()
                    for (i in 0 until errorsArray.length()) {
                        val firstError = errorsArray.getJSONObject(i)
                        val errorMessage = firstError.optString("message", errorBody)

                        Log.e("ParsedError", errorMessage)
                        // Iterate through keys and display each error message
                        val keys = firstError.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            val errorMessage1 = firstError.toString()
                            val cleanedMessage = errorMessage1
                                .replace("{", "")  // Remove opening brace
                                .replace("}", "")  // Remove closing brace
                                .replace("\"", "")


                            val formattedMessage =
                                cleanedMessage.split(",").joinToString("\n") { entry ->
                                    val keyValue = entry.split(":")
                                    if (keyValue.size == 2) {
                                        val key = keyValue[0].trim()
                                            .replaceFirstChar { it.uppercaseChar() } // Capitalize key
                                        val value = keyValue[1].trim()
                                        "$key: $value"
                                    } else {
                                        entry.trim()
                                    }
                                }

                            if (formattedMessage.isNotEmpty()) {
                                errorMessages.append("$formattedMessage\n") // Append message with a new line
                            }

                            // Display the error message in a Toast

                        }
                    }
                    val fullErrorMessage = errorMessages.toString().trim()
                    // errorDialogOpen(activity!!,fullErrorMessage)
                    //CommonUtils.toast(activity, fullErrorMessage)
                } else {
                    Log.e(
                        "ErrorParsing",
                        "Error response does not contain 'errors' array or is empty"
                    )
                    CommonUtils.toast(activity, "Unexpected error format")
                }

                submitLeadResponse.message = "Error: $errorBody"


            } catch (e: JSONException) {
                Log.e("ErrorParsing", "Error parsing error body: ${e.message}")
                CommonUtils.toast(activity, "Error parsing response")
                submitLeadResponse.message = "Error parsing response"
            }
        }

        Log.e("API Error", submitLeadResponse.message ?: "Unknown error")
        submitDataModalMutableLiveData?.postValue(submitLeadResponse)
    }


    fun getAdmissionStatus(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<AdmissionStatus?> {

        val liveData = MutableLiveData<AdmissionStatus?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                apiInterface.admissionStatus(
                    client_number,
                    device_number,
                    accessToken
                )?.enqueue(object : Callback<AdmissionStatus?> {
                    override fun onResponse(
                        call: Call<AdmissionStatus?>,
                        response: Response<AdmissionStatus?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                AdmissionStatus(
                                    statusCode = response.code(),
                                    success = false,
                                    message = errorMsg,
                                    statusInfo = null,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<AdmissionStatus?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            AdmissionStatus(
                                statusCode = 0,
                                success = false,
                                message = errorMsg,
                                statusInfo = null,
                                data = null
                            )
                        )
                    }
                })
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    AdmissionStatus(
                        statusCode = 0,
                        success = false,
                        message = errorMsg,
                        statusInfo = null,
                        data = null
                    )
                )
            }
        }

        return liveData
    }


    // document type

    fun getDocumentType(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        modules: String
    ): LiveData<DocumentTypeModal?> {

        val liveData = MutableLiveData<DocumentTypeModal?>()

        if (activity == null) {
            liveData.postValue(
                DocumentTypeModal(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                DocumentTypeModal(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        apiInterface.getDocumentType(
            clientNumber,
            deviceNumber,
            accessToken,
            modules
        )?.enqueue(object : Callback<DocumentTypeModal?> {
            override fun onResponse(
                call: Call<DocumentTypeModal?>,
                response: Response<DocumentTypeModal?>
            ) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        DocumentTypeModal(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<DocumentTypeModal?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                liveData.postValue(
                    DocumentTypeModal(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData
    }


    // save lead review



    fun saveStaffReview(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        identifier: String,
        user_identifier: String,
        title: String,
        content: String,
        rating: Int,
    ): LiveData<SaveReviewResponse?> {

        val liveData = MutableLiveData<SaveReviewResponse?>()

        if (activity == null) {
            liveData.postValue(
                SaveReviewResponse(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                SaveReviewResponse(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        apiInterface.saveReviewLead(
            clientNumber,
            deviceNumber,
            accessToken,
            identifier,
            user_identifier,
            title,
            content,
            rating
        )?.enqueue(object : Callback<SaveReviewResponse?> {
            override fun onResponse(
                call: Call<SaveReviewResponse?>,
                response: Response<SaveReviewResponse?>
            ) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        SaveReviewResponse(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<SaveReviewResponse?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                liveData.postValue(
                    SaveReviewResponse(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData
    }


    // get single application

    fun getSingleApplicationAssignStaff(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        identifier: String,

    ): LiveData<getApplicationAssignedStaff?> {

        val liveData = MutableLiveData<getApplicationAssignedStaff?>()

        if (activity == null) {
            liveData.postValue(
                getApplicationAssignedStaff(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                getApplicationAssignedStaff(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        apiInterface.getSingleApplicationsAssignStaff(
            clientNumber,
            deviceNumber,
            accessToken,
            identifier,
        )?.enqueue(object : Callback<getApplicationAssignedStaff?> {
            override fun onResponse(
                call: Call<getApplicationAssignedStaff?>,
                response: Response<getApplicationAssignedStaff?>
            ) {
                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        getApplicationAssignedStaff(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<getApplicationAssignedStaff?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                liveData.postValue(
                    getApplicationAssignedStaff(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData
    }


    fun get_branchList(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
    ): LiveData<getDestinationCountry?> {
        val liveData = MutableLiveData<getDestinationCountry?>()
        if (activity == null) {
            liveData.postValue(
                getDestinationCountry(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                getDestinationCountry(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }
        CommonUtils.showProgress(activity)


        apiInterface.getBranchList( clientNumber, deviceNumber, accessToken
        )?.enqueue(object : Callback<getDestinationCountry?> {

            override fun onResponse(

                call: Call<getDestinationCountry?>,
                response: Response<getDestinationCountry?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        getDestinationCountry(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<getDestinationCountry?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    getDestinationCountry(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData
    }

    // get staff slots

    fun get_staffSlots(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        date: String,
        branch_id: String,
    ): LiveData<GetStaffSlots?> {
        val liveData = MutableLiveData<GetStaffSlots?>()
        if (activity == null) {
            liveData.postValue(
                GetStaffSlots(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                GetStaffSlots(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }
        CommonUtils.showProgress(activity)


        apiInterface.getStaffSlots( clientNumber, deviceNumber, accessToken,
             date, branch_id
        ).enqueue(object : Callback<GetStaffSlots?> {

            override fun onResponse(

                call: Call<GetStaffSlots?>,
                response: Response<GetStaffSlots?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        GetStaffSlots(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<GetStaffSlots?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    GetStaffSlots(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData
    }

    fun CreateSlots(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        branch_identifier: String,
        event_start_datetime: String,
        event_end_datetime: String,

    ): LiveData<SlotResponse?> {
        val liveData = MutableLiveData<SlotResponse?>()
        if (activity == null) {
            liveData.postValue(
                SlotResponse(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference",
                    statusInfo = StatusInfo("", 0, "", "", ""),
                    data = null
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                SlotResponse(
                    statusCode = 0,
                    success = false,
                    message = errorMsg,
                    statusInfo = StatusInfo("", 0, "", "", ""),
                    data = null
                )
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface.bookSlotForUser(clientNumber, deviceNumber, accessToken, branch_identifier,
            event_start_datetime, event_end_datetime
        )
            .enqueue(object : Callback<SlotResponse?> {
                override fun onResponse(
                    call: Call<SlotResponse?>,
                    response: Response<SlotResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful) {
                        liveData.postValue(response.body())
                    } else {
                        val errorMsg = apiErrorHandler.handleError(HttpException(response))
                        liveData.postValue(
                            SlotResponse(
                                statusCode = response.code(),
                                success = false,
                                message = errorMsg,
                                statusInfo = StatusInfo("", response.code(), "", "", ""),
                                data = null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<SlotResponse?>, t: Throwable) {
                    val errorMsg = apiErrorHandler.handleError(t)
                    CommonUtils.dismissProgress()
                    liveData.postValue(
                        SlotResponse(
                            statusCode = 0,
                            success = false,
                            message = errorMsg,
                            statusInfo = StatusInfo("", 0, "", "", ""),
                            data = null
                        )
                    )
                }
            })

        return liveData
    }
    fun get_staffList(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        identifier: String?,
        number_only: Boolean?,
    ): LiveData<StaffDropdownResponse?> {
        val liveData = MutableLiveData<StaffDropdownResponse?>()
        if (activity == null) {
            liveData.postValue(
                StaffDropdownResponse(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                StaffDropdownResponse(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface.getStaffList(clientNumber, deviceNumber, accessToken,
            identifier, number_only
        )?.enqueue(object : Callback<StaffDropdownResponse?> {

            override fun onResponse(

                call: Call<StaffDropdownResponse?>,
                response: Response<StaffDropdownResponse?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        StaffDropdownResponse(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<StaffDropdownResponse?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    StaffDropdownResponse(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData

    }

    // top destination


    fun get_topdestination(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,

    ): LiveData<TopDestinations?> {
        val liveData = MutableLiveData<TopDestinations?>()
        if (activity == null) {
            liveData.postValue(
                TopDestinations(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                TopDestinations(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface.getTopDestination(clientNumber, deviceNumber, accessToken
        )?.enqueue(object : Callback<TopDestinations?> {

            override fun onResponse(

                call: Call<TopDestinations?>,
                response: Response<TopDestinations?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        TopDestinations(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<TopDestinations?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    TopDestinations(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData

    }

    // in-demandCourses


    fun get_in_demandCourses(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,

        ): LiveData<InDemandCourse?> {
        val liveData = MutableLiveData<InDemandCourse?>()
        if (activity == null) {
            liveData.postValue(
                InDemandCourse(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                InDemandCourse(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface.get_InDemandCourses(clientNumber, deviceNumber, accessToken
        )?.enqueue(object : Callback<InDemandCourse?> {

            override fun onResponse(

                call: Call<InDemandCourse?>,
                response: Response<InDemandCourse?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        InDemandCourse(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<InDemandCourse?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    InDemandCourse(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData

    }


    // in-demandInstitution


    fun get_in_demandInstitution(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,

        ): LiveData<InDemandInstitution?> {
        val liveData = MutableLiveData<InDemandInstitution?>()
        if (activity == null) {
            liveData.postValue(
                InDemandInstitution(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return liveData
        }
        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                InDemandInstitution(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface.get_InDemandInstitution(clientNumber, deviceNumber, accessToken
        )?.enqueue(object : Callback<InDemandInstitution?> {

            override fun onResponse(

                call: Call<InDemandInstitution?>,
                response: Response<InDemandInstitution?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful) {
                    liveData.postValue(response.body())
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        InDemandInstitution(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<InDemandInstitution?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                CommonUtils.dismissProgress()

                liveData.postValue(
                    InDemandInstitution(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return liveData

    }

    fun getTestimonials(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        is_visible:Boolean,
        type:String
    ): LiveData<getTestimonials?> {

        val liveData = MutableLiveData<getTestimonials?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                apiInterface.getTestimonials(
                    client_number,
                    device_number,
                    accessToken,is_visible,type
                )?.enqueue(object : Callback<getTestimonials?> {
                    override fun onResponse(
                        call: Call<getTestimonials?>,
                        response: Response<getTestimonials?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                getTestimonials(
                                    statusCode = response.code(),
                                    success = false,
                                    message = errorMsg,
                                    statusInfo = null,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<getTestimonials?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            getTestimonials(
                                statusCode = 0,
                                success = false,
                                message = errorMsg,
                                statusInfo = null,
                                data = null
                            )
                        )
                    }
                })
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    getTestimonials(
                        statusCode = 0,
                        success = false,
                        message = errorMsg,
                        statusInfo = null,
                        data = null
                    )
                )
            }
        }

        return liveData
    }

}