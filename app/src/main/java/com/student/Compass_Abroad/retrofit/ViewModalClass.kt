package com.student.Compass_Abroad.retrofit

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.student.Compass_Abroad.ApiResponseForm
import com.student.firmliagent.modal.getLeadModel.getLeadResponse
import com.student.Compass_Abroad.ChatMessageModels
import com.student.Compass_Abroad.CreateApplicationRequest
import com.student.Compass_Abroad.SavePreferencesRequest
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.BecomeScoutModel.BecomeaScout
import com.student.Compass_Abroad.modal.ChangeLeadStatus
import com.student.Compass_Abroad.modal.CommunityCategories.CategoriesResponse
import com.student.Compass_Abroad.modal.CreateApplication.CreateApplicationModal
import com.student.Compass_Abroad.modal.DestinationCountryModal
import com.student.Compass_Abroad.modal.EditCommentModal.EditCommentResponse
import com.student.Compass_Abroad.modal.EditPostModel.EditPostResponse
import com.student.Compass_Abroad.modal.EditReplyModel.EditReplyResponse
import com.student.Compass_Abroad.modal.GetCampusModal.GetCampusResponse
import com.student.Compass_Abroad.modal.GetStudentsModal.GetStudentResponse
import com.student.Compass_Abroad.modal.LeadSourceModal
import com.student.Compass_Abroad.modal.LoginResponseModel.LoginResponseModel
import com.student.Compass_Abroad.modal.ReportReasons.ReportReasonresponse
import com.student.Compass_Abroad.modal.allFieldResponse.formAllFieldResponse
import com.student.Compass_Abroad.modal.campusModel.CampusModel
import com.student.Compass_Abroad.modal.chatMessage.ChatMessageResponse
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.cityModel.CityModel
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.countryModel.CountryResponse
import com.student.Compass_Abroad.modal.createPostResponse.CreatePostResponse
import com.student.Compass_Abroad.modal.deleteCommentResponse.DeleteCommentResponse
import com.student.Compass_Abroad.modal.deletePostResponse.DeletePostResponse
import com.student.Compass_Abroad.modal.deleteReplyModel.DeleteReplyResponse
import com.student.Compass_Abroad.modal.discipline.DisciplineModel


import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler.getErrorMessage
import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler.parseError
import com.student.Compass_Abroad.modal.forgotPasswordModel.ForgotPasswordModel
import com.student.Compass_Abroad.modal.generatingPaymentLinkforApplication.generatingPaymentLinkApplication

import com.student.Compass_Abroad.modal.getAllComments.getAllComments
import com.student.Compass_Abroad.modal.getAllPosts.getAllPostResponse
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.getApplicationAssignedStaff
import com.student.Compass_Abroad.modal.getApplicationDocuments.getApplicationDocuments
import com.student.Compass_Abroad.modal.getApplicationNotes.getApplicationNotes
import com.student.Compass_Abroad.modal.getApplicationRemider.getApplicationReminderResponse
import com.student.Compass_Abroad.modal.getApplicationTimelineResponse.getApplicationTimelineResponse
import com.student.Compass_Abroad.modal.getCategoryLeadStat.getCategoryLeadStat
import com.student.Compass_Abroad.modal.getCommentReplies.getCommentReplies
import com.student.Compass_Abroad.modal.getDocumentTypes.getDocumentTypes
import com.student.Compass_Abroad.modal.getLeadAssignedStaffResponse.getLeadAssignedStaffResponse
import com.student.Compass_Abroad.modal.getLeadPaymentLinks.getLeadPaymentLinks
import com.student.Compass_Abroad.modal.getLeadShorlistedProgram.getLeadShortlistedProgram
import com.student.Compass_Abroad.modal.getLeadTimelineResponse.getLeadTimelineResponse
import com.student.Compass_Abroad.modal.getPaymentForDropDown.getPaymentForDropDown
import com.student.Compass_Abroad.modal.getPaymentMode.getPaymentMode
import com.student.Compass_Abroad.modal.intakeModel.IntakeModel
import com.student.Compass_Abroad.modal.likePost.LikeResponse
import com.student.Compass_Abroad.modal.postApplicationNotes.PostApplicationNotes
import com.student.Compass_Abroad.modal.postComment.PostComment
import com.student.Compass_Abroad.modal.PostLeadNotesResponse.postLeadNotesResponse
import com.student.Compass_Abroad.modal.PreferCollageModal.PreferCollageModal
import com.student.Compass_Abroad.modal.ProgramTags.ProgramTags
import com.student.Compass_Abroad.modal.SaveReviewResponse.SaveReviewResponse
import com.student.Compass_Abroad.modal.TokenFcmData.TokenFcmData
import com.student.Compass_Abroad.modal.UtmModal.UtmModalResponse
import com.student.Compass_Abroad.modal.ambassadorAddConversation.AmbassadorAddConversation
import com.student.Compass_Abroad.modal.ambassadorGetChat.AmbassadorGetChatData
import com.student.Compass_Abroad.modal.ambassadroChatList.AmbassadorChatListModal
import com.student.Compass_Abroad.modal.applicationProgramDetails.ApplicationProgramResponse
import com.student.Compass_Abroad.modal.changeStatusReminder.changeStatusReminder
import com.student.Compass_Abroad.modal.counsellingModal.CounsellingResponse
import com.student.Compass_Abroad.modal.createAttende.CreateAttende
import com.student.Compass_Abroad.modal.createCounsellingModel.createCounsellingModel
import com.student.Compass_Abroad.modal.createRefreralLink.getRefferalLink
import com.student.Compass_Abroad.modal.editProfile.EditProfile
import com.student.Compass_Abroad.modal.editProfile.UploadImages
import com.student.Compass_Abroad.modal.findAmbassadorModal.AmbassadorModal
import com.student.Compass_Abroad.modal.generatingPaymentLinkVoucher.generatingPaymentLinkVoucher
import com.student.Compass_Abroad.modal.getApplicationResponse.getApplicationResponse
import com.student.Compass_Abroad.modal.getBannerModel.getBannerModel
import com.student.Compass_Abroad.modal.getCategoryProgramModel.getCategoryProgramModel
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.getDestintionManager.getDestinationmanager
import com.student.Compass_Abroad.modal.getHistoryListModel.getHistoryListModel
import com.student.Compass_Abroad.modal.getLeadCounsellings.getLeadCounsellings
import com.student.Compass_Abroad.modal.getLeadNotes.getLeadNotesResponse
import com.student.Compass_Abroad.modal.getLeadReminderResponse.GetLeadReminderResponse

import com.student.Compass_Abroad.modal.getLeads.getLeadsModal
import com.student.Compass_Abroad.modal.getLeadsDocuments.getLeadsDocuments
import com.student.Compass_Abroad.modal.getNotification.getNotificationResponse
import com.student.Compass_Abroad.modal.getNotificationRead.getNotificationReadResponse
import com.student.Compass_Abroad.modal.getNotificationReadAll.getNotificationReadAllResponse
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.GetOffersandUpdates
import com.student.Compass_Abroad.modal.getPaymentApplication.getPaymentApplication
import com.student.Compass_Abroad.modal.getPaymentApplicationPay.GetPaymentApplicationPay
import com.student.Compass_Abroad.modal.getProgramFilters.getProgramFIltersResponse
import com.student.Compass_Abroad.modal.getReviewList.getReviewList
import com.student.Compass_Abroad.modal.getScholarships.GetScholarships
import com.student.Compass_Abroad.modal.getStudentPref.GetStudentPreferences
import com.student.Compass_Abroad.modal.getSubWorkliiTabs.getSubWorkliiTabInfo
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.modal.getVouchersHistoryTabs.getVouchersHistoryTabs
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.modal.getWorkliiTabs.getWorklliTabs
import com.student.Compass_Abroad.modal.institutionModel.InstitutionModel
import com.student.Compass_Abroad.modal.joinAmbassadorChat.JoinAmbassadorChatModal
import com.student.Compass_Abroad.modal.logoutUser.Logout
import com.student.Compass_Abroad.modal.paymentDetails.ApplicationPaymentDetails
import com.student.Compass_Abroad.modal.postLeadReminder.postLeadReminder
import com.student.Compass_Abroad.modal.postLeadStatus.postLeadStatus
import com.student.Compass_Abroad.modal.preferCountryList.GetPreferCountryList

import com.student.Compass_Abroad.modal.reactionModel.REactionResponse
import com.student.Compass_Abroad.modal.replyModel.ReplyComment
import com.student.Compass_Abroad.modal.reportPost.ReportResponse
import com.student.Compass_Abroad.modal.saveApplicationDocuments.SaveDocumentsRequest
import com.student.Compass_Abroad.modal.saveApplicationDocuments.saveApplicationDocuments
import com.student.Compass_Abroad.modal.savePeferences.SavePreferences
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.modal.stateModel.stateModel
import com.student.Compass_Abroad.modal.studyLevelModel.StudyLevelModal
import com.student.Compass_Abroad.modal.testScoreModel.TestScoreModel
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.modal.verifyOtp.VerifyOtp
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import com.student.Compass_Abroad.retrofit.RetrofitClient2.retrofitCallerObject2
import com.student.Compass_Abroad.retrofit.RetrofitClient3.retrofitCallerObject3
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewModalClass : ViewModel() {
    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)
    var apiInterface2 = retrofitCallerObject2!!.create(ApiInterface::class.java)
    var apiInterface3 = retrofitCallerObject3!!.create(ApiInterface::class.java)

    //for check User

    var checkUserModalMutableLiveData: MutableLiveData<CheckUserModel?>? = null
    fun checkUserModelLiveData(
        activity: Activity?,
        content: String
    ): LiveData<CheckUserModel?> {
        checkUserModalMutableLiveData = MutableLiveData()
        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
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
                            val apiError = parseError(response)
                            handleError(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CheckUserModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError(0, "No internet connection.")
        }
        return checkUserModalMutableLiveData!!
    }


    private fun handleError(code: Int, backendMessage: String?) {
        var checkUserModel = CheckUserModel()
        checkUserModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> "username must be a valid email"
            else -> backendMessage ?: "Error $code"
        }
        checkUserModel.message = errorMessage
        Log.e("API Error", checkUserModel.message!!)
        checkUserModalMutableLiveData!!.postValue(checkUserModel)

    }

    //For Login




    //verifyOTP
    var verifyOTPMutableLiveData: MutableLiveData<VerifyOtp?>? = null

    fun verifyOTPModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        content: String
    ): LiveData<VerifyOtp?> {
        verifyOTPMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
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
                            val apiError = parseError(response)
                            handleError4(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<VerifyOtp?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError4(0, "Network error: " + t.message)
                    }
                })
        } else {

            handleError4(0, "No internet connection.")

        }

        return verifyOTPMutableLiveData!!

    }

    private fun handleError4(code: Int, backendMessage: String?) {
        val verifyResponse = VerifyOtp()
        verifyResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> "$backendMessage"
            else -> backendMessage ?: "Error $code"
        }
        verifyResponse.message = errorMessage
        Log.e("API Error", verifyResponse.message!!)
        verifyOTPMutableLiveData!!.postValue(verifyResponse)

    }


    //forgot pasword

    var forgotPasscodeMutableLiveData: MutableLiveData<ForgotPasswordModel?>? = null

    fun forgetPasswordModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        content: String
    ): LiveData<ForgotPasswordModel?> {
        forgotPasscodeMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
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
                            val apiError = parseError(response)
                            handleError5(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<ForgotPasswordModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError5(0, "Network error: " + t.message)
                    }
                })
        } else {

            handleError5(0, "No internet connection.")

        }


        return forgotPasscodeMutableLiveData!!

    }

    private fun handleError5(code: Int, backendMessage: String?) {
        var forgotResponse = ForgotPasswordModel()
        forgotResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> "$backendMessage"
            else -> backendMessage ?: "Error $code"
        }
        forgotResponse.message = errorMessage
        Log.e("API Error", forgotResponse.message!!)
        forgotPasscodeMutableLiveData!!.postValue(forgotResponse)

    }


    //getClient Events

    var clientEventMutableLiveData1: MutableLiveData<ClientEventResponse?>? = null
    fun clientEventsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
    ): LiveData<ClientEventResponse?> {

        clientEventMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getClientEvents(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                "desc",
                "future",
                "online",
                "everyone,staff"
            ).enqueue(object : Callback<ClientEventResponse?> {
                override fun onResponse(
                    call: Call<ClientEventResponse?>,
                    response: Response<ClientEventResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        clientEventMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError6(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<ClientEventResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError6(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return clientEventMutableLiveData1!!
    }

    private fun handleError6(code: Int, backendMessage: String?) {
        val clientEventResponse = ClientEventResponse()
        clientEventResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        clientEventResponse.message = errorMessage
        Log.e("API Error", clientEventResponse.message!!)
        clientEventMutableLiveData1!!.postValue(clientEventResponse)
    }


//getPrograms

    var AllProgramMutableLiveData1: MutableLiveData<AllProgramModel?>? = null
    fun getAllProgramsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        countryId: List<String>,
        stateId: List<String>,
        cityId: List<String>,
        institutionId: List<String>,
        is_pgwp_available: String? = null,
        study_level_id: List<String>,
        displine: List<String>,
        available: String? = null,
        program_type: String? = null,
        intake: List<String>,
        tvminTutionFee: String?,
        tvMaxTutionFee: String?,
        tvMinApplicationFee: String?,
        tvMaxApplicationFee: String?,
        search: String?,
        category: String?,
        tvAccomodation: String? = null,
        english_level_id: String? = null,
        age: String? = null,
        isrecommended: String? = null,
    ): LiveData<AllProgramModel?> {

        AllProgramMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getAllPrograms(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                countryId,
                stateId,
                cityId,
                institutionId,
                is_pgwp_available,
                study_level_id,
                displine,
                available,
                program_type,
                intake,
                tvminTutionFee,
                tvMaxTutionFee,
                tvMinApplicationFee,
                tvMaxApplicationFee,
                search,
                category,
                tvAccomodation,
                english_level_id, age,
                isrecommended
            )!!.enqueue(object : Callback<AllProgramModel?> {
                override fun onResponse(
                    call: Call<AllProgramModel?>,
                    response: Response<AllProgramModel?>
                ) {
                    //    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        AllProgramMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError7(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError7(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllProgramMutableLiveData1!!
    }


    private fun handleError7(code: Int, backendMessage: String?) {
        var allProgramModel = AllProgramModel()
        allProgramModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        allProgramModel.message = errorMessage
        Log.e("API Error", allProgramModel.message!!)
        AllProgramMutableLiveData1!!.postValue(allProgramModel)
    }


    //shortListed api

    var AllShorListMutableLiveData1: MutableLiveData<ShortListResponse?>? = null
    fun getshorListModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        content: String,
    ): LiveData<ShortListResponse?> {

        AllShorListMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.addShorListProgram(client_number, device_number, acessToken, content)!!
                .enqueue(object : Callback<ShortListResponse?> {
                    override fun onResponse(
                        call: Call<ShortListResponse?>,
                        response: Response<ShortListResponse?>
                    ) {
                        //  CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            AllShorListMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError8(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<ShortListResponse?>, t: Throwable) {
                        //  CommonUtils.dismissProgress()
                        handleError8(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllShorListMutableLiveData1!!
    }


    private fun handleError8(code: Int, backendMessage: String?) {
        val shortListResponse = ShortListResponse()
        shortListResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        shortListResponse.message = errorMessage
        Log.e("API Error", shortListResponse.message!!)
        AllShorListMutableLiveData1!!.postValue(shortListResponse)
    }


// get country

    var getCountryMutableLiveData1: MutableLiveData<CountryResponse?>? = null
    fun getCountryModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String

    ): LiveData<CountryResponse?> {

        getCountryMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getCountry(client_number, device_number, acessToken)!!
                .enqueue(object : Callback<CountryResponse?> {
                    override fun onResponse(
                        call: Call<CountryResponse?>,
                        response: Response<CountryResponse?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCountryMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError9(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CountryResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError9(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getCountryMutableLiveData1!!
    }


    private fun handleError9(code: Int, backendMessage: String?) {
        var countryResponse = CountryResponse()
        countryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        countryResponse.message = errorMessage
        Log.e("API Error", countryResponse.message!!)
        getCountryMutableLiveData1!!.postValue(countryResponse)
    }

//get State


    var getStateMutableLiveData1: MutableLiveData<stateModel?>? = null
    fun getStateModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        countryId: Int?

    ): LiveData<stateModel?> {

        getStateMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getState(client_number, device_number, acessToken, countryId)!!
                .enqueue(object : Callback<stateModel?> {
                    override fun onResponse(
                        call: Call<stateModel?>,
                        response: Response<stateModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getStateMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError10(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<stateModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError10(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getStateMutableLiveData1!!
    }


    private fun handleError10(code: Int, backendMessage: String?) {
        var stateResponse = stateModel()
        stateResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        stateResponse.message = errorMessage
        Log.e("API Error", stateResponse.message!!)
        getStateMutableLiveData1!!.postValue(stateResponse)
    }


    // get City
    var getCityMutableLiveData1: MutableLiveData<CityModel?>? = null
    fun getCityModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        stateId: Int?

    ): LiveData<CityModel?> {

        getCityMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getCity(client_number, device_number, acessToken, stateId)!!
                .enqueue(object : Callback<CityModel?> {
                    override fun onResponse(
                        call: Call<CityModel?>,
                        response: Response<CityModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCityMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError11(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CityModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError11(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getCityMutableLiveData1!!
    }


    private fun handleError11(code: Int, backendMessage: String?) {
        var cityResponse = CityModel()
        cityResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        cityResponse.message = errorMessage
        Log.e("API Error", cityResponse.message!!)
        getCityMutableLiveData1!!.postValue(cityResponse)
    }

    //getCampus

    var getCampusMutableLiveData1: MutableLiveData<CampusModel?>? = null
    fun getCampusModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        city_id: Int

    ): LiveData<CampusModel?> {

        getCampusMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getCampus(client_number, device_number, acessToken, city_id)!!
                .enqueue(object : Callback<CampusModel?> {
                    override fun onResponse(
                        call: Call<CampusModel?>,
                        response: Response<CampusModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCampusMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError12(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CampusModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError12(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getCampusMutableLiveData1!!
    }


    private fun handleError12(code: Int, backendMessage: String?) {
        var campusResponse = CampusModel()
        campusResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        campusResponse.message = errorMessage
        Log.e("API Error", campusResponse.message!!)
        getCampusMutableLiveData1!!.postValue(campusResponse)
    }


    //getStudy Level

    var getStudyLevelMutableLiveData1: MutableLiveData<StudyLevelModal?>? = null
    fun getStudyLevelModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,

        ): LiveData<StudyLevelModal?> {

        getStudyLevelMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getStudyLevel(client_number, device_number, acessToken)!!
                .enqueue(object : Callback<StudyLevelModal?> {
                    override fun onResponse(
                        call: Call<StudyLevelModal?>,
                        response: Response<StudyLevelModal?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getStudyLevelMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError13(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<StudyLevelModal?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError13(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getStudyLevelMutableLiveData1!!
    }


    private fun handleError13(code: Int, backendMessage: String?) {
        var studyResponse = StudyLevelModal()
        studyResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        studyResponse.message = errorMessage
        Log.e("API Error", studyResponse.message!!)
        getStudyLevelMutableLiveData1!!.postValue(studyResponse)
    }

    //getDiscipline
    var getDisciplineMutableLiveData1: MutableLiveData<DisciplineModel?>? = null
    fun getDisciplineModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,

        ): LiveData<DisciplineModel?> {

        getDisciplineMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getdiscipline(client_number, device_number, acessToken)!!
                .enqueue(object : Callback<DisciplineModel?> {
                    override fun onResponse(
                        call: Call<DisciplineModel?>,
                        response: Response<DisciplineModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getDisciplineMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError14(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<DisciplineModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError14(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getDisciplineMutableLiveData1!!
    }


    private fun handleError14(code: Int, backendMessage: String?) {
        var disciplineResponse = DisciplineModel()
        disciplineResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        disciplineResponse.message = errorMessage
        Log.e("API Error", disciplineResponse.message!!)
        getDisciplineMutableLiveData1!!.postValue(disciplineResponse)
    }

    //getTestScore

    var getTestScoreMutableLiveData1: MutableLiveData<TestScoreModel?>? = null
    fun getTestScoreModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
    ): LiveData<TestScoreModel?> {

        getTestScoreMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getTestScore(client_number, device_number, acessToken)!!
                .enqueue(object : Callback<TestScoreModel?> {
                    override fun onResponse(
                        call: Call<TestScoreModel?>,
                        response: Response<TestScoreModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getTestScoreMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError15(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<TestScoreModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError15(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getTestScoreMutableLiveData1!!
    }


    private fun handleError15(code: Int, backendMessage: String?) {
        var testScoreResponse = TestScoreModel()
        testScoreResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        testScoreResponse.message = errorMessage
        Log.e("API Error", testScoreResponse.message!!)
        getTestScoreMutableLiveData1!!.postValue(testScoreResponse)
    }


    //getInTake
    var getIntakeMutableLiveData1: MutableLiveData<IntakeModel?>? = null
    fun getIntakeModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
    ): LiveData<IntakeModel?> {

        getIntakeMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //  CommonUtils.showProgress(activity)
            apiInterface2.getIntake(client_number, device_number, acessToken)!!
                .enqueue(object : Callback<IntakeModel?> {
                    override fun onResponse(
                        call: Call<IntakeModel?>,
                        response: Response<IntakeModel?>
                    ) {
                        //  CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getIntakeMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError16(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<IntakeModel?>, t: Throwable) {
                        //  CommonUtils.dismissProgress()
                        handleError16(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getIntakeMutableLiveData1!!
    }


    private fun handleError16(code: Int, backendMessage: String?) {
        var intakeResponse = IntakeModel()
        intakeResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        intakeResponse.message = errorMessage
        Log.e("API Error", intakeResponse.message!!)
        getIntakeMutableLiveData1!!.postValue(intakeResponse)
    }


    //getShorListedProgram

    var AllshorlistedProgramMutableLiveData1: MutableLiveData<AllProgramModel?>? = null
    fun getshortlistedModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        category: String
    ): LiveData<AllProgramModel?> {

        AllshorlistedProgramMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getshortListedPrograms(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                true,
                category
            ).enqueue(object : Callback<AllProgramModel?> {
                override fun onResponse(
                    call: Call<AllProgramModel?>,
                    response: Response<AllProgramModel?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        AllshorlistedProgramMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError17(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                    handleError17(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllshorlistedProgramMutableLiveData1!!
    }


    private fun handleError17(code: Int, backendMessage: String?) {
        var shortlistedModel = AllProgramModel()
        shortlistedModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        shortlistedModel.message = errorMessage
        Log.e("API Error", shortlistedModel.message!!)
        AllshorlistedProgramMutableLiveData1!!.postValue(shortlistedModel)
    }


    // shortlisted by staff


    var AllshorlistedProgramMutableLiveDataStaff1: MutableLiveData<AllProgramModel?>? = null

    fun getshortlistedModalLiveDataStaff(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        category: String,
    ): LiveData<AllProgramModel?> {

        AllshorlistedProgramMutableLiveDataStaff1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.addShorListProgramAssignedByStaff(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                true,
                category
            ).enqueue(object : Callback<AllProgramModel?> {
                override fun onResponse(
                    call: Call<AllProgramModel?>,
                    response: Response<AllProgramModel?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        AllshorlistedProgramMutableLiveDataStaff1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError1Staff7(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                    handleError1Staff7(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllshorlistedProgramMutableLiveDataStaff1!!
    }


    private fun handleError1Staff7(code: Int, backendMessage: String?) {
        var shortlistedModel = AllProgramModel()
        shortlistedModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        shortlistedModel.message = errorMessage
        Log.e("API Error", shortlistedModel.message!!)
        AllshorlistedProgramMutableLiveDataStaff1!!.postValue(shortlistedModel)
    }


    //

    var getCategoriesMutableLiveData1: MutableLiveData<CategoriesResponse?>? = null
    fun getCategoriesLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String

    ): LiveData<CategoriesResponse?> {

        getCategoriesMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getCategories(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<CategoriesResponse?> {
                    override fun onResponse(
                        call: Call<CategoriesResponse?>,
                        response: Response<CategoriesResponse?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCategoriesMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError18(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CategoriesResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError18(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getCategoriesMutableLiveData1!!
    }


    private fun handleError18(code: Int, backendMessage: String?) {
        var categoriesResponse = CategoriesResponse()
        categoriesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        categoriesResponse.message = errorMessage
        Log.e("API Error", categoriesResponse.message!!)
        getCategoriesMutableLiveData1!!.postValue(categoriesResponse)
    }


    var createPostMutableLiveData: MutableLiveData<CreatePostResponse?>? = null

    fun CreatePostLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        content: String

    ): LiveData<CreatePostResponse?> {

        createPostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.createPost(client_number, device_number, accessToken, content)
                .enqueue(object : Callback<CreatePostResponse?> {
                    override fun onResponse(
                        call: Call<CreatePostResponse?>,
                        response: Response<CreatePostResponse?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            createPostMutableLiveData!!.postValue(response.body())

                            if (response.isSuccessful) {

                                CommonUtils.toast(activity, "Post Created Successfully")
                                activity.onBackPressed()
                            } else {
                                CommonUtils.toast(activity, "Post not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError19(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<CreatePostResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError19(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError19(0, "No internet connection.")
        }
        return createPostMutableLiveData!!
    }

    private fun handleError19(code: Int, backendMessage: String?) {
        var createPost = CreatePostResponse()
        createPost.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        createPost.message = errorMessage
        Log.e("API Error", createPost.message!!)
        createPostMutableLiveData!!.postValue(createPost)
    }

    var getAllPostsMutableLiveData1: MutableLiveData<getAllPostResponse?>? = null
    fun getAllPostLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        perPage: Int

    ): LiveData<getAllPostResponse?> {

        getAllPostsMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getAllPosts(client_number, device_number, accessToken, page, perPage)!!
                .enqueue(object : Callback<getAllPostResponse?> {
                    override fun onResponse(
                        call: Call<getAllPostResponse?>,
                        response: Response<getAllPostResponse?>
                    ) {
                        //CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getAllPostsMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError20(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getAllPostResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError20(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getAllPostsMutableLiveData1!!
    }


    private fun handleError20(code: Int, backendMessage: String?) {
        var getAllPostResponse = getAllPostResponse()
        getAllPostResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getAllPostResponse.message = errorMessage
        Log.e("API Error", getAllPostResponse.message!!)
        getAllPostsMutableLiveData1!!.postValue(getAllPostResponse)
    }

    var getmyPostsMutableLiveData1: MutableLiveData<getAllPostResponse?>? = null
    fun getMyPostLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        perPage: Int

    ): LiveData<getAllPostResponse?> {

        getmyPostsMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getMyPosts(
                client_number,
                device_number,
                accessToken,
                "my_post",
                page,
                perPage
            )!!.enqueue(object : Callback<getAllPostResponse?> {
                override fun onResponse(
                    call: Call<getAllPostResponse?>,
                    response: Response<getAllPostResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getmyPostsMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError21(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getAllPostResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError21(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getmyPostsMutableLiveData1!!
    }


    private fun handleError21(code: Int, backendMessage: String?) {
        var getAllPostResponse = getAllPostResponse()
        getAllPostResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getAllPostResponse.message = errorMessage
        Log.e("API Error", getAllPostResponse.message!!)
        getmyPostsMutableLiveData1!!.postValue(getAllPostResponse)
    }


    var editPostMutableLiveData: MutableLiveData<EditPostResponse?>? = null

    fun EditPostLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        content: String
    ): LiveData<EditPostResponse?> {

        editPostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.EditPost(client_number, device_number, accessToken, identifier, content)
                .enqueue(object : Callback<EditPostResponse?> {
                    override fun onResponse(
                        call: Call<EditPostResponse?>,
                        response: Response<EditPostResponse?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {


                            editPostMutableLiveData!!.postValue(response.body())

                            if (response.isSuccessful) {

                                CommonUtils.toast(activity, "Post Edited Successfully")
                                activity.onBackPressed()
                            } else {
                                CommonUtils.toast(activity, "Post not Edited Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError23(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<EditPostResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError23(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError23(0, "No internet connection.")
        }
        return editPostMutableLiveData!!
    }

    private fun
            handleError23(code: Int, backendMessage: String?) {
        var editPostResponse = EditPostResponse()
        editPostResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        editPostResponse.message = errorMessage
        Log.e("API Error", editPostResponse.message!!)
        editPostMutableLiveData!!.postValue(editPostResponse)
    }


    var postCommentMutableLiveData: MutableLiveData<PostComment?>? = null

    fun CreateCommentLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postId: String,
        content: String

    ): LiveData<PostComment?> {

        postCommentMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.postComment(client_number, device_number, accessToken, postId, content)
                .enqueue(object : Callback<PostComment?> {
                    override fun onResponse(
                        call: Call<PostComment?>,
                        response: Response<PostComment?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postCommentMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Comment Created Successfully")
                                activity.onBackPressed()
                            } else {
                                CommonUtils.toast(activity, "Comment not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError22(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<PostComment?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError22(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError22(0, "No internet connection.")
        }
        return postCommentMutableLiveData!!
    }

    private fun handleError22(code: Int, backendMessage: String?) {
        var postComment = PostComment()
        postComment.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        postComment.message = errorMessage
        Log.e("API Error", postComment.message!!)
        postCommentMutableLiveData!!.postValue(postComment)
    }

    var deletePostMutableLiveData: MutableLiveData<DeletePostResponse?>? = null

    fun deletePostLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
    ): LiveData<DeletePostResponse?> {

        deletePostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //    CommonUtils.showProgress(activity)
            apiInterface.deletePost(client_number, device_number, accessToken, identifier)
                .enqueue(object : Callback<DeletePostResponse?> {
                    override fun onResponse(
                        call: Call<DeletePostResponse?>,
                        response: Response<DeletePostResponse?>
                    ) {
                        // CommonUtils.dismissProgress()

                        if (response.isSuccessful) {
                            deletePostMutableLiveData!!.postValue(response.body())
                            CommonUtils.toast(activity, "Post Deleted Successfully")
                        } else {
                            handleError24(response.code(), response.errorBody()?.string())
                        }
                    }

                    override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError24(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError24(0, "No internet connection.")
        }
        return deletePostMutableLiveData!!
    }

    private fun handleError24(code: Int, backendMessage: String?) {
        var deletePostResponse = DeletePostResponse()
        deletePostResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        deletePostResponse.message = errorMessage
        Log.e("API Error", deletePostResponse.message!!)
        deletePostMutableLiveData!!.postValue(deletePostResponse)
    }

    var getReasonsMutableLiveData1: MutableLiveData<ReportReasonresponse?>? = null
    fun getReasonsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,

        ): LiveData<ReportReasonresponse?> {

        getReasonsMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getReasons(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<ReportReasonresponse?> {
                    override fun onResponse(
                        call: Call<ReportReasonresponse?>,
                        response: Response<ReportReasonresponse?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getReasonsMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError25(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<ReportReasonresponse?>, t: Throwable) {
                        //  CommonUtils.dismissProgress()
                        handleError25(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getReasonsMutableLiveData1!!
    }


    private fun handleError25(code: Int, backendMessage: String?) {
        var responseReason = ReportReasonresponse()
        responseReason.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        responseReason.message = errorMessage
        Log.e("API Error", responseReason.message!!)
        getReasonsMutableLiveData1!!.postValue(responseReason)
    }

    var reportPostMutableLiveData: MutableLiveData<ReportResponse?>? = null

    fun ReportResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postidentifier: String,
        contentKey: String,


        ): LiveData<ReportResponse?> {

        reportPostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.reportPost(
                client_number,
                device_number,
                accessToken,
                postidentifier,
                contentKey
            ).enqueue(object : Callback<ReportResponse?> {
                override fun onResponse(
                    call: Call<ReportResponse?>,
                    response: Response<ReportResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        reportPostMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError26(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<ReportResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError26(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError26(0, "No internet connection.")
        }
        return reportPostMutableLiveData!!
    }

    private fun handleError26(code: Int, backendMessage: String?) {
        var reportResponse = ReportResponse()
        reportResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        reportResponse.message = errorMessage
        Log.e("API Error", reportResponse.message!!)
        reportPostMutableLiveData!!.postValue(reportResponse)
    }


    var getAllCommentsMutableLiveData1: MutableLiveData<getAllComments?>? = null
    fun getAllCommentLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        dataPerPage: Int,
        presentPage: Int,

        ): LiveData<getAllComments?> {

        getAllCommentsMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getAllComments(
                client_number,
                device_number,
                accessToken,
                identifier,
                dataPerPage,
                presentPage
            )!!.enqueue(object : Callback<getAllComments?> {
                override fun onResponse(
                    call: Call<getAllComments?>,
                    response: Response<getAllComments?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getAllCommentsMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError27(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getAllComments?>, t: Throwable) {
                    //  CommonUtils.dismissProgress()
                    handleError27(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getAllCommentsMutableLiveData1!!
    }


    private fun handleError27(code: Int, backendMessage: String?) {
        var getAllcommentsReason = getAllComments()
        getAllcommentsReason.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getAllcommentsReason.message = errorMessage
        Log.e("API Error", getAllcommentsReason.message!!)
        getAllCommentsMutableLiveData1!!.postValue(getAllcommentsReason)
    }

    var getAllCommentsRepliesMutableLiveData1: MutableLiveData<getCommentReplies?>? = null
    fun getAllCommentRepliesLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postIdentifier: String,
        commentIdentifier: String,


        ): LiveData<getCommentReplies?> {

        getAllCommentsRepliesMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getAllCommentsReplies(
                client_number,
                device_number,
                accessToken,
                postIdentifier,
                commentIdentifier
            )!!.enqueue(object : Callback<getCommentReplies?> {
                override fun onResponse(
                    call: Call<getCommentReplies?>,
                    response: Response<getCommentReplies?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getAllCommentsRepliesMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError28(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getCommentReplies?>, t: Throwable) {
                    //  CommonUtils.dismissProgress()
                    handleError28(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getAllCommentsRepliesMutableLiveData1!!
    }


    private fun handleError28(code: Int, backendMessage: String?) {
        var getAllcommentsReplies = getCommentReplies()
        getAllcommentsReplies.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getAllcommentsReplies.message = errorMessage
        Log.e("API Error", getAllcommentsReplies.message!!)
        getAllCommentsRepliesMutableLiveData1!!.postValue(getAllcommentsReplies)
    }


    var likePostMutableLiveData: MutableLiveData<LikeResponse?>? = null
    fun likeResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postIdentifier: String,
        likeKey: String
    ): LiveData<LikeResponse?> {

        likePostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //  CommonUtils.showProgress(activity)
            apiInterface.like(client_number, device_number, accessToken, postIdentifier, likeKey)
                .enqueue(object : Callback<LikeResponse?> {
                    override fun onResponse(
                        call: Call<LikeResponse?>,
                        response: Response<LikeResponse?>
                    ) {
                        //  CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            likePostMutableLiveData!!.postValue(response.body())


                        } else {
                            val apiError = parseError(response)
                            handleError29(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<LikeResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError29(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError29(0, "No internet connection.")
        }
        return likePostMutableLiveData!!
    }

    private fun handleError29(code: Int, backendMessage: String?) {
        var likeResponse = LikeResponse()
        likeResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        likeResponse.message = errorMessage
        Log.e("API Error", likeResponse.message!!)
        likePostMutableLiveData!!.postValue(likeResponse)
    }

    var reactionPostMutableLiveData: MutableLiveData<REactionResponse?>? = null
    fun reactionResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postIdentifier: String,
        tabType: String
    ): LiveData<REactionResponse?> {

        reactionPostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getReactions(
                client_number,
                device_number,
                accessToken,
                postIdentifier,
                tabType
            )!!.enqueue(object : Callback<REactionResponse?> {
                override fun onResponse(
                    call: Call<REactionResponse?>,
                    response: Response<REactionResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        reactionPostMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError30(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<REactionResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError30(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError30(0, "No internet connection.")
        }
        return reactionPostMutableLiveData!!
    }

    private fun handleError30(code: Int, backendMessage: String?) {
        var reactionResponse = REactionResponse()
        reactionResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        reactionResponse.message = errorMessage
        Log.e("API Error", reactionResponse.message!!)
        reactionPostMutableLiveData!!.postValue(reactionResponse)
    }

    var postReplyMutableLiveData: MutableLiveData<ReplyComment?>? = null

    fun CreateReplyLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postId: String,
        content: String

    ): LiveData<ReplyComment?> {

        postReplyMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.postReply(client_number, device_number, accessToken, postId, content)
                .enqueue(object : Callback<ReplyComment?> {
                    override fun onResponse(
                        call: Call<ReplyComment?>,
                        response: Response<ReplyComment?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postReplyMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Reply Created Successfully")
                                activity.onBackPressed()
                            } else {
                                CommonUtils.toast(activity, "Reply not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError31(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<ReplyComment?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError31(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError31(0, "No internet connection.")
        }
        return postReplyMutableLiveData!!
    }

    private fun handleError31(code: Int, backendMessage: String?) {
        var postComment = ReplyComment()
        postComment.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        postComment.message = errorMessage
        Log.e("API Error", postComment.message!!)
        postReplyMutableLiveData!!.postValue(postComment)
    }

    var editCommentMutableLiveData: MutableLiveData<EditCommentResponse?>? = null

    fun EditCommentLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        PostIdentifier: String,
        CommentIdentifier: String,
        content: String
    ): LiveData<EditCommentResponse?> {

        editCommentMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.EditComment(
                client_number,
                device_number,
                accessToken,
                PostIdentifier,
                CommentIdentifier,
                content
            ).enqueue(object : Callback<EditCommentResponse?> {
                override fun onResponse(
                    call: Call<EditCommentResponse?>,
                    response: Response<EditCommentResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {


                        editCommentMutableLiveData!!.postValue(response.body())

                        if (response.isSuccessful) {

                            CommonUtils.toast(activity, "comment Edited Successfully")
                            activity.onBackPressed()
                        } else {
                            CommonUtils.toast(activity, "comment not Edited Successfully")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError32(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<EditCommentResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError32(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError32(0, "No internet connection.")
        }
        return editCommentMutableLiveData!!
    }

    private fun
            handleError32(code: Int, backendMessage: String?) {
        var editCommentResponse = EditCommentResponse()
        editCommentResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        editCommentResponse.message = errorMessage
        Log.e("API Error", editCommentResponse.message!!)
        editCommentMutableLiveData!!.postValue(editCommentResponse)
    }


    var deleteCommentMutableLiveData: MutableLiveData<DeleteCommentResponse?>? = null

    fun deleteCommentLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postIdentifier: String,
        commentIdentifier: String,
    ): LiveData<DeleteCommentResponse?> {

        deleteCommentMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.deleteComment(
                client_number,
                device_number,
                accessToken,
                postIdentifier,
                commentIdentifier
            ).enqueue(object : Callback<DeleteCommentResponse?> {
                override fun onResponse(
                    call: Call<DeleteCommentResponse?>,
                    response: Response<DeleteCommentResponse?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful) {
                        deleteCommentMutableLiveData!!.postValue(response.body())
                        CommonUtils.toast(activity, "Comment Deleted Successfully")
                    } else {
                        handleError33(response.code(), response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<DeleteCommentResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError33(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError33(0, "No internet connection.")
        }
        return deleteCommentMutableLiveData!!
    }

    private fun handleError33(code: Int, backendMessage: String?) {
        var deleteCommentResponse = DeleteCommentResponse()
        deleteCommentResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        deleteCommentResponse.message = errorMessage
        Log.e("API Error", deleteCommentResponse.message!!)
        deleteCommentMutableLiveData!!.postValue(deleteCommentResponse)
    }

    var editReplyMutableLiveData: MutableLiveData<EditReplyResponse?>? = null

    fun EditReplyLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        PostIdentifier: String,
        ReplyIdentifier: String,
        content: String
    ): LiveData<EditReplyResponse?> {

        editReplyMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.EditReply(
                client_number,
                device_number,
                accessToken,
                PostIdentifier,
                ReplyIdentifier,
                content
            ).enqueue(object : Callback<EditReplyResponse?> {
                override fun onResponse(
                    call: Call<EditReplyResponse?>,
                    response: Response<EditReplyResponse?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {


                        editReplyMutableLiveData!!.postValue(response.body())

                        if (response.isSuccessful) {

                            CommonUtils.toast(activity, "reply Edited Successfully")
                            activity.onBackPressed()
                        } else {
                            CommonUtils.toast(activity, "reply not Edited Successfully")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError34(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<EditReplyResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError34(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError34(0, "No internet connection.")
        }
        return editReplyMutableLiveData!!
    }

    private fun handleError34(code: Int, backendMessage: String?) {
        var editReplyResponse = EditReplyResponse()
        editReplyResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        editReplyResponse.message = errorMessage
        Log.e("API Error", editReplyResponse.message!!)
        editReplyMutableLiveData!!.postValue(editReplyResponse)
    }

    var deleteReplyMutableLiveData: MutableLiveData<DeleteReplyResponse?>? = null

    fun deleteReplyLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        postIdentifier: String,
        ReplyIdentifier: String,
    ): LiveData<DeleteReplyResponse?> {

        deleteReplyMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.deleteReply(
                client_number,
                device_number,
                accessToken,
                postIdentifier,
                ReplyIdentifier
            ).enqueue(object : Callback<DeleteReplyResponse?> {
                override fun onResponse(
                    call: Call<DeleteReplyResponse?>,
                    response: Response<DeleteReplyResponse?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful) {
                        deleteReplyMutableLiveData!!.postValue(response.body())
                        CommonUtils.toast(activity, "Reply Deleted Successfully")
                    } else {
                        handleError35(response.code(), response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<DeleteReplyResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError35(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError35(0, "No internet connection.")
        }
        return deleteReplyMutableLiveData!!
    }

    private fun handleError35(code: Int, backendMessage: String?) {
        var deleteReplytResponse = DeleteReplyResponse()
        deleteReplytResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        deleteReplytResponse.message = errorMessage
        Log.e("API Error", deleteReplytResponse.message!!)
        deleteReplyMutableLiveData!!.postValue(deleteReplytResponse)
    }

    var leadFormPostMutableLiveData: MutableLiveData<ApiResponseForm?>? = null

    fun leadFormResponseLiveData(
        activity: Activity?,
        client_number: String,

        ): LiveData<ApiResponseForm?> {

        leadFormPostMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)

            apiInterface.getLeadForm(
                client_number
            )!!.enqueue(object : Callback<ApiResponseForm?> {
                override fun onResponse(
                    call: Call<ApiResponseForm?>,
                    response: Response<ApiResponseForm?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful && response.body() != null) {
                        leadFormPostMutableLiveData!!.postValue(response.body())
                        CommonUtils.dismissProgress()


                    } else {
                        val apiError = parseError(response)
                        handleError36(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<ApiResponseForm?>, t: Throwable) {
                    CommonUtils.dismissProgress()

                    handleError36(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError36(0, "No internet connection.")
        }
        return leadFormPostMutableLiveData!!
    }

    private fun handleError36(code: Int, backendMessage: String?) {
        var leadFormResponse = ApiResponseForm()
        leadFormResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        leadFormResponse.message = errorMessage
        Log.e("API Error", leadFormResponse.message!!)
        leadFormPostMutableLiveData!!.postValue(leadFormResponse)
    }

    // agent signup

    var getFormMutableLiveData1: MutableLiveData<formAllFieldResponse?>? = null


    fun getCountryList(
        activity: Activity?,
        fullUrl: String,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<formAllFieldResponse?> {
        val countryListLiveData = MutableLiveData<formAllFieldResponse?>()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface2.getAllFields(fullUrl, client_number, device_number, accessToken)!!
                .enqueue(object : Callback<formAllFieldResponse?> {
                    override fun onResponse(
                        call: Call<formAllFieldResponse?>,
                        response: Response<formAllFieldResponse?>,
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            countryListLiveData.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError37(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<formAllFieldResponse?>, t: Throwable) {
                        handleError37(0, "Network error: " + t.message)
                    }
                })
        } else {
            handleError37(0, "No internet connection.")
        }
        return countryListLiveData
    }

    private fun handleError37(code: Int, backendMessage: String?) {
        val formAllFieldResponse = formAllFieldResponse()
        formAllFieldResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        formAllFieldResponse.message = errorMessage
        Log.e("API Error", formAllFieldResponse.message!!)
        getFormMutableLiveData1?.postValue(formAllFieldResponse)
    }


    var postCreateChatMessageMutableLiveData: MutableLiveData<ChatMessageResponse?>? = null

    fun CreateChatMessageLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        entity: String,
        chatIdentifier: String,
        title: String,
        content: String,
        type: String,
        has_attachments: String,
        attachments: List<ChatMessageModels.FileData>
    ): LiveData<ChatMessageResponse?> {

        postCreateChatMessageMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)

            val chatMessageRequest = ChatMessageModels.ChatMessageRequest(
                title = title,
                content = content,
                type = type,
                has_attachments = has_attachments,
                attachments = attachments
            )

            apiInterface.postChatMessage(
                client_number,
                device_number,
                accessToken,
                entity,
                chatIdentifier,
                chatMessageRequest
            )
                .enqueue(object : Callback<ChatMessageResponse?> {
                    override fun onResponse(
                        call: Call<ChatMessageResponse?>,
                        response: Response<ChatMessageResponse?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postCreateChatMessageMutableLiveData!!.postValue(response.body())

                            if (response.body()!!.statusCode == 201) {
                            } else {
                            }
                        } else {
                            val apiError = parseError(response)
                            handleError38(response.code(), getErrorMessage(apiError), activity)
                        }
                    }

                    override fun onFailure(call: Call<ChatMessageResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError38(0, "Network error: " + t.message, activity)
                    }
                })
        } else {

            handleError38(0, "No internet connection.", activity)
        }
        return postCreateChatMessageMutableLiveData!!
    }


    private fun handleError38(code: Int, backendMessage: String?, activity: Activity?) {
        val chatMessageResponse = ChatMessageResponse()
        chatMessageResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> "File doesn't exist"
            else -> backendMessage ?: "Error $code"
        }
        chatMessageResponse.message = errorMessage
        Log.e("API Error", chatMessageResponse.message!!)
        postCreateChatMessageMutableLiveData!!.postValue(chatMessageResponse)

        // Show toast with the error message
        activity?.let {
            Toast.makeText(it, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    // get_application_attachment


    var getViewAttachmentsModalClass: MutableLiveData<getApplicationDocuments?>? = null

    fun getViewAttachmentsModalClassData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        chatIdentifier: String,
        entity: String,
    ): LiveData<getApplicationDocuments?> {

        getViewAttachmentsModalClass = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getChatAttachments(
                client_number,
                device_number,
                accessToken,
                chatIdentifier,
                entity

            )!!.enqueue(object : Callback<getApplicationDocuments?> {
                override fun onResponse(
                    call: Call<getApplicationDocuments?>,
                    response: Response<getApplicationDocuments?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getViewAttachmentsModalClass!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleErrorgetViewAttachmentsModalClass(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }


                override fun onFailure(call: Call<getApplicationDocuments?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleErrorgetViewAttachmentsModalClass(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorgetViewAttachmentsModalClass(0, "No internet connection.")
        }
        return getViewAttachmentsModalClass!!
    }

    private fun handleErrorgetViewAttachmentsModalClass(code: Int, backendMessage: String?) {
        var getChatResponse = getApplicationDocuments()
        getChatResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getChatResponse.message = errorMessage
        Log.e("API Error", getChatResponse.message!!)
        getViewAttachmentsModalClass!!.postValue(getChatResponse)
    }


    ///

    var getLeadMutableLiveData: MutableLiveData<getLeadResponse?>? = null
    fun getLeadResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        perPage: Int,
        category: String,
        stage: String,
        sort: String
    ): LiveData<getLeadResponse?> {

        getLeadMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeads(
                client_number,
                device_number,
                accessToken,
                page, perPage, category, stage, sort
            )!!.enqueue(object : Callback<getLeadResponse?> {
                override fun onResponse(
                    call: Call<getLeadResponse?>,
                    response: Response<getLeadResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError40(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError40(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError40(0, "No internet connection.")
        }
        return getLeadMutableLiveData!!
    }

    private fun handleError40(code: Int, backendMessage: String?) {
        var getLeadResponse = getLeadResponse()
        getLeadResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadResponse.message = errorMessage
        Log.e("API Error", getLeadResponse.message!!)
        getLeadMutableLiveData!!.postValue(getLeadResponse)
    }

    var getLeadTimelineresponseMutableLiveData: MutableLiveData<getLeadTimelineResponse?>? = null
    fun getLeadTimelineResponseResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getLeadTimelineResponse?> {

        getLeadTimelineresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadTimeLine(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getLeadTimelineResponse?> {
                override fun onResponse(
                    call: Call<getLeadTimelineResponse?>,
                    response: Response<getLeadTimelineResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadTimelineresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError41(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadTimelineResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError41(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError41(0, "No internet connection.")
        }
        return getLeadTimelineresponseMutableLiveData!!
    }

    private fun handleError41(code: Int, backendMessage: String?) {
        var getLeadTimelineResponse = getLeadTimelineResponse()
        getLeadTimelineResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadTimelineResponse.message = errorMessage
        Log.e("API Error", getLeadTimelineResponse.message!!)
        getLeadTimelineresponseMutableLiveData!!.postValue(getLeadTimelineResponse)
    }

    var getLeadAssignedStaffresponseMutableLiveData: MutableLiveData<getApplicationAssignedStaff?>? =
        null

    fun getApplicationAssignedStaffResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getApplicationAssignedStaff?> {

        getLeadAssignedStaffresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadAssignedStaff(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getApplicationAssignedStaff?> {
                override fun onResponse(
                    call: Call<getApplicationAssignedStaff?>,
                    response: Response<getApplicationAssignedStaff?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadAssignedStaffresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError42(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationAssignedStaff?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError42(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError42(0, "No internet connection.")
        }
        return getLeadAssignedStaffresponseMutableLiveData!!
    }

    private fun handleError42(code: Int, backendMessage: String?) {
        var getLeadAssignedStaffResponse = getApplicationAssignedStaff()
        getLeadAssignedStaffResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadAssignedStaffResponse.message = errorMessage
        Log.e("API Error", getLeadAssignedStaffResponse.message!!)
        getLeadAssignedStaffresponseMutableLiveData!!.postValue(getLeadAssignedStaffResponse)
    }


    var getLeadreminderresponseMutableLiveData: MutableLiveData<GetLeadReminderResponse?>? = null

    fun getLeadReminderResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        type: String,
        category: String,
        identifier: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String
    ): LiveData<GetLeadReminderResponse?> {

        getLeadreminderresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getLeadReminder(
                client_number,
                device_number,
                accessToken,
                type,
                category,
                identifier,
                page,
                per_page,
                sort,
                sortBy
            )!!.enqueue(object : Callback<GetLeadReminderResponse?> {
                override fun onResponse(
                    call: Call<GetLeadReminderResponse?>,
                    response: Response<GetLeadReminderResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getLeadreminderresponseMutableLiveData!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError43(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<GetLeadReminderResponse?>, t: Throwable) {
                    handleError43(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError43(0, "No internet connection.")
        }
        return getLeadreminderresponseMutableLiveData!!
    }

    private fun handleError43(code: Int, backendMessage: String?) {
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        Log.e("API Error", errorMessage)
    }


    var postReminderMutableLiveData: MutableLiveData<postLeadReminder?>? = null

    fun postReminderLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        moduleType: String,
        moduleIdentifier: String,
        scheduledAt: String,
        note: String
    ): LiveData<postLeadReminder?> {

        postReminderMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.postReminder(
                client_number,
                device_number,
                accessToken,
                moduleType,
                moduleIdentifier,
                scheduledAt,
                note
            )
                .enqueue(object : Callback<postLeadReminder?> {
                    override fun onResponse(
                        call: Call<postLeadReminder?>,
                        response: Response<postLeadReminder?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postReminderMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, " Reminder Created Successfully")
                            } else {
                                CommonUtils.toast(activity, "Reminder not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError44(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<postLeadReminder?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError44(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError44(0, "No internet connection.")
        }
        return postReminderMutableLiveData!!
    }

    private fun handleError44(code: Int, backendMessage: String?) {
        var postLeadReminder = postLeadReminder()
        postLeadReminder.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        postLeadReminder.message = errorMessage
        Log.e("API Error", postLeadReminder.message!!)
        postReminderMutableLiveData!!.postValue(postLeadReminder)
    }


    var getLeadNotesresponseMutableLiveData: MutableLiveData<getLeadNotesResponse?>? = null
    fun getLeadNotesResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String

    ): LiveData<getLeadNotesResponse?> {

        getLeadNotesresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadNotes(
                client_number,
                device_number,
                accessToken,
                identifier,
                page,
                per_page,
                sort,
                sortBy

            )!!.enqueue(object : Callback<getLeadNotesResponse?> {
                override fun onResponse(
                    call: Call<getLeadNotesResponse?>,
                    response: Response<getLeadNotesResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadNotesresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError45(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadNotesResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError45(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError45(0, "No internet connection.")
        }
        return getLeadNotesresponseMutableLiveData!!
    }

    private fun handleError45(code: Int, backendMessage: String?) {
        var getLeadNotesResponse = getLeadNotesResponse()
        getLeadNotesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadNotesResponse.message = errorMessage
        Log.e("API Error", getLeadNotesResponse.message!!)
        getLeadNotesresponseMutableLiveData!!.postValue(getLeadNotesResponse)
    }

    var getLeadDocumentsresponseMutableLiveData: MutableLiveData<getLeadsDocuments?>? = null
    fun getLeadDocumentsResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String

    ): LiveData<getLeadsDocuments?> {

        getLeadDocumentsresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadDocuments(
                client_number,
                device_number,
                accessToken,
                identifier,
                page,
                per_page,
                sort,
                sortBy

            )!!.enqueue(object : Callback<getLeadsDocuments?> {
                override fun onResponse(
                    call: Call<getLeadsDocuments?>,
                    response: Response<getLeadsDocuments?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadDocumentsresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError46(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadsDocuments?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError46(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError46(0, "No internet connection.")
        }
        return getLeadDocumentsresponseMutableLiveData!!
    }

    private fun handleError46(code: Int, backendMessage: String?) {
        var getLeadsDocuments = getLeadsDocuments()
        getLeadsDocuments.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadsDocuments.message = errorMessage
        Log.e("API Error", getLeadsDocuments.message!!)
        getLeadDocumentsresponseMutableLiveData!!.postValue(getLeadsDocuments)
    }

    var getLeadShorlistedProgramresponseMutableLiveData: MutableLiveData<getLeadShortlistedProgram?>? =
        null

    fun getLeadShorlistedProgramResponseResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getLeadShortlistedProgram?> {

        getLeadShorlistedProgramresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadShortlistedProgram(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getLeadShortlistedProgram?> {
                override fun onResponse(
                    call: Call<getLeadShortlistedProgram?>,
                    response: Response<getLeadShortlistedProgram?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadShorlistedProgramresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError47(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadShortlistedProgram?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError47(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError47(0, "No internet connection.")
        }
        return getLeadShorlistedProgramresponseMutableLiveData!!
    }

    private fun handleError47(code: Int, backendMessage: String?) {
        var getLeadShortlistedProgramResponse = getLeadShortlistedProgram()
        getLeadShortlistedProgramResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadShortlistedProgramResponse.message = errorMessage
        Log.e("API Error", getLeadShortlistedProgramResponse.message!!)
        getLeadShorlistedProgramresponseMutableLiveData!!.postValue(
            getLeadShortlistedProgramResponse
        )
    }


    var getLeadCounsellingresponseMutableLiveData: MutableLiveData<getLeadCounsellings?>? = null
    fun getLeadCounsellingResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getLeadCounsellings?> {

        getLeadCounsellingresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getLeadCounselling(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getLeadCounsellings?> {
                override fun onResponse(
                    call: Call<getLeadCounsellings?>,
                    response: Response<getLeadCounsellings?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadCounsellingresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError48(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadCounsellings?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError48(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError48(0, "No internet connection.")
        }
        return getLeadCounsellingresponseMutableLiveData!!
    }

    private fun handleError48(code: Int, backendMessage: String?) {
        var getLeadCounsellingResponse = getLeadCounsellings()
        getLeadCounsellingResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadCounsellingResponse.message = errorMessage
        Log.e("API Error", getLeadCounsellingResponse.message!!)
        getLeadCounsellingresponseMutableLiveData!!.postValue(getLeadCounsellingResponse)
    }

    var getLeadPaymentLinksresponseMutableLiveData: MutableLiveData<getLeadPaymentLinks?>? = null
    fun getLeadPaymentLinksResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getLeadPaymentLinks?> {

        getLeadPaymentLinksresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getLeadPaymentLinks(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getLeadPaymentLinks?> {
                override fun onResponse(
                    call: Call<getLeadPaymentLinks?>,
                    response: Response<getLeadPaymentLinks?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadPaymentLinksresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError49(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getLeadPaymentLinks?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError49(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError49(0, "No internet connection.")
        }
        return getLeadPaymentLinksresponseMutableLiveData!!
    }

    private fun handleError49(code: Int, backendMessage: String?) {
        var getLeadPaymentLinksResponse = getLeadPaymentLinks()
        getLeadPaymentLinksResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadPaymentLinksResponse.message = errorMessage
        Log.e("API Error", getLeadPaymentLinksResponse.message!!)
        getLeadPaymentLinksresponseMutableLiveData!!.postValue(getLeadPaymentLinksResponse)
    }

    var getApplicationresponseMutableLiveData: MutableLiveData<getApplicationResponse?>? = null
    fun getApplicationResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String,
        lead_identifier: String?

    ): LiveData<getApplicationResponse?> {

        getApplicationresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplications(
                client_number,
                device_number,
                accessToken,
                page,
                per_page,
                sort,
                sortBy,
                lead_identifier

            )!!.enqueue(object : Callback<getApplicationResponse?> {
                override fun onResponse(
                    call: Call<getApplicationResponse?>,
                    response: Response<getApplicationResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError50(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError50(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError50(0, "No internet connection.")
        }
        return getApplicationresponseMutableLiveData!!
    }

    private fun handleError50(code: Int, backendMessage: String?) {
        var getApplicationResponse = getApplicationResponse()
        getApplicationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationResponse.message = errorMessage
        Log.e("API Error", getApplicationResponse.message!!)
        getApplicationresponseMutableLiveData!!.postValue(getApplicationResponse)
    }


    // application payment details


    var getApplicationresponsePaymentDeailsMutableLiveData: MutableLiveData<ApplicationPaymentDetails?>? =
        null

    fun getApplicationResponsePaymentDetailsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String,
        search: String

    ): LiveData<ApplicationPaymentDetails?> {

        getApplicationresponsePaymentDeailsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationsPaymentDetails(
                client_number,
                device_number,
                accessToken,
                page,
                per_page,
                sort,
                sortBy,
                search

            )!!.enqueue(object : Callback<ApplicationPaymentDetails?> {
                override fun onResponse(
                    call: Call<ApplicationPaymentDetails?>,
                    response: Response<ApplicationPaymentDetails?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationresponsePaymentDeailsMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError550(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<ApplicationPaymentDetails?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError550(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError550(0, "No internet connection.")
        }
        return getApplicationresponsePaymentDeailsMutableLiveData!!
    }

    private fun handleError550(code: Int, backendMessage: String?) {
        var getApplicationResponse = ApplicationPaymentDetails()
        getApplicationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationResponse.message = errorMessage
        getApplicationresponsePaymentDeailsMutableLiveData!!.postValue(getApplicationResponse)
    }


    //
    var getApplicationAssignedStaffresponseMutableLiveData: MutableLiveData<getApplicationAssignedStaff?>? =
        null

    fun getApplicationAssignedStaffResponseResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        leadIdentifier: String
    ): LiveData<getApplicationAssignedStaff?> {

        getApplicationAssignedStaffresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationAssignedStaff(
                client_number,
                device_number,
                accessToken,
                leadIdentifier
            )!!.enqueue(object : Callback<getApplicationAssignedStaff?> {
                override fun onResponse(
                    call: Call<getApplicationAssignedStaff?>,
                    response: Response<getApplicationAssignedStaff?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationAssignedStaffresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError51(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationAssignedStaff?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError51(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError51(0, "No internet connection.")
        }
        return getApplicationAssignedStaffresponseMutableLiveData!!
    }

    private fun handleError51(code: Int, backendMessage: String?) {
        var getApplicationAssignedStaff = getApplicationAssignedStaff()
        getApplicationAssignedStaff.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationAssignedStaff.message = errorMessage
        Log.e("API Error", getApplicationAssignedStaff.message!!)
        getApplicationAssignedStaffresponseMutableLiveData!!.postValue(getApplicationAssignedStaff)
    }

    var getApplicationTimelineresponseMutableLiveData: MutableLiveData<getApplicationTimelineResponse?>? =
        null

    fun getApplicationTimelineResponseResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        applicationIdentifier: String
    ): LiveData<getApplicationTimelineResponse?> {

        getApplicationTimelineresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationTimeLine(
                client_number,
                device_number,
                accessToken,
                applicationIdentifier
            )!!.enqueue(object : Callback<getApplicationTimelineResponse?> {
                override fun onResponse(
                    call: Call<getApplicationTimelineResponse?>,
                    response: Response<getApplicationTimelineResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationTimelineresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError52(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationTimelineResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError52(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError52(0, "No internet connection.")
        }
        return getApplicationTimelineresponseMutableLiveData!!
    }

    private fun handleError52(code: Int, backendMessage: String?) {
        var getApplicationTimelineResponse = getApplicationTimelineResponse()
        getApplicationTimelineResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationTimelineResponse.message = errorMessage
        Log.e("API Error", getApplicationTimelineResponse.message!!)
        getApplicationTimelineresponseMutableLiveData!!.postValue(getApplicationTimelineResponse)
    }

    var getApplicationNotesresponseMutableLiveData: MutableLiveData<getApplicationNotes?>? = null
    fun getApplicationNotesResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String

    ): LiveData<getApplicationNotes?> {

        getApplicationNotesresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationNotes(
                client_number,
                device_number,
                accessToken,
                identifier,
                page,
                per_page,
                sort,
                sortBy

            )!!.enqueue(object : Callback<getApplicationNotes?> {
                override fun onResponse(
                    call: Call<getApplicationNotes?>,
                    response: Response<getApplicationNotes?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationNotesresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError53(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationNotes?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError53(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError53(0, "No internet connection.")
        }
        return getApplicationNotesresponseMutableLiveData!!
    }

    private fun handleError53(code: Int, backendMessage: String?) {
        var getApplicationNotesResponse = getApplicationNotes()
        getApplicationNotesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationNotesResponse.message = errorMessage
        Log.e("API Error", getApplicationNotesResponse.message!!)
        getApplicationNotesresponseMutableLiveData!!.postValue(getApplicationNotesResponse)
    }

    var posLeadNotesMutableLiveData: MutableLiveData<postLeadNotesResponse?>? = null

    fun postLeadNotesLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        content: String
    ): LiveData<postLeadNotesResponse?> {

        posLeadNotesMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.postLeadNotes(
                client_number,
                device_number,
                accessToken,
                identifier,
                content
            )
                .enqueue(object : Callback<postLeadNotesResponse?> {
                    override fun onResponse(
                        call: Call<postLeadNotesResponse?>,
                        response: Response<postLeadNotesResponse?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            posLeadNotesMutableLiveData!!.postValue(response.body())

                            if (response.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Notes Created Successfully")


                            } else {
                                CommonUtils.toast(activity, "Notes not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError54(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<postLeadNotesResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError54(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError54(0, "No internet connection.")
        }
        return posLeadNotesMutableLiveData!!
    }

    private fun handleError54(code: Int, backendMessage: String?) {
        var postLeadNotesResponse = postLeadNotesResponse()
        postLeadNotesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        postLeadNotesResponse.message = errorMessage
        Log.e("API Error", postLeadNotesResponse.message!!)
        posLeadNotesMutableLiveData!!.postValue(postLeadNotesResponse)
    }

    private fun handleError56(code: Int, backendMessage: String?) {
        var PostApplicationNotesResponse = PostApplicationNotes()
        PostApplicationNotesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        PostApplicationNotesResponse.message = errorMessage
        Log.e("API Error", PostApplicationNotesResponse.message!!)
        postApplicationNotesMutableLiveData!!.postValue(PostApplicationNotesResponse)
    }

    var getApplicationreminderresponseMutableLiveData: MutableLiveData<getApplicationReminderResponse?>? =
        null

    fun getApplicationReminderResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        type: String,
        category: String,
        identifier: String,
        page: Int,
        per_page: Int,
        sort: String,
        sortBy: String

    ): LiveData<getApplicationReminderResponse?> {

        getApplicationreminderresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationReminder(
                client_number,
                device_number,
                accessToken,
                type,
                category,
                identifier,
                page,
                per_page,
                sort,
                sortBy

            )!!.enqueue(object : Callback<getApplicationReminderResponse?> {
                override fun onResponse(
                    call: Call<getApplicationReminderResponse?>,
                    response: Response<getApplicationReminderResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationreminderresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError57(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationReminderResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError57(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError57(0, "No internet connection.")
        }
        return getApplicationreminderresponseMutableLiveData!!
    }

    private fun handleError57(code: Int, backendMessage: String?) {
        var getApplicationReminderResponse = getApplicationReminderResponse()
        getApplicationReminderResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationReminderResponse.message = errorMessage
        Log.e("API Error", getApplicationReminderResponse.message!!)
        getApplicationreminderresponseMutableLiveData!!.postValue(getApplicationReminderResponse)
    }

    var getDocumentsresponseMutableLiveData: MutableLiveData<getApplicationDocuments?>? = null


    fun getDocumentsResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,

        ): LiveData<getApplicationDocuments?> {

        getDocumentsresponseMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getApplicationDocuments(
                client_number,
                device_number,
                accessToken,
                identifier


            )!!.enqueue(object : Callback<getApplicationDocuments?> {
                override fun onResponse(
                    call: Call<getApplicationDocuments?>,
                    response: Response<getApplicationDocuments?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getDocumentsresponseMutableLiveData!!.postValue(response.body())


                    } else {
                        val apiError = parseError(response)
                        handleError58(response.code(), getErrorMessage(apiError))
                    }
                }


                override fun onFailure(call: Call<getApplicationDocuments?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError58(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError58(0, "No internet connection.")
        }
        return getDocumentsresponseMutableLiveData!!
    }

    private fun handleError58(code: Int, backendMessage: String?) {
        var getApplicationDocuments = getApplicationDocuments()
        getApplicationDocuments.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getApplicationDocuments.message = errorMessage
        Log.e("API Error", getApplicationDocuments.message!!)
        getDocumentsresponseMutableLiveData!!.postValue(getApplicationDocuments)
    }

    var getDocumentMutableLiveData1: MutableLiveData<getDocumentTypes?>? = null
    fun getDocumentsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String

    ): LiveData<getDocumentTypes?> {

        getDocumentMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getDocumentsTypes(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<getDocumentTypes?> {
                    override fun onResponse(
                        call: Call<getDocumentTypes?>,
                        response: Response<getDocumentTypes?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getDocumentMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError59(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getDocumentTypes?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError59(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getDocumentMutableLiveData1!!
    }

    private fun handleError59(code: Int, backendMessage: String?) {
        var categoriesResponse = getDocumentTypes()
        categoriesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        categoriesResponse.message = errorMessage
        Log.e("API Error", categoriesResponse.message!!)
        getDocumentMutableLiveData1!!.postValue(categoriesResponse)
    }


    var postApplicationUploadDocumentsMutableLiveData: MutableLiveData<uploadDocuments?>? = null

    fun postApplicationUploadDocumentsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        body: MultipartBody.Part,
        type: String


    ): LiveData<uploadDocuments?> {

        postApplicationUploadDocumentsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface3.uploadDocuments(client_number, device_number, accessToken, type, body)!!
                .enqueue(object : Callback<uploadDocuments?> {
                    override fun onResponse(
                        call: Call<uploadDocuments?>,
                        response: Response<uploadDocuments?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postApplicationUploadDocumentsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Image Uploaded Successfully")

                            } else {
                                CommonUtils.toast(activity, "Image not Uploaded")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError60(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<uploadDocuments?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError60(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError60(0, "No internet connection.")
        }
        return postApplicationUploadDocumentsMutableLiveData!!
    }

    private fun handleError60(code: Int, backendMessage: String?) {
        var uploadDocumentsResponse = uploadDocuments()
        uploadDocumentsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        uploadDocumentsResponse.message = errorMessage
        Log.e("API Error", uploadDocumentsResponse.message!!)
        postApplicationUploadDocumentsMutableLiveData!!.postValue(uploadDocumentsResponse)
    }

    var saveApplicationDocumentsMutableLiveData: MutableLiveData<saveApplicationDocuments?>? = null

    fun saveApplicatonDocumentsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        jsonString: SaveDocumentsRequest
    ): LiveData<saveApplicationDocuments?> {

        saveApplicationDocumentsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.saveDocuments(
                client_number,
                device_number,
                accessToken,
                identifier,
                jsonString
            )
                .enqueue(object : Callback<saveApplicationDocuments?> {
                    override fun onResponse(
                        call: Call<saveApplicationDocuments?>,
                        response: Response<saveApplicationDocuments?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            saveApplicationDocumentsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {


                            } else {
                                CommonUtils.toast(activity, "Documents not saved Successfully")
                            }

                        } else {
                            val apiError = parseError(response)
                            handleError61(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<saveApplicationDocuments?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError61(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError61(0, "No internet connection.")
        }
        return saveApplicationDocumentsMutableLiveData!!
    }

    var postApplicationNotesMutableLiveData: MutableLiveData<PostApplicationNotes?>? = null

    fun postApplicationNotesLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        content: String
    ): LiveData<PostApplicationNotes?> {

        postApplicationNotesMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.postApplicationNotes(
                client_number,
                device_number,
                accessToken,
                identifier,
                content
            )
                .enqueue(object : Callback<PostApplicationNotes?> {
                    override fun onResponse(
                        call: Call<PostApplicationNotes?>,
                        response: Response<PostApplicationNotes?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postApplicationNotesMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Notes Created Successfully")


                            } else {
                                CommonUtils.toast(activity, "Notes not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError56(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<PostApplicationNotes?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError56(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError56(0, "No internet connection.")
        }
        return postApplicationNotesMutableLiveData!!
    }

    private fun handleError61(code: Int, backendMessage: String?) {
        var saveApplicationDocumentsResponse = saveApplicationDocuments()
        saveApplicationDocumentsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        saveApplicationDocumentsResponse.message = errorMessage
        Log.e("API Error", saveApplicationDocumentsResponse.message!!)
        saveApplicationDocumentsMutableLiveData!!.postValue(saveApplicationDocumentsResponse)
    }


    var getPaymentApplicationMutableLiveData: MutableLiveData<getPaymentApplication?>? = null

    fun getPaymentApplicationLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        page: Int,
        perPage: Int,
        sort: String,
        sort_by: String

    ): LiveData<getPaymentApplication?> {

        getPaymentApplicationMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getPaymentApplication(
                client_number,
                device_number,
                accessToken,
                identifier,
                page,
                perPage,
                sort,
                sort_by
            )!!.enqueue(object : Callback<getPaymentApplication?> {
                override fun onResponse(
                    call: Call<getPaymentApplication?>,
                    response: Response<getPaymentApplication?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getPaymentApplicationMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 200) {
                            //CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError62(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getPaymentApplication?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError62(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError62(0, "No internet connection.")
        }
        return getPaymentApplicationMutableLiveData!!
    }

    private fun handleError62(code: Int, backendMessage: String?) {
        var getPaymentApplicationResponse = getPaymentApplication()
        getPaymentApplicationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationResponse.message!!)
        getPaymentApplicationMutableLiveData!!.postValue(getPaymentApplicationResponse)
    }

    var getPaymentForDropDownApplicationMutableLiveData: MutableLiveData<getPaymentForDropDown?>? =
        null

    fun getPaymentForDropDownApplicationLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,

        ): LiveData<getPaymentForDropDown?> {

        getPaymentForDropDownApplicationMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getPaymentForDropdown(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<getPaymentForDropDown?> {
                    override fun onResponse(
                        call: Call<getPaymentForDropDown?>,
                        response: Response<getPaymentForDropDown?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getPaymentForDropDownApplicationMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                //CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError63(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getPaymentForDropDown?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError63(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError63(0, "No internet connection.")
        }
        return getPaymentForDropDownApplicationMutableLiveData!!
    }

    private fun handleError63(code: Int, backendMessage: String?) {
        var getPaymentForDropDownResponse = getPaymentForDropDown()
        getPaymentForDropDownResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentForDropDownResponse.message = errorMessage
        Log.e("API Error", getPaymentForDropDownResponse.message!!)
        getPaymentForDropDownApplicationMutableLiveData!!.postValue(getPaymentForDropDownResponse)
    }

    var getModeOfPaymentDropDownApplicationMutableLiveData: MutableLiveData<getPaymentMode?>? = null

    fun getModeOFPaymentDropDownApplicationLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getPaymentMode?> {

        getModeOfPaymentDropDownApplicationMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getModeOfPaymentDropdown(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<getPaymentMode?> {
                    override fun onResponse(
                        call: Call<getPaymentMode?>,
                        response: Response<getPaymentMode?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getModeOfPaymentDropDownApplicationMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                //CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError64(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getPaymentMode?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError64(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError64(0, "No internet connection.")
        }
        return getModeOfPaymentDropDownApplicationMutableLiveData!!
    }

    private fun handleError64(code: Int, backendMessage: String?) {
        var getPaymentModeResponse = getPaymentMode()
        getPaymentModeResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentModeResponse.message = errorMessage
        Log.e("API Error", getPaymentModeResponse.message!!)
        getModeOfPaymentDropDownApplicationMutableLiveData!!.postValue(getPaymentModeResponse)
    }

    var postApplicationPaymentGeneratingLinkMutableLiveData: MutableLiveData<generatingPaymentLinkApplication?>? =
        null

    fun genratingPaymentLinkApplicationLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        module: String,
        module_identifier: String,
        price: String,
        currency: String,
        payment_type_identifier: String,
        payment_gateway_identifier: String
    ): LiveData<generatingPaymentLinkApplication?> {

        postApplicationPaymentGeneratingLinkMutableLiveData = MutableLiveData()


        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.generatingPaymentLinksApplication(
                client_number,
                device_number,
                accessToken,
                module,
                module_identifier,
                price,
                currency,
                payment_type_identifier,
                payment_gateway_identifier
            )
                .enqueue(object : Callback<generatingPaymentLinkApplication?> {
                    override fun onResponse(
                        call: Call<generatingPaymentLinkApplication?>,
                        response: Response<generatingPaymentLinkApplication?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postApplicationPaymentGeneratingLinkMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Generating Payment Link  Successfully")

                            } else {
                                CommonUtils.toast(
                                    activity,
                                    "Generating Payment Link not  Successfully"
                                )
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError65(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(
                        call: Call<generatingPaymentLinkApplication?>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissProgress()
                        handleError65(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError65(0, "No internet connection.")
        }
        return postApplicationPaymentGeneratingLinkMutableLiveData!!
    }

    private fun handleError65(code: Int, backendMessage: String?) {
        var generatingPaymentLinkApplicationResponse = generatingPaymentLinkApplication()
        generatingPaymentLinkApplicationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        generatingPaymentLinkApplicationResponse.message = errorMessage
        Log.e("API Error", generatingPaymentLinkApplicationResponse.message!!)
        postApplicationPaymentGeneratingLinkMutableLiveData!!.postValue(
            generatingPaymentLinkApplicationResponse
        )
    }

    var getCategoryLeadStatMutableLiveData: MutableLiveData<getCategoryLeadStat?>? = null

    fun getLeadCategoryStatApplicationLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        category: String,
    ): LiveData<getCategoryLeadStat?> {

        getCategoryLeadStatMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getCategoryLeadStat(client_number, device_number, accessToken, category)!!
                .enqueue(object : Callback<getCategoryLeadStat?> {
                    override fun onResponse(
                        call: Call<getCategoryLeadStat?>,
                        response: Response<getCategoryLeadStat?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCategoryLeadStatMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                //CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError66(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getCategoryLeadStat?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError66(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError66(0, "No internet connection.")
        }
        return getCategoryLeadStatMutableLiveData!!
    }

    private fun handleError66(code: Int, backendMessage: String?) {
        var getCategoryLeadStatResponse = getCategoryLeadStat()
        getCategoryLeadStatResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getCategoryLeadStatResponse.message = errorMessage
        Log.e("API Error", getCategoryLeadStatResponse.message!!)
        getCategoryLeadStatMutableLiveData!!.postValue(getCategoryLeadStatResponse)
    }


    var getReviewListMutableLiveData: MutableLiveData<getReviewList?>? = null

    fun getReviewListLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        UserIdentifier: String,
        status: String,
        page: Int,
        perPage: Int
    ): LiveData<getReviewList?> {

        getReviewListMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getReviewList(
                client_number,
                device_number,
                accessToken,
                UserIdentifier,
                status,
                page,
                perPage
            )!!.enqueue(object : Callback<getReviewList?> {
                override fun onResponse(
                    call: Call<getReviewList?>,
                    response: Response<getReviewList?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getReviewListMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError67(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getReviewList?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError67(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError67(0, "No internet connection.")
        }
        return getReviewListMutableLiveData!!
    }

    private fun handleError67(code: Int, backendMessage: String?) {
        var getReviewListResponse = getReviewList()
        getReviewListResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getReviewListResponse.message = errorMessage
        Log.e("API Error", getReviewListResponse.message!!)
        getReviewListMutableLiveData!!.postValue(getReviewListResponse)
    }

    var postStatusMutableLiveData: MutableLiveData<postLeadStatus?>? = null

    fun postStatusApplicationLiveData(
        activity: Activity,
        client_number: String,
        device_number: String,
        accessToken: String,
        Identifier: String,
        status: String

    ): LiveData<postLeadStatus?> {

        postStatusMutableLiveData = MutableLiveData()


        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getLeadStatusUpdate(
                client_number,
                device_number,
                accessToken,
                Identifier,
                status
            )
                .enqueue(object : Callback<postLeadStatus?> {
                    override fun onResponse(
                        call: Call<postLeadStatus?>,
                        response: Response<postLeadStatus?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postStatusMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Successfully")

                            } else {
                                CommonUtils.toast(activity, "Not  Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError68(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<postLeadStatus?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError68(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError68(0, "No internet connection.")
        }
        return postStatusMutableLiveData!!
    }

    private fun handleError68(code: Int, backendMessage: String?) {
        var postLeadStatusResponse = postLeadStatus()
        postLeadStatusResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        postLeadStatusResponse.message = errorMessage
        Log.e("API Error", postLeadStatusResponse.message!!)
        postStatusMutableLiveData!!.postValue(postLeadStatusResponse)
    }

    var getApplicationPayMutableLiveData: MutableLiveData<GetPaymentApplicationPay?>? = null

    fun getApplicationPayLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,

        ): LiveData<GetPaymentApplicationPay?> {

        getApplicationPayMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getPaymentApplicationPay(
                client_number,
                device_number,
                accessToken,
                identifier
            )!!.enqueue(object : Callback<GetPaymentApplicationPay?> {
                override fun onResponse(
                    call: Call<GetPaymentApplicationPay?>,
                    response: Response<GetPaymentApplicationPay?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationPayMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError69(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<GetPaymentApplicationPay?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError69(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError69(0, "No internet connection.")
        }
        return getApplicationPayMutableLiveData!!
    }

    private fun handleError69(code: Int, backendMessage: String?) {
        var getPaymentApplicationPayResponse = GetPaymentApplicationPay()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        getApplicationPayMutableLiveData!!.postValue(getPaymentApplicationPayResponse)
    }


    // lead_source
    var leadSourceModal: MutableLiveData<LeadSourceModal?>? = null

    fun getLeadSourceData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,


        ): LiveData<LeadSourceModal?> {

        leadSourceModal = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getLeadSource(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<LeadSourceModal?> {
                    override fun onResponse(
                        call: Call<LeadSourceModal?>,
                        response: Response<LeadSourceModal?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            leadSourceModal!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError70(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<LeadSourceModal?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError70(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError70(0, "No internet connection.")
        }
        return leadSourceModal!!
    }

    private fun handleError70(code: Int, backendMessage: String?) {
        val getPaymentApplicationPayResponse = LeadSourceModal()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        leadSourceModal!!.postValue(getPaymentApplicationPayResponse)
    }

    // destination_country_lead_source
    var destinationCountryModal: MutableLiveData<DestinationCountryModal?>? = null

    fun getDestinationCountryData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,


        ): LiveData<DestinationCountryModal?> {

        destinationCountryModal = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getLeadSourceDestinationCountry(
                client_number,
                device_number,
                accessToken
            )!!.enqueue(object : Callback<DestinationCountryModal?> {
                override fun onResponse(
                    call: Call<DestinationCountryModal?>,
                    response: Response<DestinationCountryModal?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        destinationCountryModal!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError71(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<DestinationCountryModal?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError71(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError71(0, "No internet connection.")
        }
        return destinationCountryModal!!
    }

    private fun handleError71(code: Int, backendMessage: String?) {
        val getPaymentApplicationPayResponse = DestinationCountryModal()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        destinationCountryModal!!.postValue(getPaymentApplicationPayResponse)
    }


    // destination_country_lead_source
    var getLeadMediumModal: MutableLiveData<DestinationCountryModal?>? = null

    fun getLeadMediumModalData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,


        ): LiveData<DestinationCountryModal?> {

        getLeadMediumModal = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getLeadMediumData(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<DestinationCountryModal?> {
                    override fun onResponse(
                        call: Call<DestinationCountryModal?>,
                        response: Response<DestinationCountryModal?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getLeadMediumModal!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError72(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<DestinationCountryModal?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError72(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError72(0, "No internet connection.")
        }
        return getLeadMediumModal!!
    }

    private fun handleError72(code: Int, backendMessage: String?) {
        val getPaymentApplicationPayResponse = DestinationCountryModal()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        getLeadMediumModal!!.postValue(getPaymentApplicationPayResponse)
    }


    // _lead_branch
    var getLeadBranchModal: MutableLiveData<DestinationCountryModal?>? = null

    fun getLeadBranchData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,


        ): LiveData<DestinationCountryModal?> {

        getLeadBranchModal = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getLeadBranchData(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<DestinationCountryModal?> {
                    override fun onResponse(
                        call: Call<DestinationCountryModal?>,
                        response: Response<DestinationCountryModal?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getLeadBranchModal!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError73(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<DestinationCountryModal?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError73(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError73(0, "No internet connection.")
        }
        return getLeadBranchModal!!
    }

    private fun handleError73(code: Int, backendMessage: String?) {
        val getPaymentApplicationPayResponse = DestinationCountryModal()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        getLeadBranchModal!!.postValue(getPaymentApplicationPayResponse)
    }


    // _lead_branch
    var staffProfileModal: MutableLiveData<StaffProfileModal?>? = null

    fun getStaffProfileData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,

        ): LiveData<StaffProfileModal?> {

        staffProfileModal = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getStaffProfileData(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<StaffProfileModal?> {
                    override fun onResponse(
                        call: Call<StaffProfileModal?>,
                        response: Response<StaffProfileModal?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            staffProfileModal!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError74(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<StaffProfileModal?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError74(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError74(0, "No internet connection.")
        }
        return staffProfileModal!!
    }

    private fun handleError74(code: Int, backendMessage: String?) {
        val getPaymentApplicationPayResponse = StaffProfileModal()
        getPaymentApplicationPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getPaymentApplicationPayResponse.message = errorMessage
        Log.e("API Error", getPaymentApplicationPayResponse.message!!)
        staffProfileModal!!.postValue(getPaymentApplicationPayResponse)
    }

    var postChangeStatusReminderMutableLiveData: MutableLiveData<changeStatusReminder?>? = null

    fun postChangeStatusReminderApplicationLiveData(
        activity: Activity,
        client_number: String,
        device_number: String,
        accessToken: String,
        Identifier: String,
        status: String,
        hasFollowUp: String,
        followup_at: String,
        note: String

    ): LiveData<changeStatusReminder?> {

        postChangeStatusReminderMutableLiveData = MutableLiveData()


        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.changeStatusReminder(
                client_number,
                device_number,
                accessToken,
                Identifier,
                status,
                hasFollowUp,
                followup_at,
                note
            )
                .enqueue(object : Callback<changeStatusReminder?> {
                    override fun onResponse(
                        call: Call<changeStatusReminder?>,
                        response: Response<changeStatusReminder?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postChangeStatusReminderMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Successfully")

                            } else {
                                CommonUtils.toast(activity, "Not  Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            Log.e("shshhshs", "${response.message()}")

                            handleError75(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<changeStatusReminder?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError75(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError75(0, "No internet connection.")
        }
        return postChangeStatusReminderMutableLiveData!!
    }

    private fun handleError75(code: Int, backendMessage: String?) {
        var changeStatusReminderResponse = changeStatusReminder()
        changeStatusReminderResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        changeStatusReminderResponse.message = errorMessage
        Log.e("API Error", changeStatusReminderResponse.message!!)
        postChangeStatusReminderMutableLiveData!!.postValue(changeStatusReminderResponse)
    }


    var saveLeadDocumentsMutableLiveData: MutableLiveData<saveApplicationDocuments?>? = null

    fun saveLeadDocumentsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        jsonString: SaveDocumentsRequest
    ): LiveData<saveApplicationDocuments?> {

        saveLeadDocumentsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.saveDocumentsLead(
                client_number,
                device_number,
                accessToken,
                identifier,
                jsonString
            )
                .enqueue(object : Callback<saveApplicationDocuments?> {
                    override fun onResponse(
                        call: Call<saveApplicationDocuments?>,
                        response: Response<saveApplicationDocuments?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            saveLeadDocumentsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {

                                CommonUtils.toast(activity, "Documents saved Successfully")


                            } else {
                                CommonUtils.toast(activity, "Documents not saved Successfully")
                            }

                        } else {
                            val apiError = parseError(response)
                            handleError76(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<saveApplicationDocuments?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError76(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError76(0, "No internet connection.")
        }
        return saveLeadDocumentsMutableLiveData!!
    }

    private fun handleError76(code: Int, backendMessage: String?) {
        var saveApplicationDocumentsResponse = saveApplicationDocuments()
        saveApplicationDocumentsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        saveApplicationDocumentsResponse.message = errorMessage
        Log.e("API Error", saveApplicationDocumentsResponse.message!!)
        saveLeadDocumentsMutableLiveData!!.postValue(saveApplicationDocumentsResponse)
    }

    var getDestinationManagerLiveMutable: MutableLiveData<getDestinationmanager?>? = null

    fun getDestinationManagerData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getDestinationmanager?> {

        getDestinationManagerLiveMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getDestinationManagerList(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<getDestinationmanager?> {
                    override fun onResponse(
                        call: Call<getDestinationmanager?>,
                        response: Response<getDestinationmanager?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getDestinationManagerLiveMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getDestinationmanager?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError77(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError77(0, "No internet connection.")
        }
        return getDestinationManagerLiveMutable!!
    }

    private fun handleError77(code: Int, backendMessage: String?) {
        val getDestinationmanagerPayResponse = getDestinationmanager()
        getDestinationmanagerPayResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationmanagerPayResponse.message = errorMessage
        Log.e("API Error", getDestinationmanagerPayResponse.message!!)
        getDestinationManagerLiveMutable!!.postValue(getDestinationmanagerPayResponse)
    }


    var getDestinationCountryLiveMutable: MutableLiveData<getDestinationCountry?>? = null

    fun getDestinationCountryLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getDestinationCountry?> {

        getDestinationCountryLiveMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getDestinationCountryList(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<getDestinationCountry?> {
                    override fun onResponse(
                        call: Call<getDestinationCountry?>,
                        response: Response<getDestinationCountry?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getDestinationCountryLiveMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getDestinationCountry?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError78(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError78(0, "No internet connection.")
        }
        return getDestinationCountryLiveMutable!!
    }

    private fun handleError78(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = getDestinationCountry()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getDestinationCountryLiveMutable!!.postValue(getDestinationCountryResponse)
    }

    var createCounsellingLeadMutableLiveData: MutableLiveData<createCounsellingModel?>? = null

    fun createCounsellingLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        content: String
    ): LiveData<createCounsellingModel?> {

        createCounsellingLeadMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.createcounsellings(client_number, device_number, accessToken, content)
                .enqueue(object : Callback<createCounsellingModel?> {
                    override fun onResponse(
                        call: Call<createCounsellingModel?>,
                        response: Response<createCounsellingModel?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            createCounsellingLeadMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Create Counselling Successfully")


                            } else {
                                CommonUtils.toast(activity, "counselling not Created Successfully")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError79(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<createCounsellingModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleError79(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError79(0, "No internet connection.")
        }
        return createCounsellingLeadMutableLiveData!!
    }

    private fun handleError79(code: Int, backendMessage: String?) {
        var createCounsellingModelResponse = createCounsellingModel()
        createCounsellingModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        createCounsellingModelResponse.message = errorMessage
        Log.e("API Error", createCounsellingModelResponse.message!!)
        createCounsellingLeadMutableLiveData!!.postValue(createCounsellingModelResponse)
    }


    // get Students


    var getStudentResponseMutable: MutableLiveData<GetStudentResponse?>? = null

    fun getStudentResponseMutable(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetStudentResponse?> {

        getStudentResponseMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getStudentsList(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<GetStudentResponse?> {
                    override fun onResponse(
                        call: Call<GetStudentResponse?>,
                        response: Response<GetStudentResponse?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getStudentResponseMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetStudentResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError80(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError80(0, "No internet connection.")
        }
        return getStudentResponseMutable!!
    }

    private fun handleError80(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = GetStudentResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getStudentResponseMutable!!.postValue(getDestinationCountryResponse)
    }


    // get prefer collage List


    var getPreferCollageModalMutable: MutableLiveData<PreferCollageModal?>? = null

    fun getPreferCollageData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        country_id: String,
    ): LiveData<PreferCollageModal?> {

        getPreferCollageModalMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getPreferCollageList(
                client_number,
                device_number,
                accessToken,
                country_id
            )!!.enqueue(object : Callback<PreferCollageModal?> {
                override fun onResponse(
                    call: Call<PreferCollageModal?>,
                    response: Response<PreferCollageModal?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getPreferCollageModalMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError77(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<PreferCollageModal?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError81(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError81(0, "No internet connection.")
        }
        return getPreferCollageModalMutable!!
    }

    private fun handleError81(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = PreferCollageModal()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getPreferCollageModalMutable!!.postValue(getDestinationCountryResponse)
    }


    // get campus List


    var getCampusResponseMutable: MutableLiveData<GetCampusResponse?>? = null

    fun getCampusListData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        institutionId: String,
    ): LiveData<GetCampusResponse?> {

        getCampusResponseMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getCampusList(client_number, device_number, accessToken, institutionId)!!
                .enqueue(object : Callback<GetCampusResponse?> {
                    override fun onResponse(
                        call: Call<GetCampusResponse?>,
                        response: Response<GetCampusResponse?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getCampusResponseMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError82(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetCampusResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError82(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError82(0, "No internet connection.")
        }
        return getCampusResponseMutable!!
    }

    private fun handleError82(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = GetCampusResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getCampusResponseMutable!!.postValue(getDestinationCountryResponse)
    }


    // get prefer Collage List


    var getPreferCourseMutable: MutableLiveData<GetCampusResponse?>? = null

    fun getPreferCourseData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        campus_id: String,
        institution_id: String,
    ): LiveData<GetCampusResponse?> {

        getPreferCourseMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getPreferCourseList(
                client_number,
                device_number,
                accessToken,
                campus_id, institution_id
            )!!.enqueue(object : Callback<GetCampusResponse?> {
                override fun onResponse(
                    call: Call<GetCampusResponse?>,
                    response: Response<GetCampusResponse?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getPreferCourseMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError83(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<GetCampusResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError83(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError83(0, "No internet connection.")
        }
        return getPreferCourseMutable!!
    }

    private fun handleError83(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = GetCampusResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getPreferCourseMutable!!.postValue(getDestinationCountryResponse)
    }


    // create_application


    var createApplicationMutable: MutableLiveData<CreateApplicationModal?>? = null

    fun createApplicationMutableData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        requestBody: CreateApplicationRequest // Accept the data class instance
    ): LiveData<CreateApplicationModal?> {

        createApplicationMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)

            apiInterface.createApplication(
                client_number,
                device_number,
                accessToken,
                requestBody // Pass the data class instance here
            ).enqueue(object : Callback<CreateApplicationModal?> {
                override fun onResponse(
                    call: Call<CreateApplicationModal?>,
                    response: Response<CreateApplicationModal?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        createApplicationMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            CommonUtils.toast(activity, "Payment List getting")
                        }
                    } else {
                        val apiError = parseError(response)
                        handleError84(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<CreateApplicationModal?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError84(0, "Network error: " + t.message)
                }
            })
        } else {
            handleError84(0, "No internet connection.")
        }
        return createApplicationMutable!!
    }


    private fun handleError84(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = CreateApplicationModal()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        createApplicationMutable!!.postValue(getDestinationCountryResponse)
    }

    // get_country_list

    var getLeadCountryList: MutableLiveData<GetStudentResponse?>? = null

    fun getLeadCountryListMutable(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetStudentResponse?> {

        getLeadCountryList = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.get_lead_country(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<GetStudentResponse?> {
                    override fun onResponse(
                        call: Call<GetStudentResponse?>,
                        response: Response<GetStudentResponse?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getLeadCountryList!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetStudentResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError80(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError80(0, "No internet connection.")
        }
        return getLeadCountryList!!
    }
    // get_country_state_list

    var getLeadCountryStateList: MutableLiveData<GetStudentResponse?>? = null

    fun getLeadCountryStateListMutable(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        countryName: String,
    ): LiveData<GetStudentResponse?> {

        getLeadCountryStateList = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.get_lead_state(client_number, device_number, accessToken, countryName)!!
                .enqueue(object : Callback<GetStudentResponse?> {
                    override fun onResponse(
                        call: Call<GetStudentResponse?>,
                        response: Response<GetStudentResponse?>,
                    ) {
                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getLeadCountryStateList!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetStudentResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleError80(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError80(0, "No internet connection.")
        }
        return getLeadCountryStateList!!
    }


    // lead_destination_country

    var getlead_destination_countryMutable: MutableLiveData<GetStudentResponse?>? = null

    fun getlead_destination_countryData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetStudentResponse?> {

        getlead_destination_countryMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getlead_destination_countryList(
                client_number,
                device_number,
                accessToken
            )!!.enqueue(object : Callback<GetStudentResponse?> {
                override fun onResponse(
                    call: Call<GetStudentResponse?>,
                    response: Response<GetStudentResponse?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getlead_destination_countryMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleError83(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<GetStudentResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError83(0, "Network error: " + t.message)
                }


            })
        } else {
            handleError83(0, "No internet connection.")
        }
        return getlead_destination_countryMutable!!
    }
    // get Utm Source

    var getUtmSourceListMutable: MutableLiveData<UtmModalResponse?>? = null

    fun getUtmSourceListData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<UtmModalResponse?> {

        getUtmSourceListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getUtmSourceList(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<UtmModalResponse?> {
                    override fun onResponse(
                        call: Call<UtmModalResponse?>,
                        response: Response<UtmModalResponse?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getUtmSourceListMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleErrorUtm(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<UtmModalResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleErrorUtm(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorUtm(0, "No internet connection.")
        }
        return getUtmSourceListMutable!!
    }

    private fun handleErrorUtm(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = UtmModalResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getUtmSourceListMutable!!.postValue(getDestinationCountryResponse)
    }

    var getWorkliTabsMutableLiveData: MutableLiveData<getWorklliTabs?>? = null

    fun getWorkliTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        category: String,
    ): LiveData<getWorklliTabs?> {

        getWorkliTabsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
// CommonUtils.showProgress(activity)
            apiInterface.getWorkliiTabs(client_number, device_number, accessToken, category)!!
                .enqueue(object : Callback<getWorklliTabs?> {
                    override fun onResponse(
                        call: Call<getWorklliTabs?>,
                        response: Response<getWorklliTabs?>,
                    ) {

// CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getWorkliTabsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
//CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError90(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getWorklliTabs?>, t: Throwable) {
// CommonUtils.dismissProgress()
                        handleError90(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError90(0, "No internet connection.")
        }
        return getWorkliTabsMutableLiveData!!
    }

    private fun handleError90(code: Int, backendMessage: String?) {
        var getWorklliTabsResponse = getWorklliTabs()
        getWorklliTabsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getWorklliTabsResponse.message = errorMessage
        Log.e("API Error", getWorklliTabsResponse.message!!)
        getWorkliTabsMutableLiveData!!.postValue(getWorklliTabsResponse)
    }

    var getSubWorkliTabsMutableLiveData: MutableLiveData<getSubWorkliiTabInfo?>? = null

    fun getSubWorkliTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        from: String,
        type: String,
        statues: String,
        page: Int,
        per_page: Int,
        sort: String

    ): LiveData<getSubWorkliiTabInfo?> {

        getSubWorkliTabsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
// CommonUtils.showProgress(activity)
            apiInterface.getSubWorkliiTabs(
                client_number,
                device_number,
                accessToken,
                from,
                type,
                statues,
                page,
                per_page,
                sort
            )!!
                .enqueue(object : Callback<getSubWorkliiTabInfo?> {
                    override fun onResponse(
                        call: Call<getSubWorkliiTabInfo?>,
                        response: Response<getSubWorkliiTabInfo?>,
                    ) {

// CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getSubWorkliTabsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
//CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError92(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getSubWorkliiTabInfo?>, t: Throwable) {
// CommonUtils.dismissProgress()
                        handleError92(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleError92(0, "No internet connection.")
        }
        return getSubWorkliTabsMutableLiveData!!
    }

    private fun handleError92(code: Int, backendMessage: String?) {
        var getSubWorklliTabsResponse = getSubWorkliiTabInfo()
        getSubWorklliTabsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getSubWorklliTabsResponse.message = errorMessage
        Log.e("API Error", getSubWorklliTabsResponse.message!!)
        getSubWorkliTabsMutableLiveData!!.postValue(getSubWorklliTabsResponse)
    }

    // timeline lead stage


    var getTimelineLeadStageMutable: MutableLiveData<UtmModalResponse?>? = null

    fun getTimelineLeadData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<UtmModalResponse?> {

        getTimelineLeadStageMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getTimelineStageList(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<UtmModalResponse?> {
                    override fun onResponse(
                        call: Call<UtmModalResponse?>,
                        response: Response<UtmModalResponse?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getTimelineLeadStageMutable!!.postValue(response.body())

                            if (response.body()!!.statusCode == 200) {
                                // CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleError77(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<UtmModalResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleErrorTimeLine(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorTimeLine(0, "No internet connection.")
        }
        return getTimelineLeadStageMutable!!
    }

    private fun handleErrorTimeLine(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = UtmModalResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getTimelineLeadStageMutable!!.postValue(getDestinationCountryResponse)
    }

    // timeline status change


    var getChangeTimelineLeadStageMutable: MutableLiveData<ChangeLeadStatus?>? = null

    fun getChangeTimelineLeadData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        LeadId: String,
        LeadStatusId: String,
    ): LiveData<ChangeLeadStatus?> {

        getChangeTimelineLeadStageMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.changeLeadStatusApi(
                client_number,
                device_number,
                accessToken,
                LeadId,
                LeadStatusId
            )!!.enqueue(object : Callback<ChangeLeadStatus?> {
                override fun onResponse(
                    call: Call<ChangeLeadStatus?>,
                    response: Response<ChangeLeadStatus?>,
                ) {

                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getChangeTimelineLeadStageMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleErrorTimeLineStatus(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<ChangeLeadStatus?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleErrorTimeLineStatus(0, "Network error: " + t.message)
                }


            })
        } else {
            handleErrorTimeLineStatus(0, "No internet connection.")
        }
        return getChangeTimelineLeadStageMutable!!
    }

    private fun handleErrorTimeLineStatus(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = ChangeLeadStatus()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getChangeTimelineLeadStageMutable!!.postValue(getDestinationCountryResponse)
    }


    // get Counselling


    var getCounsellingResponseMutable: MutableLiveData<CounsellingResponse?>? = null

    fun getCounsellingResponseData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        lead_identifier: String,
        status: String,
        page: Int,
        per_page: Int,

        ): LiveData<CounsellingResponse?> {

        getCounsellingResponseMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getCounsellingResponse(
                client_number,
                device_number,
                accessToken,
                lead_identifier,
                status,
                page,
                per_page,

                )!!.enqueue(object : Callback<CounsellingResponse?> {
                override fun onResponse(
                    call: Call<CounsellingResponse?>,
                    response: Response<CounsellingResponse?>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getCounsellingResponseMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleErrorCounselling(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<CounsellingResponse?>, t: Throwable) {
                    handleErrorCounselling(0, "Network error: " + t.message)
                }


            })
        } else {
            handleErrorCounselling(0, "No internet connection.")
        }
        return getCounsellingResponseMutable!!
    }

    private fun handleErrorCounselling(code: Int, backendMessage: String?) {
        val getDestinationCountryResponse = CounsellingResponse()
        getDestinationCountryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$backendMessage"
            422 -> backendMessage ?: "$backendMessage"
            else -> backendMessage ?: "Error $code"
        }
        getDestinationCountryResponse.message = errorMessage
        Log.e("API Error", getDestinationCountryResponse.message!!)
        getCounsellingResponseMutable!!.postValue(getDestinationCountryResponse)
    }

    var getInstitutionMutableLiveData1: MutableLiveData<InstitutionModel?>? = null
    fun getInstitutionModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        countryId: String,
    ): LiveData<InstitutionModel?> {

        getInstitutionMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getInstitution(client_number, device_number, acessToken, countryId)!!
                .enqueue(object : Callback<InstitutionModel?> {
                    override fun onResponse(
                        call: Call<InstitutionModel?>,
                        response: Response<InstitutionModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getInstitutionMutableLiveData1!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleError93(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<InstitutionModel?>, t: Throwable) {
                        //  CommonUtils.dismissProgress()
                        handleError93(0, "Network error: " + t.message)
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getInstitutionMutableLiveData1!!
    }


    private fun handleError93(code: Int, backendMessage: String?) {
        var InstitutionModelResponse = InstitutionModel()
        InstitutionModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        InstitutionModelResponse.message = errorMessage
        Log.e("API Error", InstitutionModelResponse.message!!)
        getInstitutionMutableLiveData1!!.postValue(InstitutionModelResponse)
    }


    // get application program details
    var getApplicationProgramResponseData: MutableLiveData<ApplicationProgramResponse?>? = null

    fun getApplicationProgramResponseData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        acessToken: String,
        country_id: String,
        institution_id: String,
        program_id: String,
        campus_id: String,
    ): LiveData<ApplicationProgramResponse?> {

        getApplicationProgramResponseData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //  CommonUtils.showProgress(activity)
            apiInterface.getApplicationProgramDetails(
                client_number,
                device_number,
                acessToken,
                country_id,
                institution_id,
                program_id,
                campus_id
            )!!
                .enqueue(object : Callback<ApplicationProgramResponse?> {
                    override fun onResponse(
                        call: Call<ApplicationProgramResponse?>,
                        response: Response<ApplicationProgramResponse?>
                    ) {
                        //  CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getApplicationProgramResponseData!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            handleErrorgetApplicationProgramResponseData(
                                response.code(),
                                getErrorMessage(apiError)
                            )
                        }
                    }

                    override fun onFailure(call: Call<ApplicationProgramResponse?>, t: Throwable) {
                        //  CommonUtils.dismissProgress()
                        handleErrorgetApplicationProgramResponseData(
                            0,
                            "Network error: " + t.message
                        )
                    }
                })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getApplicationProgramResponseData!!
    }


    private fun handleErrorgetApplicationProgramResponseData(code: Int, backendMessage: String?) {
        var InstitutionModelResponse = ApplicationProgramResponse()
        InstitutionModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        InstitutionModelResponse.message = errorMessage
        Log.e("API Error", InstitutionModelResponse.message!!)
        getApplicationProgramResponseData!!.postValue(InstitutionModelResponse)
    }

    var getOffersUpdatesMutableLiveData: MutableLiveData<GetOffersandUpdates?>? = null

    fun getOffersUpdatesTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetOffersandUpdates?> {

        getOffersUpdatesMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
// CommonUtils.showProgress(activity)
            apiInterface.getOffersupdates(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<GetOffersandUpdates?> {
                    override fun onResponse(
                        call: Call<GetOffersandUpdates?>,
                        response: Response<GetOffersandUpdates?>,
                    ) {

// CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getOffersUpdatesMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
//CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            getOffersupdates(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetOffersandUpdates?>, t: Throwable) {
// CommonUtils.dismissProgress()
                        getOffersupdates(0, "Network error: " + t.message)
                    }


                })
        } else {
            getOffersupdates(0, "No internet connection.")
        }
        return getOffersUpdatesMutableLiveData!!
    }

    private fun getOffersupdates(code: Int, backendMessage: String?) {
        var GetOffersandUpdatesResponse = GetOffersandUpdates()
        GetOffersandUpdatesResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        GetOffersandUpdatesResponse.message = errorMessage
        Log.e("API Error", GetOffersandUpdatesResponse.message!!)
        getOffersUpdatesMutableLiveData!!.postValue(GetOffersandUpdatesResponse)
    }

    var getScholarshipsMutableLiveData: MutableLiveData<GetScholarships?>? = null

    fun getScholarshipsTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetScholarships?> {

        getScholarshipsMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
// CommonUtils.showProgress(activity)
            apiInterface.getScholarships(client_number, device_number, accessToken)!!
                .enqueue(object : Callback<GetScholarships?> {
                    override fun onResponse(
                        call: Call<GetScholarships?>,
                        response: Response<GetScholarships?>,
                    ) {

//         CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getScholarshipsMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
//CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            getScholarships(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<GetScholarships?>, t: Throwable) {
// CommonUtils.dismissProgress()
                        getScholarships(0, "Network error: " + t.message)
                    }


                })
        } else {
            getScholarships(0, "No internet connection.")
        }
        return getScholarshipsMutableLiveData!!
    }

    private fun getScholarships(code: Int, backendMessage: String?) {
        var GetScholarshipsResponse = GetScholarships()
        GetScholarshipsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        GetScholarshipsResponse.message = errorMessage
        Log.e("API Error", GetScholarshipsResponse.message!!)
        getScholarshipsMutableLiveData!!.postValue(GetScholarshipsResponse)
    }

    var changePasscodeMutableLiveData: MutableLiveData<ForgotPasswordModel?>? = null

    fun changePasswordModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        authorization: String,
        oldPassword: String,
        user_identifier: String,
        confirmPassword: String,
        Password: String
    ): LiveData<ForgotPasswordModel?> {
        changePasscodeMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.changePassword(
                client_number,
                device_number,
                authorization,
                oldPassword,
                user_identifier,
                confirmPassword,
                Password
            )
                .enqueue(object : Callback<ForgotPasswordModel?> {
                    override fun onResponse(
                        call: Call<ForgotPasswordModel?>,
                        response: Response<ForgotPasswordModel?>
                    ) {
                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            changePasscodeMutableLiveData!!.postValue(response.body())
                        } else {
                            val apiError = parseError(response)
                            changeError5(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<ForgotPasswordModel?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        changeError5(0, "Network error: " + t.message)
                    }
                })
        } else {

            changeError5(0, "No internet connection.")

        }

        return changePasscodeMutableLiveData!!

    }

    private fun changeError5(code: Int, backendMessage: String?) {
        var forgotResponse = ForgotPasswordModel()
        forgotResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> "$backendMessage"
            else -> backendMessage ?: "Error $code"
        }
        forgotResponse.message = errorMessage
        Log.e("API Error", forgotResponse.message!!)
        changePasscodeMutableLiveData!!.postValue(forgotResponse)

    }

    var deleteAccountMutableLiveData: MutableLiveData<DeletePostResponse?>? = null

    fun deleteAccountLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accesToken: String
    ): LiveData<DeletePostResponse?> {

        deleteAccountMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.deleteAccount(client_number, device_number, accesToken)
                .enqueue(object : Callback<DeletePostResponse?> {
                    override fun onResponse(
                        call: Call<DeletePostResponse?>,
                        response: Response<DeletePostResponse?>
                    ) {
                        CommonUtils.dismissProgress()

                        if (response.isSuccessful) {
                            deleteAccountMutableLiveData!!.postValue(response.body())
                            CommonUtils.toast(activity, "Deleted  Accounted Successfully")
                        } else {
                            deleteAccountError24(response.code(), response.errorBody()?.string())
                        }
                    }

                    override fun onFailure(call: Call<DeletePostResponse?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        deleteAccountError24(0, "Network error: " + t.message)
                    }
                })
        } else {
            deleteAccountError24(0, "No internet connection.")
        }
        return deleteAccountMutableLiveData!!
    }

    private fun deleteAccountError24(code: Int, backendMessage: String?) {
        var deletePostResponse = DeletePostResponse()
        deletePostResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        deletePostResponse.message = errorMessage
        Log.e("API Error", deletePostResponse.message!!)
        deleteAccountMutableLiveData!!.postValue(deletePostResponse)
    }


    // upload images


    var postProfileImagesMutableLiveData: MutableLiveData<UploadImages?>? = null

    fun postProfileImagesLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        profile_picture_url: String,
    ): LiveData<UploadImages?> {

        postProfileImagesMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.uploadImage(
                client_number,
                device_number,
                accessToken,
                profile_picture_url
            )!!
                .enqueue(object : Callback<UploadImages?> {
                    override fun onResponse(
                        call: Call<UploadImages?>,
                        response: Response<UploadImages?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postProfileImagesMutableLiveData!!.postValue(response.body())

                        } else {
                            val apiError = parseError(response)
                            handleErrorProfilePic(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<UploadImages?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        handleErrorProfilePic(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorProfilePic(0, "No internet connection.")
        }
        return postProfileImagesMutableLiveData!!
    }

    private fun handleErrorProfilePic(code: Int, backendMessage: String?) {
        var uploadDocumentsResponse = UploadImages()
        uploadDocumentsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        uploadDocumentsResponse.message = errorMessage

        Log.e("API Error", uploadDocumentsResponse.message!!)

        postProfileImagesMutableLiveData!!.postValue(uploadDocumentsResponse)

    }

    // edit user profile

    var editProfileMutableLiveData: MutableLiveData<EditProfile?>? = null

    fun editProfileLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        first_name: String,
        last_name: String,
        gender: String,
        marital_status: String,
        birthday: String
    ): LiveData<EditProfile?> {

        editProfileMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.editUserProfile(
                client_number,
                device_number,
                accessToken,
                first_name,
                last_name,
                gender,
                marital_status,
                birthday
            )
                .enqueue(object : Callback<EditProfile?> {
                    override fun onResponse(
                        call: Call<EditProfile?>,
                        response: Response<EditProfile?>,
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            editProfileMutableLiveData!!.postValue(response.body())

                        } else {
                            val apiError = parseError(response)
                            editProfileData(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<EditProfile?>, t: Throwable) {
                        editProfileData(0, "Network error: " + t.message)
                    }

                })
        } else {
            editProfileData(0, "No internet connection.")
        }
        return editProfileMutableLiveData!!
    }

    private fun editProfileData(code: Int, backendMessage: String?) {
        var createCounsellingModelResponse = EditProfile()
        createCounsellingModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        createCounsellingModelResponse.message = errorMessage
        Log.e("API Error", createCounsellingModelResponse.message!!)
        editProfileMutableLiveData!!.postValue(createCounsellingModelResponse)
    }


    var getNotificationListMutable: MutableLiveData<getNotificationResponse?>? = null

    fun getNotificationList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String
    ): LiveData<getNotificationResponse?> {

        getNotificationListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getNotification(
                client_number,
                device_number,
                accessToken
            )!!.enqueue(object : Callback<getNotificationResponse?> {
                override fun onResponse(
                    call: Call<getNotificationResponse?>,
                    response: Response<getNotificationResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getNotificationListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleNotificationList(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getNotificationResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleNotificationList(0, "Network error: " + t.message)
                }


            })
        } else {
            handleNotificationList(0, "No internet connection.")
        }
        return getNotificationListMutable!!
    }

    private fun handleNotificationList(code: Int, backendMessage: String?) {
        val getNotificationResponse = getNotificationResponse()
        getNotificationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getNotificationResponse.message = errorMessage
        Log.e("API Error", getNotificationResponse.message!!)
        getNotificationListMutable!!.postValue(getNotificationResponse)
    }

    var getNotificationReadAllListMutable: MutableLiveData<getNotificationReadAllResponse?>? = null

    fun getNotificationReadAllList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String
    ): LiveData<getNotificationReadAllResponse?> {

        getNotificationReadAllListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getNotificationsReadAll(
                client_number,
                device_number,
                accessToken
            )!!.enqueue(object : Callback<getNotificationReadAllResponse?> {
                override fun onResponse(
                    call: Call<getNotificationReadAllResponse?>,
                    response: Response<getNotificationReadAllResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getNotificationReadAllListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleNotificationReadAllList(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getNotificationReadAllResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleNotificationReadAllList(0, "Network error: " + t.message)
                }


            })
        } else {
            handleNotificationReadAllList(0, "No internet connection.")
        }
        return getNotificationReadAllListMutable!!
    }

    private fun handleNotificationReadAllList(code: Int, backendMessage: String?) {
        val getNotificationReadAllResponse = getNotificationReadAllResponse()
        getNotificationReadAllResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getNotificationReadAllResponse.message = errorMessage
        Log.e("API Error", getNotificationReadAllResponse.message!!)
        getNotificationReadAllListMutable!!.postValue(getNotificationReadAllResponse)
    }

    var getNotificationReadListMutable: MutableLiveData<getNotificationReadResponse?>? = null

    fun getNotificationReadList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String
    ): LiveData<getNotificationReadResponse?> {

        getNotificationReadListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getNotificationsRead(
                client_number,
                device_number,
                accessToken,
                identifier
            )!!.enqueue(object : Callback<getNotificationReadResponse?> {
                override fun onResponse(
                    call: Call<getNotificationReadResponse?>,
                    response: Response<getNotificationReadResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getNotificationReadListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        getNotificationReadResponse(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getNotificationReadResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    getNotificationReadResponse(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            getNotificationReadResponse(
                0,
                "No internet connection."
            )
        }
        return getNotificationReadListMutable!!
    }

    private fun getNotificationReadResponse(code: Int, backendMessage: String?) {
        val getNotificationReadResponse = getNotificationReadResponse()
        getNotificationReadResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getNotificationReadResponse.message = errorMessage
        Log.e("API Error", getNotificationReadResponse.message!!)
        getNotificationReadListMutable!!.postValue(getNotificationReadResponse)
    }


    var getCountryFilterListMutable: MutableLiveData<getProgramFIltersResponse?>? = null

    fun getCountryFiltersList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        mapvalue: Map<String, String>,
        data: String,
    ): LiveData<getProgramFIltersResponse?> {

        getCountryFilterListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getCountryFilter(
                client_number,
                device_number,
                accessToken, mapvalue, data
            )!!.enqueue(object : Callback<getProgramFIltersResponse?> {
                override fun onResponse(
                    call: Call<getProgramFIltersResponse?>,
                    response: Response<getProgramFIltersResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getCountryFilterListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getProgramFIltersResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseFilters(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseFilters(
                0,
                "No internet connection."
            )
        }
        return getCountryFilterListMutable!!
    }

    private fun handleResponseFilters(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = getProgramFIltersResponse()
        getProgramFIltersResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramFIltersResponse.message = errorMessage
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getCountryFilterListMutable!!.postValue(getProgramFIltersResponse)
    }


    var getStateFilterListMutable: MutableLiveData<getProgramFIltersResponse?>? = null

    fun getStateFiltersList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        country_id: String,
    ): LiveData<getProgramFIltersResponse?> {

        getStateFilterListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getStateFilter(
                client_number,
                device_number,
                accessToken,
                country_id
            )!!.enqueue(object : Callback<getProgramFIltersResponse?> {
                override fun onResponse(
                    call: Call<getProgramFIltersResponse?>,
                    response: Response<getProgramFIltersResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getStateFilterListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters1(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getProgramFIltersResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseFilters1(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseFilters1(
                0,
                "No internet connection."
            )
        }
        return getStateFilterListMutable!!
    }

    private fun handleResponseFilters1(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = getProgramFIltersResponse()
        getProgramFIltersResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramFIltersResponse.message = errorMessage
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getStateFilterListMutable?.postValue(getProgramFIltersResponse)
    }


    var getcityFilterListMutable: MutableLiveData<getProgramFIltersResponse?>? = null

    fun getCityFiltersList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        country_id: String,
        state_id: String,
    ): LiveData<getProgramFIltersResponse?> {

        getcityFilterListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getCityFilter(
                client_number,
                device_number,
                accessToken,
                country_id,
                state_id
            )!!.enqueue(object : Callback<getProgramFIltersResponse?> {
                override fun onResponse(
                    call: Call<getProgramFIltersResponse?>,
                    response: Response<getProgramFIltersResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getcityFilterListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters2(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getProgramFIltersResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseFilters2(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseFilters2(
                0,
                "No internet connection."
            )
        }
        return getcityFilterListMutable!!
    }

    private fun handleResponseFilters2(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = getProgramFIltersResponse()
        getProgramFIltersResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramFIltersResponse.message = errorMessage
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getcityFilterListMutable?.postValue(getProgramFIltersResponse)
    }


    var getTuitionFilterListMutable: MutableLiveData<getProgramFIltersResponse?>? = null

    fun getTuitionFiltersList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        ltTuitionFee: Int,
        gtTuitionFee: Int,
    ): LiveData<getProgramFIltersResponse?> {

        getTuitionFilterListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getTuitionFilter(
                client_number,
                device_number,
                accessToken,
                ltTuitionFee,
                gtTuitionFee
            )!!.enqueue(object : Callback<getProgramFIltersResponse?> {
                override fun onResponse(
                    call: Call<getProgramFIltersResponse?>,
                    response: Response<getProgramFIltersResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getTuitionFilterListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters212(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getProgramFIltersResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseFilters212(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseFilters212(
                0,
                "No internet connection."
            )
        }
        return getTuitionFilterListMutable!!
    }

    private fun handleResponseFilters212(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = getProgramFIltersResponse()
        getProgramFIltersResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramFIltersResponse.message = errorMessage
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getTuitionFilterListMutable?.postValue(getProgramFIltersResponse)
    }

    var getApplicationFilterListMutable: MutableLiveData<getProgramFIltersResponse?>? = null

    fun getApplicationFiltersList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        ltApplicationFee: Int,
        gtApplicationFee: Int,
    ): LiveData<getProgramFIltersResponse?> {

        getApplicationFilterListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getApplicationFilter(
                client_number,
                device_number,
                accessToken,
                ltApplicationFee,
                gtApplicationFee
            )!!.enqueue(object : Callback<getProgramFIltersResponse?> {
                override fun onResponse(
                    call: Call<getProgramFIltersResponse?>,
                    response: Response<getProgramFIltersResponse?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getApplicationFilterListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters2124(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getProgramFIltersResponse?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseFilters2124(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseFilters2124(
                0,
                "No internet connection."
            )
        }
        return getApplicationFilterListMutable!!
    }

    private fun handleResponseFilters2124(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = getProgramFIltersResponse()
        getProgramFIltersResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramFIltersResponse.message = errorMessage
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getApplicationFilterListMutable?.postValue(getProgramFIltersResponse)
    }

    var getProgramtagsListMutable: MutableLiveData<ProgramTags?>? = null

    fun getPrograTagsList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        type: String,
    ): LiveData<ProgramTags?> {

        getProgramtagsListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getProgramTags(
                client_number,
                device_number,
                accessToken,
                type,
            )!!.enqueue(object : Callback<ProgramTags?> {
                override fun onResponse(
                    call: Call<ProgramTags?>,
                    response: Response<ProgramTags?>,
                ) {

                    if (response.isSuccessful && response.body() != null) {
                        getProgramtagsListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        programTagsFilters2124(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<ProgramTags?>, t: Throwable) {
                    programTagsFilters2124(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            programTagsFilters2124(
                0,
                "No internet connection."
            )
        }
        return getProgramtagsListMutable!!
    }

    private fun programTagsFilters2124(code: Int, backendMessage: String?) {
        val getProgramFIltersResponse = ProgramTags()
        code.also { getProgramFIltersResponse.statusCode = it }
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        errorMessage.also { getProgramFIltersResponse.message = it }
        Log.e("API Error", getProgramFIltersResponse.message!!)
        getProgramtagsListMutable?.postValue(getProgramFIltersResponse)
    }

    var saveReviewMutableLiveData: MutableLiveData<SaveReviewResponse?>? = null

    fun saveReviewLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
        user_identifier: String,
        title: String,
        content: String,
        rating: Int
    ): LiveData<SaveReviewResponse?> {

        saveReviewMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.saveReview(
                client_number,
                device_number,
                accessToken,
                identifier,
                user_identifier,
                title,
                content,
                rating
            )
                .enqueue(object : Callback<SaveReviewResponse?> {
                    override fun onResponse(
                        call: Call<SaveReviewResponse?>,
                        response: Response<SaveReviewResponse?>,
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            saveReviewMutableLiveData!!.postValue(response.body())

                        } else {
                            val apiError = parseError(response)
                            saveReviewData(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<SaveReviewResponse?>, t: Throwable) {
                        saveReviewData(0, "Network error: " + t.message)
                    }

                })
        } else {
            saveReviewData(0, "No internet connection.")
        }
        return saveReviewMutableLiveData!!
    }

    private fun saveReviewData(code: Int, backendMessage: String?) {
        var SaveReviewResponseModelResponse = SaveReviewResponse()
        SaveReviewResponseModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        SaveReviewResponseModelResponse.message = errorMessage
        Log.e("API Error", SaveReviewResponseModelResponse.message!!)
        saveReviewMutableLiveData!!.postValue(SaveReviewResponseModelResponse)
    }


    var getCategoryProgramListMutable: MutableLiveData<getCategoryProgramModel?>? = null

    fun getCategoryProgramList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getCategoryProgramModel?> {

        getCategoryProgramListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getCategoryProgramStat(
                client_number,
                device_number,
                accessToken,
            )!!.enqueue(object : Callback<getCategoryProgramModel?> {
                override fun onResponse(
                    call: Call<getCategoryProgramModel?>,
                    response: Response<getCategoryProgramModel?>,
                ) {

                    //CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getCategoryProgramListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // CommonUtils.toast(activity, "Payment List getting")

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters2124(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getCategoryProgramModel?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleResponseCategory(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseCategory(
                0,
                "No internet connection."
            )
        }
        return getCategoryProgramListMutable!!
    }

    private fun handleResponseCategory(code: Int, backendMessage: String?) {
        val getProgramCategoryResponse = getCategoryProgramModel()
        getProgramCategoryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramCategoryResponse.message = errorMessage
        Log.e("API Error", getProgramCategoryResponse.message!!)
        getCategoryProgramListMutable?.postValue(getProgramCategoryResponse)
    }

    //getPreferCounrty


    var getCountryListMutable: MutableLiveData<GetPreferCountryList?>? = null

    fun getCountryListProgramList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetPreferCountryList?> {

        getCountryListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getPreferCountryList(
                client_number,
                device_number,
                accessToken,
            )!!.enqueue(object : Callback<GetPreferCountryList?> {
                override fun onResponse(
                    call: Call<GetPreferCountryList?>,
                    response: Response<GetPreferCountryList?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getCountryListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseFilters2124(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<GetPreferCountryList?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleResponseCategoryCountry(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseCategory(
                0,
                "No internet connection."
            )
        }
        return getCountryListMutable!!
    }

    private fun handleResponseCategoryCountry(code: Int, backendMessage: String?) {
        val getProgramCategoryResponse = GetPreferCountryList()
        getProgramCategoryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramCategoryResponse.message = errorMessage
        Log.e("API Error", getProgramCategoryResponse.message!!)
        getCountryListMutable?.postValue(getProgramCategoryResponse)
    }


    //getDisplineList


    var getDisciplineModelListMutable: MutableLiveData<GetPreferCountryList?>? = null

    fun getDisciplineDataList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<GetPreferCountryList?> {

        getDisciplineModelListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface2.getDisciplineList(
                client_number,
                device_number,
                accessToken,
            )!!.enqueue(object : Callback<GetPreferCountryList?> {
                override fun onResponse(
                    call: Call<GetPreferCountryList?>,
                    response: Response<GetPreferCountryList?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getDisciplineModelListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {

                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }


                    } else {
                        val apiError = parseError(response)
                        handleResponseCategoryDispline(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<GetPreferCountryList?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleResponseCategoryDispline(
                        0,
                        "Network error: " + t.message
                    )
                }


            })
        } else {
            handleResponseCategory(
                0,
                "No internet connection."
            )
        }
        return getDisciplineModelListMutable!!
    }

    private fun handleResponseCategoryDispline(code: Int, backendMessage: String?) {
        val getProgramCategoryResponse = GetPreferCountryList()
        getProgramCategoryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramCategoryResponse.message = errorMessage
        Log.e("API Error", getProgramCategoryResponse.message!!)
        getDisciplineModelListMutable?.postValue(getProgramCategoryResponse)
    }

    // save-preferences


    var savePreferencesModelListMutable: MutableLiveData<SavePreferences?>? = null

    fun savePreferencesDataList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        request: SavePreferencesRequest // ✅ JSON body object
    ): LiveData<SavePreferences?> {

        savePreferencesModelListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)

            apiInterface.savePreferences(
                client_number,
                device_number,
                accessToken,
                request
            )!!.enqueue(object : Callback<SavePreferences?> {
                override fun onResponse(
                    call: Call<SavePreferences?>,
                    response: Response<SavePreferences?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful && response.body() != null) {
                        savePreferencesModelListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // handle success
                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }
                    } else {
                        val apiError = parseError(response)
                        handleResponseCategoryDispline(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<SavePreferences?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleResponseCategoryPrefrences(
                        0,
                        "Network error: ${t.message}"
                    )
                }
            })
        } else {
            handleResponseCategoryPrefrences(
                0,
                "No internet connection."
            )
        }

        return savePreferencesModelListMutable!!
    }


    private fun handleResponseCategoryPrefrences(code: Int, backendMessage: String?) {
        val getProgramCategoryResponse = SavePreferences()
        getProgramCategoryResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            422 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getProgramCategoryResponse.message = errorMessage
        Log.e("API Error", getProgramCategoryResponse.message!!)
        savePreferencesModelListMutable?.postValue(getProgramCategoryResponse)
    }


    // save-preferences



    var getVouchersMutableLiveData1: MutableLiveData<getVouchers?>? = null
    fun getVouchersModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
    ): LiveData<getVouchers?> {

        getVouchersMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getVouchers(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                "true"
            ).enqueue(object : Callback<getVouchers?> {
                override fun onResponse(
                    call: Call<getVouchers?>,
                    response: Response<getVouchers?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getVouchersMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError6890(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getVouchers?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleError6890(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getVouchersMutableLiveData1!!
    }

    private fun handleError6890(code: Int, backendMessage: String?) {
        val getVouchers = getVouchers()
        getVouchers.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getVouchers.message = errorMessage
        Log.e("API Error", getVouchers.message!!)
        getVouchersMutableLiveData1!!.postValue(getVouchers)
    }

    var getModeOfPaymentDropDownVoucherMutableLiveData: MutableLiveData<getVoucherPaymentMode?>? =
        null

    fun getModeOFPaymentDropDownVoucherLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getVoucherPaymentMode?> {

        getModeOfPaymentDropDownVoucherMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getModeOfPaymentDropdownVoucher(
                client_number,
                device_number,
                accessToken,
                "true",
                "gateway"
            )!!
                .enqueue(object : Callback<getVoucherPaymentMode?> {
                    override fun onResponse(
                        call: Call<getVoucherPaymentMode?>,
                        response: Response<getVoucherPaymentMode?>,
                    ) {

                        // CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            getModeOfPaymentDropDownVoucherMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 200) {
                                //CommonUtils.toast(activity, "Payment List getting")

                            } else {
                                CommonUtils.toast(activity, "Not Found")
                            }


                        } else {
                            val apiError = parseError(response)
                            handleErrorVoucher(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<getVoucherPaymentMode?>, t: Throwable) {
                        // CommonUtils.dismissProgress()
                        handleErrorVoucher(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorVoucher(0, "No internet connection.")
        }
        return getModeOfPaymentDropDownVoucherMutableLiveData!!
    }

    private fun handleErrorVoucher(code: Int, backendMessage: String?) {
        var getVoucherPaymentModeResponse = getVoucherPaymentMode()
        getVoucherPaymentModeResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getVoucherPaymentModeResponse.message = errorMessage
        Log.e("API Error", getVoucherPaymentModeResponse.message!!)
        getModeOfPaymentDropDownVoucherMutableLiveData!!.postValue(getVoucherPaymentModeResponse)
    }

    var postVoucherPaymentGeneratingLinkMutableLiveData: MutableLiveData<generatingPaymentLinkVoucher?>? =
        null

    fun genratingPaymentLinkVoucherLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        module: String,
        module_identifier: String,
        price: String,
        currency: String,
        quantity: String,
        payment_type_identifier: String,
        payment_gateway_identifier: String
    ): LiveData<generatingPaymentLinkVoucher?> {

        postVoucherPaymentGeneratingLinkMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.generatingPaymentLinksVoucher(
                client_number,
                device_number,
                accessToken,
                currency,
                price,
                quantity,
                module,
                module_identifier,
                payment_type_identifier,
                payment_gateway_identifier
            )
                .enqueue(object : Callback<generatingPaymentLinkVoucher?> {
                    override fun onResponse(
                        call: Call<generatingPaymentLinkVoucher?>,
                        response: Response<generatingPaymentLinkVoucher?>,
                    ) {

                        CommonUtils.dismissProgress()
                        if (response.isSuccessful && response.body() != null) {
                            postVoucherPaymentGeneratingLinkMutableLiveData!!.postValue(response.body())

                            if (response!!.body()!!.statusCode == 201) {
                                CommonUtils.toast(activity, "Generating Payment Link  Successfully")

                            } else {
                                CommonUtils.toast(
                                    activity,
                                    "Generating Payment Link not  Successfully"
                                )
                            }


                        } else {
                            val apiError = parseError(response)
                            handleErrorLink(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(
                        call: Call<generatingPaymentLinkVoucher?>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissProgress()
                        handleErrorLink(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorLink(0, "No internet connection.")
        }
        return postVoucherPaymentGeneratingLinkMutableLiveData!!
    }

    private fun handleErrorLink(code: Int, backendMessage: String?) {
        var generatingPaymentLinkApplicationResponse = generatingPaymentLinkVoucher()
        generatingPaymentLinkApplicationResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        generatingPaymentLinkApplicationResponse.message = errorMessage
        Log.e("API Error", generatingPaymentLinkApplicationResponse.message!!)
        postVoucherPaymentGeneratingLinkMutableLiveData!!.postValue(
            generatingPaymentLinkApplicationResponse
        )
    }

    var getVouchersHistoryTabsModelListMutable: MutableLiveData<getVouchersHistoryTabs?>? = null

    fun getVoucherHistoryTabsDataList(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getVouchersHistoryTabs?> {

        getVouchersHistoryTabsModelListMutable = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)

            apiInterface.getVouchersHistoryTabs(
                client_number,
                device_number,
                accessToken,
            )!!.enqueue(object : Callback<getVouchersHistoryTabs?> {
                override fun onResponse(
                    call: Call<getVouchersHistoryTabs?>,
                    response: Response<getVouchersHistoryTabs?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful && response.body() != null) {
                        getVouchersHistoryTabsModelListMutable!!.postValue(response.body())

                        if (response.body()!!.statusCode == 200) {
                            // handle success
                        } else {
                            CommonUtils.toast(activity, "Not Found")
                        }
                    } else {
                        val apiError = parseError(response)
                        getVouchersHistoryTabsError(
                            response.code(),
                            getErrorMessage(apiError)
                        )
                    }
                }

                override fun onFailure(call: Call<getVouchersHistoryTabs?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    getVouchersHistoryTabsError(
                        0,
                        "Network error: ${t.message}"
                    )
                }
            })
        } else {
            getVouchersHistoryTabsError(
                0,
                "No internet connection."
            )
        }

        return getVouchersHistoryTabsModelListMutable!!
    }


    private fun getVouchersHistoryTabsError(code: Int, backendMessage: String?) {
        val getVouchersHistoryTabsResponse = getVouchersHistoryTabs()
        getVouchersHistoryTabsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getVouchersHistoryTabsResponse.message = errorMessage
        Log.e("API Error", getVouchersHistoryTabsResponse.message!!)
        getVouchersHistoryTabsModelListMutable?.postValue(getVouchersHistoryTabsResponse)
    }


    var getVouchersHistoryMutableLiveData1: MutableLiveData<getHistoryListModel?>? = null
    fun getVouchersHistoryModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        search: String?,
        sort_by: String,
        sort: String
    ): LiveData<getHistoryListModel?> {

        getVouchersHistoryMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            //CommonUtils.showProgress(activity)
            apiInterface.getVouchersHistory(
                client_number,
                device_number,
                refreshToken,
                perPage,
                page,
                search,
                "true",
                sort_by,
                sort
            ).enqueue(object : Callback<getHistoryListModel?> {
                override fun onResponse(
                    call: Call<getHistoryListModel?>,
                    response: Response<getHistoryListModel?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getVouchersHistoryMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleErrorHistory(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getHistoryListModel?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleErrorHistory(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return getVouchersHistoryMutableLiveData1!!
    }

    private fun handleErrorHistory(code: Int, backendMessage: String?) {
        val getHistoryListModel = getHistoryListModel()
        getHistoryListModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getHistoryListModel.message = errorMessage
        Log.e("API Error", getHistoryListModel.message!!)
        getVouchersHistoryMutableLiveData1!!.postValue(getHistoryListModel)
    }


    var AllRecommendedProgramMutableLiveData1: MutableLiveData<AllProgramModel?>? = null
    fun getAllRecommendedProgramsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,

        ): LiveData<AllProgramModel?> {

        AllRecommendedProgramMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getRecommended(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,

                )!!.enqueue(object : Callback<AllProgramModel?> {
                override fun onResponse(
                    call: Call<AllProgramModel?>,
                    response: Response<AllProgramModel?>
                ) {
                    //    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        AllRecommendedProgramMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError70777789(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleError70777789(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllRecommendedProgramMutableLiveData1!!
    }


    private fun handleError70777789(code: Int, backendMessage: String?) {
        var allProgramModel = AllProgramModel()
        allProgramModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        allProgramModel.message = errorMessage
        Log.e("API Error", allProgramModel.message!!)
        AllRecommendedProgramMutableLiveData1!!.postValue(allProgramModel)
    }

    var AllBannerMutableLiveData1: MutableLiveData<getBannerModel?>? = null
    fun getBannerModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,

        ): LiveData<getBannerModel?> {

        AllBannerMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getBanner(
                client_number,
                device_number,
                accessToken,

                )!!.enqueue(object : Callback<getBannerModel?> {
                override fun onResponse(
                    call: Call<getBannerModel?>,
                    response: Response<getBannerModel?>
                ) {
                    //    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        AllBannerMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleErrorBanner(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getBannerModel?>, t: Throwable) {
                    // CommonUtils.dismissProgress()
                    handleErrorBanner(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return AllBannerMutableLiveData1!!
    }


    private fun handleErrorBanner(code: Int, backendMessage: String?) {
        var getBannerModel = getBannerModel()
        getBannerModel.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getBannerModel.message = errorMessage
        Log.e("API Error", getBannerModel.message!!)
        AllBannerMutableLiveData1!!.postValue(getBannerModel)
    }


    var getLeadMutableLiveData1: MutableLiveData<getLeadsModal?>? = null
    fun getLeadModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        dataperPage: Int
    ): LiveData<getLeadsModal?> {

        getLeadMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.getLeads(
                client_number,
                device_number,
                accessToken,
                page,
                dataperPage
            )!!.enqueue(object : Callback<getLeadsModal?> {
                override fun onResponse(
                    call: Call<getLeadsModal?>,
                    response: Response<getLeadsModal?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        getLeadMutableLiveData1!!.postValue(response.body())
                        Log.d("onResponseData", response.body()?.data.toString())
                    } else {
                        val apiError = parseError(response)
                        handleErrorLead(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getLeadsModal?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleErrorLead(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorLead(0, "No internet connection.")
        }
        return getLeadMutableLiveData1!!
    }


    private fun handleErrorLead(code: Int, backendMessage: String?) {
        var getLeadModal = getLeadsModal()
        getLeadModal.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadModal.message = errorMessage
        Log.e("API Error", getLeadModal.message!!)
        getLeadMutableLiveData1!!.postValue(getLeadModal)
    }


    var postReferLinkMutableLiveData: MutableLiveData<getRefferalLink?>? =
        null

    fun postReferLinkLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        referral_type: String

    ): LiveData<getRefferalLink?> {

        postReferLinkMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.createReferAndShareLink(
                client_number,
                device_number,
                accessToken,
                referral_type
            ).enqueue(object : Callback<getRefferalLink?> {
                override fun onResponse(
                    call: Call<getRefferalLink?>,
                    response: Response<getRefferalLink?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        postReferLinkMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 201) {
                            //CommonUtils.toast(activity, response.message())

                        } else {
                            CommonUtils.toast(
                                activity,
                                "Generating Payment Link not  Successfully"
                            )
                        }


                    } else {
                        val apiError = parseError(response)
                        handleErrorRefferalLink(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(
                    call: Call<getRefferalLink?>,
                    t: Throwable
                ) {
                    CommonUtils.dismissProgress()
                    handleErrorLink(0, "Network error: " + t.message)
                }


            })
        } else {
            handleErrorRefferalLink(0, "No internet connection.")
        }
        return postReferLinkMutableLiveData!!
    }

    private fun handleErrorRefferalLink(code: Int, backendMessage: String?) {
        var getRefferalLinkResponse = getRefferalLink()
        getRefferalLinkResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getRefferalLinkResponse.message = errorMessage
        Log.e("API Error", getRefferalLinkResponse.message!!)
        postReferLinkMutableLiveData!!.postValue(
            getRefferalLinkResponse
        )
    }


    var postBecomeAScoutMutableLiveData: MutableLiveData<BecomeaScout?>? =
        null

    fun postBecomeaScoutData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String

    ): LiveData<BecomeaScout?> {

        postBecomeAScoutMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.BecomeaScout(
                client_number,
                device_number,
                accessToken,
                identifier
            )?.enqueue(object : Callback<BecomeaScout?> {
                override fun onResponse(
                    call: Call<BecomeaScout?>,
                    response: Response<BecomeaScout?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        postBecomeAScoutMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 200) {
                            //CommonUtils.toast(activity, response.message())

                        } else {
                            CommonUtils.toast(
                                activity,
                                response.message()
                            )
                        }


                    } else {
                        val apiError = parseError(response)
                        handleErrorBecomeScout(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(
                    call: Call<BecomeaScout?>,
                    t: Throwable
                ) {
                    CommonUtils.dismissProgress()
                    handleErrorLink(0, "Network error: " + t.message)
                }


            })
        } else {
            handleErrorBecomeScout(0, "No internet connection.")
        }
        return postBecomeAScoutMutableLiveData!!
    }

    private fun handleErrorBecomeScout(code: Int, backendMessage: String?) {
        var BecomeaScoutResponse = BecomeaScout()
        BecomeaScoutResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        BecomeaScoutResponse.message = errorMessage
        Log.e("API Error", BecomeaScoutResponse.message!!)
        postBecomeAScoutMutableLiveData!!.postValue(
            BecomeaScoutResponse
        )
    }

    var webinarsMutableLiveData1: MutableLiveData<getWebinarsResponse?>? = null
    fun getWebinarsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        type: String? = null
    ): LiveData<getWebinarsResponse?> {

        webinarsMutableLiveData1 = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            // CommonUtils.showProgress(activity)
            apiInterface.getWebinars(
                client_number,
                device_number,
                refreshToken,
                page,
                perPage,
                type,
            ).enqueue(object : Callback<getWebinarsResponse?> {
                override fun onResponse(
                    call: Call<getWebinarsResponse?>,
                    response: Response<getWebinarsResponse?>
                ) {
                    // CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        webinarsMutableLiveData1!!.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleWebinarsError(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<getWebinarsResponse?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleWebinarsError(0, "Network error: " + t.message)
                }
            })
        } else {
            //handleError(0, "No internet connection.")
        }
        return webinarsMutableLiveData1!!
    }

    private fun handleWebinarsError(code: Int, backendMessage: String?) {
        val getWebinarsResponse = getWebinarsResponse()
        getWebinarsResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getWebinarsResponse.message = errorMessage
        Log.e("API Error", getWebinarsResponse.message!!)
        webinarsMutableLiveData1!!.postValue(getWebinarsResponse)
    }


    var postAttendeMutableLiveData: MutableLiveData<CreateAttende?>? =
        null

    fun postAttendeLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        webinar_event_identifier: String,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        attendee_type: String

    ): LiveData<CreateAttende?> {

        postAttendeMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.createattende(
                client_number,
                device_number,
                accessToken,
                webinar_event_identifier,
                firstName,
                lastName,
                email,
                phone,
                attendee_type
            ).enqueue(object : Callback<CreateAttende?> {
                override fun onResponse(
                    call: Call<CreateAttende?>,
                    response: Response<CreateAttende?>,
                ) {

                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        postAttendeMutableLiveData!!.postValue(response.body())

                        if (response!!.body()!!.statusCode == 200) {
                            //CommonUtils.toast(activity, response.message())

                        } else {
                            CommonUtils.toast(
                                activity,
                                "not  Successfully"
                            )
                        }


                    } else {
                        val apiError = parseError(response)
                        handleCreateAttende(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(
                    call: Call<CreateAttende?>,
                    t: Throwable
                ) {
                    CommonUtils.dismissProgress()
                    handleCreateAttende(0, "Network error: " + t.message)
                }


            })
        } else {
            handleCreateAttende(0, "No internet connection.")
        }
        return postAttendeMutableLiveData!!
    }

    private fun handleCreateAttende(code: Int, backendMessage: String?) {
        var CreateAttendeResponse = CreateAttende()
        CreateAttendeResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        CreateAttendeResponse.message = errorMessage
        Log.e("API Error", CreateAttendeResponse.message!!)
        postAttendeMutableLiveData!!.postValue(
            CreateAttendeResponse
        )
    }


    //get Ambassadors list


    var getAmbassadorsLiveData: MutableLiveData<AmbassadorModal?>? = null
    fun getAmbassadorLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        dataperPage: Int,
        type_id: Int,
        sort_by: String,
        sort: String,
        search: String
    ): LiveData<AmbassadorModal?> {

        getAmbassadorsLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getAmbassadors(
                client_number,
                device_number,
                accessToken,
                page,
                dataperPage, type_id, sort_by, sort, search
            ).enqueue(object : Callback<AmbassadorModal?> {
                override fun onResponse(
                    call: Call<AmbassadorModal?>,
                    response: Response<AmbassadorModal?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getAmbassadorsLiveData!!.postValue(response.body())
                        Log.d("onResponseData", response.body()?.data.toString())
                    } else {
                        val apiError = parseError(response)
                        handleErrorAmbassador(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AmbassadorModal?>, t: Throwable) {
                    handleErrorAmbassador(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorAmbassador(0, "No internet connection.")
        }
        return getAmbassadorsLiveData!!
    }


    private fun handleErrorAmbassador(code: Int, backendMessage: String?) {
        var getLeadModal = AmbassadorModal()
        getLeadModal.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadModal.message = errorMessage
        Log.e("API Error", getLeadModal.message!!)
        getAmbassadorsLiveData!!.postValue(getLeadModal)
    }


    //join ambassador chat


    var JoinAmbassadroChatMutableLiveData: MutableLiveData<JoinAmbassadorChatModal?>? = null
    fun joinAmbassadroChatLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        ambassador_id: String,
    ): LiveData<JoinAmbassadorChatModal?> {

        JoinAmbassadroChatMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            CommonUtils.showProgress(activity)
            apiInterface.joinChatAmbassador(
                client_number,
                device_number,
                accessToken,
                ambassador_id
            ).enqueue(object : Callback<JoinAmbassadorChatModal?> {
                override fun onResponse(
                    call: Call<JoinAmbassadorChatModal?>,
                    response: Response<JoinAmbassadorChatModal?>
                ) {
                    CommonUtils.dismissProgress()
                    if (response.isSuccessful && response.body() != null) {
                        JoinAmbassadroChatMutableLiveData!!.postValue(response.body())
                        Log.d("onResponseData", response.body()?.data.toString())
                    } else {
                        val apiError = parseError(response)
                        handleErrorAmbassadorJoin(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<JoinAmbassadorChatModal?>, t: Throwable) {
                    CommonUtils.dismissProgress()
                    handleErrorAmbassadorJoin(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorAmbassadorJoin(0, "No internet connection.")
        }
        return JoinAmbassadroChatMutableLiveData!!
    }


    private fun handleErrorAmbassadorJoin(code: Int, backendMessage: String?) {
        var getLeadModal = JoinAmbassadorChatModal()
        getLeadModal.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadModal.message = errorMessage
        Log.e("API Error", getLeadModal.message!!)
        JoinAmbassadroChatMutableLiveData!!.postValue(getLeadModal)
    }


    //get Ambassadors list


    var getAmbassadorsChatLiveData: MutableLiveData<AmbassadorChatListModal?>? = null
    fun getMyAmbassadorChatLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        dataperPage: Int,
        sort: String,
        sort_by: String,
    ): LiveData<AmbassadorChatListModal?> {

        getAmbassadorsChatLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getMyAmbassadorsChat(
                client_number,
                device_number,
                accessToken,
                page,
                dataperPage, sort, sort_by
            )!!.enqueue(object : Callback<AmbassadorChatListModal?> {
                override fun onResponse(
                    call: Call<AmbassadorChatListModal?>,
                    response: Response<AmbassadorChatListModal?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getAmbassadorsChatLiveData!!.postValue(response.body())
                        Log.d("onResponseData", response.body()?.data.toString())
                    } else {
                        val apiError = parseError(response)
                        handleErrorAmbassadorChat(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AmbassadorChatListModal?>, t: Throwable) {
                    handleErrorAmbassadorChat(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorAmbassadorChat(0, "No internet connection.")
        }
        return getAmbassadorsChatLiveData!!
    }


    private fun handleErrorAmbassadorChat(code: Int, backendMessage: String?) {
        var getLeadModal = AmbassadorChatListModal()
        getLeadModal.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadModal.message = errorMessage
        Log.e("API Error", getLeadModal.message!!)
        getAmbassadorsChatLiveData!!.postValue(getLeadModal)
    }


    //add conversation ambassador chat


    var addConversationAmbassadroChatMutableLiveData: MutableLiveData<AmbassadorAddConversation?>? =
        null

    fun addConversationAmbassadorLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        relation_identifier: String,
        content: String,
    ): LiveData<AmbassadorAddConversation?> {

        addConversationAmbassadroChatMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.addConversationAmbassador(
                client_number,
                device_number,
                accessToken,
                relation_identifier, content
            ).enqueue(object : Callback<AmbassadorAddConversation?> {
                override fun onResponse(
                    call: Call<AmbassadorAddConversation?>,
                    response: Response<AmbassadorAddConversation?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        addConversationAmbassadroChatMutableLiveData!!.postValue(response.body())
                        Log.d("onResponseData", response.body()?.data.toString())
                    } else {
                        val apiError = parseError(response)
                        handleErrorAmbassadorAdd(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AmbassadorAddConversation?>, t: Throwable) {
                    handleErrorAmbassadorAdd(0, "Network error: " + t.message)
                }
            })
        } else {
            handleErrorAmbassadorAdd(0, "No internet connection.")
        }
        return addConversationAmbassadroChatMutableLiveData!!
    }


    private fun handleErrorAmbassadorAdd(code: Int, backendMessage: String?) {
        var getLeadModal = AmbassadorAddConversation()
        getLeadModal.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: "$code"
            else -> backendMessage ?: "Error $code"
        }
        getLeadModal.message = errorMessage
        Log.e("API Error", getLeadModal.message!!)
        addConversationAmbassadroChatMutableLiveData!!.postValue(getLeadModal)
    }


    var sendFcmToekenMutableLiveData: MutableLiveData<TokenFcmData?>? = null

    fun sendFcmTokenLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        fcmToken: String
    ): LiveData<TokenFcmData?> {

        sendFcmToekenMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.sendFcmToken(client_number, device_number, accessToken, fcmToken)
                .enqueue(object : Callback<TokenFcmData?> {
                    override fun onResponse(
                        call: Call<TokenFcmData?>,
                        response: Response<TokenFcmData?>,
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            sendFcmToekenMutableLiveData!!.postValue(response.body())

                        } else {
                            val apiError = parseError(response)
                            handleErrorFcm(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<TokenFcmData?>, t: Throwable) {
                        handleErrorFcm(0, "Network error: " + t.message)
                    }


                })
        } else {
            handleErrorFcm(0, "No internet connection.")
        }
        return sendFcmToekenMutableLiveData!!
    }

    private fun handleErrorFcm(code: Int, backendMessage: String?) {
        var createCounsellingModelResponse = TokenFcmData()
        createCounsellingModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        createCounsellingModelResponse.message = errorMessage
        Log.e("API Error", createCounsellingModelResponse.message!!)
        sendFcmToekenMutableLiveData!!.postValue(createCounsellingModelResponse)
    }

    //logout user

    var logoutUserMutableLiveData: MutableLiveData<Logout?>? = null

    fun logoutLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<Logout?> {

        logoutUserMutableLiveData = MutableLiveData()

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.logoutUser(client_number, device_number, accessToken)
                .enqueue(object : Callback<Logout?> {
                    override fun onResponse(
                        call: Call<Logout?>,
                        response: Response<Logout?>,
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            logoutUserMutableLiveData!!.postValue(response.body())

                        } else {
                            val apiError = parseError(response)
                            handleErrorLogout(response.code(), getErrorMessage(apiError))
                        }
                    }

                    override fun onFailure(call: Call<Logout?>, t: Throwable) {
                        handleErrorLogout(0, "Network error: " + t.message)
                    }


                })
        } else {

            handleErrorLogout(0, "No internet connection.")
        }

        return logoutUserMutableLiveData!!
    }


    private fun handleErrorLogout(code: Int, backendMessage: String?) {
        val createCounsellingModelResponse = Logout()
        createCounsellingModelResponse.statusCode = code
        val errorMessage: String = when (code) {
            401 -> backendMessage ?: "Please check your credentials."
            500 -> backendMessage ?: ""
            else -> backendMessage ?: "Error $code"
        }
        createCounsellingModelResponse.message = errorMessage
        Log.e("API Error", createCounsellingModelResponse.message!!)
        logoutUserMutableLiveData!!.postValue(createCounsellingModelResponse)
    }
}
