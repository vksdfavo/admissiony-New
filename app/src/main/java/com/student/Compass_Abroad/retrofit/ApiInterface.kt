package com.student.Compass_Abroad.retrofit

import androidx.annotation.Keep
import com.student.Compass_Abroad.modal.getNotificationRead.getNotificationReadResponse
import com.student.Compass_Abroad.modal.getNotificationReadAll.getNotificationReadAllResponse
import com.student.Compass_Abroad.modal.getNotification.getNotificationResponse
import com.student.Compass_Abroad.ApiResponseForm
import com.student.firmliagent.modal.getLeadModel.getLeadResponse
import com.student.Compass_Abroad.ChatMessageModels
import com.student.Compass_Abroad.CreateApplicationRequest
import com.student.Compass_Abroad.SavePreferencesRequest
import com.student.Compass_Abroad.modal.inDemandCourse.InDemandCourse
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
import com.student.Compass_Abroad.modal.LoanRequestBody
import com.student.Compass_Abroad.modal.LoginResponseModel.LoginResponseModel
import com.student.Compass_Abroad.modal.PostLeadNotesResponse.postLeadNotesResponse
import com.student.Compass_Abroad.modal.PreferCollageModal.PreferCollageModal
import com.student.Compass_Abroad.modal.ProgramTags.ProgramTags
import com.student.Compass_Abroad.modal.ReportReasons.ReportReasonresponse
import com.student.Compass_Abroad.modal.SaveReviewResponse.SaveReviewResponse
import com.student.Compass_Abroad.modal.TokenFcmData.TokenFcmData
import com.student.Compass_Abroad.modal.UtmModal.UtmModalResponse
import com.student.Compass_Abroad.modal.admissionStatus.AdmissionStatus
import com.student.Compass_Abroad.modal.allFieldResponse.formAllFieldResponse
import com.student.Compass_Abroad.modal.ambassadorAddConversation.AmbassadorAddConversation
import com.student.Compass_Abroad.modal.ambassadorGetChat.AmbassadorGetChatData
import com.student.Compass_Abroad.modal.ambassadroChatList.AmbassadorChatListModal
import com.student.Compass_Abroad.modal.applicationProgramDetails.ApplicationProgramResponse
import com.student.Compass_Abroad.modal.campusModel.CampusModel
import com.student.Compass_Abroad.modal.changeStatusReminder.changeStatusReminder
import com.student.Compass_Abroad.modal.chatMessage.ChatMessageResponse
import com.student.Compass_Abroad.modal.checkUserModel.CheckUserModel
import com.student.Compass_Abroad.modal.cityModel.CityModel
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.counsellingModal.CounsellingResponse
import com.student.Compass_Abroad.modal.countryModel.CountryResponse
import com.student.Compass_Abroad.modal.createAttende.CreateAttende
import com.student.Compass_Abroad.modal.createCounsellingModel.createCounsellingModel
import com.student.Compass_Abroad.modal.createPostResponse.CreatePostResponse
import com.student.Compass_Abroad.modal.createRefreralLink.getRefferalLink
import com.student.Compass_Abroad.modal.createTimeSlots.SlotRequest
import com.student.Compass_Abroad.modal.createTimeSlots.SlotResponse
import com.student.Compass_Abroad.modal.deleteCommentResponse.DeleteCommentResponse
import com.student.Compass_Abroad.modal.deletePostResponse.DeletePostResponse
import com.student.Compass_Abroad.modal.deleteReplyModel.DeleteReplyResponse
import com.student.Compass_Abroad.modal.discipline.DisciplineModel
import com.student.Compass_Abroad.modal.documentType.DocumentTypeModal
import com.student.Compass_Abroad.modal.editProfile.EditProfile
import com.student.Compass_Abroad.modal.editProfile.UploadImages
import com.student.Compass_Abroad.modal.findAmbassadorModal.AmbassadorModal
import com.student.Compass_Abroad.modal.forgotPasswordModel.ForgotPasswordModel
import com.student.Compass_Abroad.modal.generatingPaymentLinkVoucher.generatingPaymentLinkVoucher
import com.student.Compass_Abroad.modal.generatingPaymentLinkforApplication.generatingPaymentLinkApplication
import com.student.Compass_Abroad.modal.getAllComments.getAllComments
import com.student.Compass_Abroad.modal.getAllPosts.getAllPostResponse
import com.student.Compass_Abroad.modal.getApplicationAssignedStaff.getApplicationAssignedStaff
import com.student.Compass_Abroad.modal.getApplicationDocuments.getApplicationDocuments
import com.student.Compass_Abroad.modal.getApplicationNotes.getApplicationNotes
import com.student.Compass_Abroad.modal.getApplicationRemider.getApplicationReminderResponse
import com.student.Compass_Abroad.modal.getApplicationResponse.getApplicationResponse
import com.student.Compass_Abroad.modal.getApplicationTimelineResponse.getApplicationTimelineResponse
import com.student.Compass_Abroad.modal.getBannerModel.getBannerModel
import com.student.Compass_Abroad.modal.getCategoryLeadStat.getCategoryLeadStat
import com.student.Compass_Abroad.modal.getCategoryProgramModel.getCategoryProgramModel
import com.student.Compass_Abroad.modal.getChatResponse.getChatResponse
import com.student.Compass_Abroad.modal.getCommentReplies.getCommentReplies
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.modal.getDestintionManager.getDestinationmanager
import com.student.Compass_Abroad.modal.getDocumentTypes.getDocumentTypes
import com.student.Compass_Abroad.modal.getHistoryListModel.getHistoryListModel
import com.student.Compass_Abroad.modal.getLeadCounsellings.getLeadCounsellings
import com.student.Compass_Abroad.modal.getLeadNotes.getLeadNotesResponse
import com.student.Compass_Abroad.modal.getLeadPaymentLinks.getLeadPaymentLinks
import com.student.Compass_Abroad.modal.getLeadReminderResponse.GetLeadReminderResponse
import com.student.Compass_Abroad.modal.getLeadShorlistedProgram.getLeadShortlistedProgram
import com.student.Compass_Abroad.modal.getLeadTimelineResponse.getLeadTimelineResponse
import com.student.Compass_Abroad.modal.getLeads.getLeadsModal
import com.student.Compass_Abroad.modal.getLeadsDocuments.getLeadsDocuments
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.GetOffersandUpdates
import com.student.Compass_Abroad.modal.getPaymentApplication.getPaymentApplication
import com.student.Compass_Abroad.modal.getPaymentApplicationPay.GetPaymentApplicationPay
import com.student.Compass_Abroad.modal.getPaymentForDropDown.getPaymentForDropDown
import com.student.Compass_Abroad.modal.getPaymentMode.getPaymentMode
import com.student.Compass_Abroad.modal.getProgramFilters.getProgramFIltersResponse
import com.student.Compass_Abroad.modal.getReviewList.getReviewList
import com.student.Compass_Abroad.modal.getScholarships.GetScholarships
import com.student.Compass_Abroad.modal.getStaffList.StaffDropdownResponse
import com.student.Compass_Abroad.modal.getStaffSlots.GetStaffSlots
import com.student.Compass_Abroad.modal.getStudentPref.GetStudentPreferences
import com.student.Compass_Abroad.modal.getSubWorkliiTabs.getSubWorkliiTabInfo
import com.student.Compass_Abroad.modal.getTestimonials.getTestimonials
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.modal.getVouchersHistoryTabs.getVouchersHistoryTabs
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.modal.getWorkliiTabs.getWorklliTabs
import com.student.Compass_Abroad.modal.in_demandInstitution.InDemandInstitution
import com.student.Compass_Abroad.modal.institutionModel.InstitutionModel
import com.student.Compass_Abroad.modal.intakeModel.IntakeModel
import com.student.Compass_Abroad.modal.joinAmbassadorChat.JoinAmbassadorChatModal
import com.student.Compass_Abroad.modal.likePost.LikeResponse
import com.student.Compass_Abroad.modal.loanApply.LoanAppliedModal
import com.student.Compass_Abroad.modal.logoutUser.Logout
import com.student.Compass_Abroad.modal.paymentDetails.ApplicationPaymentDetails
import com.student.Compass_Abroad.modal.postApplicationNotes.PostApplicationNotes
import com.student.Compass_Abroad.modal.postComment.PostComment
import com.student.Compass_Abroad.modal.postLeadReminder.postLeadReminder
import com.student.Compass_Abroad.modal.postLeadStatus.postLeadStatus
import com.student.Compass_Abroad.modal.preferCountryList.GetPreferCountryList
import com.student.Compass_Abroad.modal.reactionModel.REactionResponse
import com.student.Compass_Abroad.modal.refreshToken.RefreshTokenResonse
import com.student.Compass_Abroad.modal.replyModel.ReplyComment
import com.student.Compass_Abroad.modal.reportPost.ReportResponse
import com.student.Compass_Abroad.modal.saveApplicationDocuments.SaveDocumentsRequest
import com.student.Compass_Abroad.modal.saveApplicationDocuments.saveApplicationDocuments
import com.student.Compass_Abroad.modal.savePeferences.SavePreferences
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.modal.stateModel.stateModel
import com.student.Compass_Abroad.modal.studyLevelModel.StudyLevelModal
import com.student.Compass_Abroad.modal.submitSinUp.SubmitSinUpForm
import com.student.Compass_Abroad.modal.testScoreModel.TestScoreModel
import com.student.Compass_Abroad.modal.top_destinations.TopDestinations
import com.student.Compass_Abroad.modal.uploadDocuments.uploadDocuments
import com.student.Compass_Abroad.modal.verifyOtp.VerifyOtp
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

@Keep
interface ApiInterface {
    @FormUrlEncoded
    @POST("auth/check-user")
    fun checkUser(
        @Header("fi-client-number") fiClientNumber: String?,
        @Field("content") content: String?,
    ): Call<CheckUserModel?>?


    @FormUrlEncoded
    @PUT("auth/refresh-token")
    fun getRefreshToken(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("identity") identity: String?,

        ): Call<RefreshTokenResonse?>?

    @GET("client-events/getAll")
    fun getClientEvents(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("sort") sort: String,// Optional parameter with default value 25
        @Query("status") status: String, // Optional parameter with default value 25
        @Query("type") type: String,// Optional parameter with default value 25
        @Query("for") forr: String,// Optional parameter with default value 25
    ): Call<ClientEventResponse>

    @GET("webinar-event")
    fun getWebinars(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int, // Optional parameter with default value 1
        @Query("per_page") perPage: Int, // Optional parameter with default value 25
        @Query("type") type: String? = null,// Optional parameter with default value 25
    ): Call<getWebinarsResponse>


    @GET("voucher")
    fun getVouchers(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("ishide") ishide: String,
    ): Call<getVouchers?>


    @FormUrlEncoded
    @POST("auth/login")
    fun loginUser(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Field("content") content: String?,
    ): Call<LoginResponseModel?>


    @FormUrlEncoded
    @PUT("auth/forgot-password")
    fun forgetPassword(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") fiDeviceNumber: String?,
        @Field("content") content: String?,

        ): Call<ForgotPasswordModel?>


    @FormUrlEncoded
    @POST("auth/verify-otp")
    fun verifyOTP(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Field("content") content: String?,

        ): Call<VerifyOtp?>


    @GET("program-finder/all-programs")
    fun getAllPrograms(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int = 1, // Default value
        @Query("per_page") perPage: Int,
        @Query("country_id[]") country_id: List<String>? = null, // Nullable
        @Query("state_id[]") state_id: List<String>? = null, // Nullable
        @Query("city_id[]") city_id: List<String>? = null, // Nullable
        @Query("institution_id[]") institution_id: List<String>? = null, // Nullable
        @Query("is_pgwp") is_pgwp_available: String? = null, // Nullable
        @Query("study_level_id[]") study_level_id: List<String>? = null, // Nullable
        @Query("discipline_id[]") desicipline_id: List<String>? = null, // Nullable
        @Query("attendance_on") attendance: String? = null, // Nullable
        @Query("program_type") program_type: String? = null, // Nullable
        @Query("intake_id[]") intake_id: List<String>? = null, // Nullable
        @Query("lt_tuition_fee") lt_tuition_fee: String? = null,
        @Query("gt_tuition_fee") gt_tuition_fee: String? = null,
        @Query("lt_application_fee") lt_application_fee: String? = null,
        @Query("gt_application_fee") gt_application_fee: String? = null,
        @Query("search") search: String? = null,
        @Query("category") category: String? = null,
        @Query("hasAccommodation") accomodation: String? = null,
        @Query("englishLevel") english_level_id: String? = null,
        @Query("age") age: String? = null,
        @Query("is_recommened") is_recommened: String? = null,
    ): Call<AllProgramModel>


    @FormUrlEncoded
    @POST("program-finder/shortlist")
    fun addShorListProgram(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("content") content: String?,
    ): Call<ShortListResponse?>


    @GET("dropdowns/destination-countries-dropdown?number_only=true")
    fun getCountry(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<CountryResponse?>?


    @GET("dropdown/state")
    fun getState(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id") country_id: Int?,
    ): Call<stateModel?>?

    @GET("dropdown/city")
    fun getCity(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("state_id") state_id: Int?,
    ): Call<CityModel?>?

    @GET("dropdown/campus")
    fun getCampus(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("city_id") city_id: Int,
    ): Call<CampusModel?>?

    @GET("dropdown/studylevel")
    fun getStudyLevel(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<StudyLevelModal?>?

    @GET("dropdown/discipline")
    fun getdiscipline(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<DisciplineModel?>?

    @GET("dropdown/testscore")
    fun getTestScore(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<TestScoreModel?>?

    @GET("dropdown/intake")
    fun getIntake(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<IntakeModel?>?

    @GET("dropdowns/institutions?number_only=true")
    fun getInstitution(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id") countryId: String
    ): Call<InstitutionModel?>?

    @GET("program-finder")
    fun getshortListedPrograms(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int, // Optional parameter with default value 1
        @Query("per_page") perPage: Int, // Optional parameter with default value 25
        @Query("is_shortlisted") is_shortlisted: Boolean, // Optional parameter with default value 25
        @Query("category") category: String
    ): Call<AllProgramModel>


    @GET("program-finder/shortlisted_program")
    fun addShorListProgramAssignedByStaff(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int, // Optional parameter with default value 1
        @Query("per_page") perPage: Int, // Optional parameter with default value 25
        @Query("is_shortlisted") is_shortlisted: Boolean, // Optional parameter with default value 25
        @Query("category") category: String

    ): Call<AllProgramModel>

    @FormUrlEncoded
    @POST("community/posts/create")
    fun createPost(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("content") content: String?,
    ): Call<CreatePostResponse?>

    @GET("community/categories")
    fun getCategories(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<CategoriesResponse?>?

    @GET("community/posts")
    fun getAllPosts(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,

        ): Call<getAllPostResponse?>?

    @GET("community/posts")
    fun getMyPosts(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("tab_type") tab_type: String,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,

        ): Call<getAllPostResponse?>?

    @FormUrlEncoded
    @POST("community/posts/{postId}/comment")
    fun postComment(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postId") postId: String,
        @Field("content") content: String?,
    ): Call<PostComment?>


    @FormUrlEncoded
    @PUT("community/posts/{identifier}/modify")
    fun EditPost(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Field("content") content: String?,
    ): Call<EditPostResponse?>


    @DELETE("community/posts/{identifier}/delete")
    fun deletePost(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<DeletePostResponse?>

    @GET("community/report_reasons")
    fun getReasons(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<ReportReasonresponse?>?

    @FormUrlEncoded
    @POST("community/posts/{identifier}/report")
    fun reportPost(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Field("content") content: String?,
    ): Call<ReportResponse?>

    @GET("community/posts/{identifier}/comment")
    fun getAllComments(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,
    ): Call<getAllComments?>?

    @GET("community/posts/{postIdentifier}/comment/{commentIdentifier}")
    fun getAllCommentsReplies(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postIdentifier") postIdentifier: String,
        @Path("commentIdentifier") commentIdentifier: String,
    ): Call<getCommentReplies?>?

    @FormUrlEncoded
    @POST("community/posts/{identifier}/reaction")
    fun like(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Field("content") content: String?,

        ): Call<LikeResponse?>

    @GET("community/posts/{identifier}/reaction_users")
    fun getReactions(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("reaction_type") reaction_type: String,
    ): Call<REactionResponse?>?


    @FormUrlEncoded
    @POST("community/posts/{postId}/comment")
    fun postReply(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postId") postId: String,
        @Field("content") content: String?,
    ): Call<ReplyComment?>


    @FormUrlEncoded
    @PUT("community/posts/{postIdentifier}/comment/{commentIndentifier}")
    fun EditComment(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postIdentifier") postIdentifier: String,
        @Path("commentIndentifier") commentIndentifier: String,
        @Field("content") content: String?,
    ): Call<EditCommentResponse?>

    @DELETE("community/posts/{postIdentifier}/comment/{commentIdentifier}")
    fun deleteComment(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postIdentifier") postIdentifier: String,
        @Path("commentIdentifier") commentIdentifier: String,
    ): Call<DeleteCommentResponse?>

    @FormUrlEncoded
    @PUT("community/posts/{postIdentifier}/comment/{replyIdentifier}")
    fun EditReply(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postIdentifier") postIdentifier: String,
        @Path("replyIdentifier") replyIndentifier: String,
        @Field("content") content: String?,
    ): Call<EditReplyResponse?>

    @DELETE("community/posts/{postIdentifier}/comment/{ReplyIdentifier}")
    fun deleteReply(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("postIdentifier") postIdentifier: String,
        @Path("ReplyIdentifier") ReplyIdentifier: String,
    ): Call<DeleteReplyResponse?>

    @GET("landing_page/LFO1716112595567K56USAHJ02")
    fun getLeadForm(
        @Header("fi-client-number") fiClientNumber: String?,
    ): Call<ApiResponseForm?>?

    @FormUrlEncoded
    @POST("landing_page/create")
    fun submitLeadForm(
        @Header("fi-client-number") fiClientNumber: String?,
        @Field("content") content: String?,
    ): Call<SubmitSinUpForm?>?


    @GET
    fun getAllFields(
        @Url url: String,
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<formAllFieldResponse?>?

    @POST("{entity}/{identifier}/conversations")
    fun postChatMessage(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("entity") entity: String,
        @Path("identifier") identifier: String,
        @Body request: ChatMessageModels.ChatMessageRequest,
    ): Call<ChatMessageResponse?>


    @GET("{entity}/{identifier}/conversations")
    fun getChat(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Path("entity") entity: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
    ): Call<getChatResponse?>?


    @GET("{entity}/conversations/{identifier}/attachments")
    fun getChatAttachments(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Path("entity") entity: String,
    ): Call<getApplicationDocuments?>?

    @GET("leads")
    fun getLeads(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("lead_category") lead_category: String?,
        @Query("lead_stage_type") stage: String?,
        @Query("sort") sort: String,
    ): Call<getLeadResponse?>?

    /* @GET("leads/{id}/conversations")
     fun  chatStore(
     @Field("content") content : String?,
     @Field("type") type : String?,
     @Path("id") id:String,
     @Field("taggedUsers")taggedUsers : [String]?
     ):Call<getLeadResponse?>?*/

    @GET("leads/{identifier}/timeline")
    fun getLeadTimeLine(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getLeadTimelineResponse?>?

    @GET("leads/{identifier}/assigned-staff")
    fun getLeadAssignedStaff(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getApplicationAssignedStaff?>?


    @GET("tasks")
    fun getLeadReminder(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Query("type") type: String,
        @Query("category") category: String,
        @Query("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<GetLeadReminderResponse?>?


    @FormUrlEncoded
    @POST("tasks/create-reminder")
    fun postReminder(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("module_type") module_type: String,
        @Field("module_identifier") module_identifier: String?,
        @Field("scheduled_at") scheduled_at: String?,
        @Field("note") note: String?,
    ): Call<postLeadReminder?>

    @GET("leads/{identifier}/notes")
    fun getLeadNotes(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<getLeadNotesResponse?>?


    @GET("leads/{identifier}/documents")
    fun getLeadDocuments(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<getLeadsDocuments?>?

    @GET("leads/{identifier}/program_shortlist")
    fun getLeadShortlistedProgram(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getLeadShortlistedProgram?>?


    @GET("leads/counselings")
    fun getLeadCounselling(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("lead_identifier") lead_identifier: String,
    ): Call<getLeadCounsellings?>?

    @GET("fee-payments/leads/{identifier}/links")
    fun getLeadPaymentLinks(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getLeadPaymentLinks?>?


    @GET("applications")
    fun getApplications(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
        @Query("lead_identifier") lead_identifier: String?,
    ): Call<getApplicationResponse?>?


    @GET("fee-payments/transactions")
    fun getApplicationsPaymentDetails(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
        @Query("search") search: String,
    ): Call<ApplicationPaymentDetails?>?


    @GET("applications/{identifier}/assigned-staff")
    fun getApplicationAssignedStaff(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getApplicationAssignedStaff?>?


    @GET("applications/{identifier}/timeline")
    fun getApplicationTimeLine(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<getApplicationTimelineResponse?>?


    @GET("applications/{identifier}/notes")
    fun getApplicationNotes(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<getApplicationNotes?>?


    @FormUrlEncoded
    @POST("leads/{identifier}/notes")
    fun postLeadNotes(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Field("content") content: String?,
    ): Call<postLeadNotesResponse?>


    @FormUrlEncoded
    @POST("applications/{identifier}/notes")
    fun postApplicationNotes(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Field("content") content: String?,
    ): Call<PostApplicationNotes?>

    @GET("tasks")
    fun getApplicationReminder(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Query("type") type: String,
        @Query("category") category: String,
        @Query("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<getApplicationReminderResponse?>?


    @GET("applications/{identifier}/documents")
    fun getApplicationDocuments(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,

        ): Call<getApplicationDocuments?>?


    @GET("documents/types-dropdown")
    fun getDocumentsTypes(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getDocumentTypes?>?


    @Multipart
    @POST("files/upload-file")
    fun uploadDocuments(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Part("type") type: String?,
        @Part image: MultipartBody.Part,
    ): Call<uploadDocuments?>?


    @POST("documents/save-multiple/application/{identifier}")
    fun saveDocuments(
        @Header("fi-client-number") clientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Body request: SaveDocumentsRequest,
    ): Call<saveApplicationDocuments>


    @POST("documents/save-multiple/lead/{identifier}")
    fun saveDocumentsLead(
        @Header("fi-client-number") clientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Body request: SaveDocumentsRequest,  // Use the data class here
    ): Call<saveApplicationDocuments>

    @GET("fee-payments/applications/{identifier}/links")
    fun getPaymentApplication(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<getPaymentApplication?>?

    @GET("fee-payments/types-dropdown")
    fun getPaymentForDropdown(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getPaymentForDropDown?>?

    @GET("fee-payments/gateways")
    fun getModeOfPaymentDropdown(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getPaymentMode?>?


    @FormUrlEncoded
    @POST("fee-payments/links")
    fun generatingPaymentLinksApplication(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("module") module: String?,
        @Field("module_identifier") module_identifier: String?,
        @Field("price") price: String?,
        @Field("currency") currency: String?,
        @Field("payment_type_identifier") payment_type_identifier: String?,
        @Field("payment_gateway_identifier") payment_gateway_identifier: String?,
    ): Call<generatingPaymentLinkApplication?>

    @GET("leads/stats")
    fun getCategoryLeadStat(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("lead_category") lead_category: String?,
    ): Call<getCategoryLeadStat?>?

    @GET("reviews/{identifier}")
    fun getReviewList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
        @Query("status") status: String?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,
    ): Call<getReviewList?>?

    @FormUrlEncoded
    @PUT("leads/{identifier}/change_status")
    fun getLeadStatusUpdate(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
        @Field("status") status: String?,

        ): Call<postLeadStatus?>

    @GET("fee-payments/pay/{identifier}")
    fun getPaymentApplicationPay(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,

        ): Call<GetPaymentApplicationPay?>?

    @GET("dashboard/lead/lead_source")
    fun getLeadSource(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,

        ): Call<LeadSourceModal?>?


    @GET("dashboard/lead/destination_country")
    fun getLeadSourceDestinationCountry(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,

        ): Call<DestinationCountryModal?>?


    @GET("dashboard/lead/lead_medium")
    fun getLeadMediumData(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,

        ): Call<DestinationCountryModal?>?


    @GET("dashboard/lead/lead_branch")
    fun getLeadBranchData(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<DestinationCountryModal?>?


    @GET("account/profile")
    fun getStaffProfileData(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?
    ): Call<StaffProfileModal?>?

    @FormUrlEncoded
    @PUT("tasks/{identifier}/change-status")
    fun changeStatusReminder(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
        @Field("status") status: String?,
        @Field("has_follow_up") has_follow_up: String?,
        @Field("followup_at") followup_at: String,
        @Field("note") note: String?,

        ): Call<changeStatusReminder?>

    @GET("leads/counselings/managers")
    fun getDestinationManagerList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getDestinationmanager?>?

    @GET("dropdowns/destination-countries-dropdown?number_only=true")
    fun getDestinationCountryList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getDestinationCountry?>?


    @FormUrlEncoded
    @POST("leads/counselings/create")
    fun createcounsellings(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("content") content: String?,
    ): Call<createCounsellingModel?>


    @GET("leads/dropdowns")
    fun getStudentsList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetStudentResponse?>?

    @GET("dropdowns/lead_country")
    fun get_lead_country(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetStudentResponse?>?


    @GET("dropdowns/lead_state/{country}")
    fun get_lead_state(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("country") countryName: String?,
    ): Call<GetStudentResponse?>?

    @PUT("leads/timeline/{LeadId}/{LeadStatusId}")
    fun changeLeadStatusApi(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("LeadId") LeadId: String?,
        @Path("LeadStatusId") LeadStatusId: String?,
    ): Call<ChangeLeadStatus?>?


    @GET("dropdowns/institutions?number_only=true")
    fun getPreferCollageList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id") institution_id: String?,
    ): Call<PreferCollageModal?>?

    @GET("dropdowns/campuses")
    fun getCampusList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("institution_id") institution_id: String?,

        ): Call<GetCampusResponse?>?


    @GET("dropdowns/programs")
    fun getPreferCourseList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("campus_id") campus_id: String?,
        @Query("institution_id") institution_id: String?,
    ): Call<GetCampusResponse?>?


    @GET("dropdowns/lead_destination_country")
    fun getlead_destination_countryList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetStudentResponse?>?


    @GET("utm/sources-dropdown")
    fun getUtmSourceList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<UtmModalResponse?>?


    @GET("lead-stages/stages-dropdown")
    fun getTimelineStageList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<UtmModalResponse?>?


    @POST("applications/add-application")
    fun createApplication(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Body requestBody: CreateApplicationRequest
    ): Call<CreateApplicationModal?>


    @GET("tasks/tabs-info")
    fun getWorkliiTabs(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("type") type: String?,
    ): Call<getWorklliTabs?>?

    @GET("leads/counselings")
    fun getCounsellingResponse(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("lead_identifier") lead_identifier: String?,
        @Query("status") status: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,

        ): Call<CounsellingResponse?>?

    @GET("tasks")
    fun getSubWorkliiTabs(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("from") from: String?,
        @Query("type") type: String?,
        @Query("statuses") statuses: String?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?,
        @Query("sort") sort: String,
    ): Call<getSubWorkliiTabInfo?>?


    @GET("program-finder/program-detail")
    fun getApplicationProgramDetails(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id") country_id: String?,
        @Query("institution_id") institution_id: String?,
        @Query("program_id") program_id: String?,
        @Query("campus_id") campus_id: String?
    ): Call<ApplicationProgramResponse?>?

    @GET("recent_updates?type=in_house")
    fun getOffersupdates(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetOffersandUpdates?>?

    @GET("scholarship?type=in_house")
    fun getScholarships(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetScholarships?>?

    @FormUrlEncoded
    @PUT("account/update_password")
    fun changePassword(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") fiDeviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Field("old_password") old: String?,
        @Field("user_identifier") user: String?,
        @Field("confirm_password") confirm: String?,
        @Field("password") password: String?,
    ): Call<ForgotPasswordModel?>

    @DELETE("account/delete-student-profile")
    fun deleteAccount(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<DeletePostResponse?>


    @FormUrlEncoded
    @PUT("settings/profile-photo")
    fun uploadImage(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("profile_picture_url") profile_picture_url: String
    ): Call<UploadImages?>?


    @FormUrlEncoded
    @POST("settings/save-personalInfo")
    fun editUserProfile(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("gender") gender: String?,
        @Field("marital_status") marital_status: String?,
        @Field("birthday") birthday: String?,
    ): Call<EditProfile?>


    @GET("notifications?channel_identifier=NCH1735532757581F24HIDPH12&sort_by=id&sort=DESC")
    fun getNotification(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getNotificationResponse?>?

    @GET("notifications/read/all")
    fun getNotificationsReadAll(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getNotificationReadAllResponse?>?

    @GET("notifications/read/{identifier}")
    fun getNotificationsRead(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
    ): Call<getNotificationReadResponse?>?

    @GET("program-finder/all-filters")
    fun getCountryFilter(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @QueryMap options: Map<String, String>,
        @Query("category") data: String
    ): Call<getProgramFIltersResponse?>?

    @GET("program-finder/filters")
    fun getStateFilter(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id[]") countryId: String? = null,
    ): Call<getProgramFIltersResponse?>?

    @GET("program-finder/filters")
    fun getCityFilter(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("country_id[]") countryId: String? = null,
        @Query("state_id[]") stateId: String? = null
    ): Call<getProgramFIltersResponse?>?

    @GET("program-finder/filters")
    fun getTuitionFilter(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("lt_tuition_fee") lt_tuition_fee: Int,
        @Query("gt_tuition_fee") gt_tuition_fee: Int
    ): Call<getProgramFIltersResponse?>?


    @GET("program-finder/filters")
    fun getApplicationFilter(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("lt_application_fee") lt_Application_fee: Int,
        @Query("gt_application_fee") gt_Application_fee: Int
    ): Call<getProgramFIltersResponse?>?

    @GET("tags/dropdown?number_only=true")
    fun getProgramTags(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("type") type: String
    ): Call<ProgramTags?>?


    @GET("program-finder/category")
    fun getCategoryProgramStat(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getCategoryProgramModel?>?


    @FormUrlEncoded
    @POST("reviews/application/{identifier}")
    fun saveReview(
        @Header("fi-client-number") clientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
        @Field("user_identifier") userIdentifier: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("rating") rating: Int
    ): Call<SaveReviewResponse>


    @GET("fee-payments/gateways-dropdown")
    fun getModeOfPaymentDropdownVoucher(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("string_only") string_only: String,
        @Query("type") type: String
    ): Call<getVoucherPaymentMode?>?

    @FormUrlEncoded
    @POST("fee-payments/links")
    fun generatingPaymentLinksVoucher(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("currency") currency: String?,
        @Field("price") price: String?,
        @Field("quantity") quantity: String?,
        @Field("module") module: String?,
        @Field("module_identifier") module_identifier: String?,
        @Field("payment_type_identifier") payment_type_identifier: String?,
        @Field("payment_gateway_identifier") payment_gateway_identifier: String?,
    ): Call<generatingPaymentLinkVoucher?>


    @GET("dropdowns/destination-countries-dropdown?text_only=true&with_icons=true")
    fun getPreferCountryList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetPreferCountryList?>?

    @GET("dropdown/discipline?with_icons=true")
    fun getDisciplineList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetPreferCountryList?>?

    @PUT("settings/save-student-preferences")
    fun savePreferences(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Body request: SavePreferencesRequest
    ): Call<SavePreferences?>


    @GET("settings/student-preferences")
    fun getPreferences(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<GetStudentPreferences?>?

    @GET("voucher/history-tabs")
    fun getVouchersHistoryTabs(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getVouchersHistoryTabs?>?


    @GET("voucher/buy-voucher-history")
    fun getVouchersHistory(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("ishide") ishide: String?,
        @Query("sort_by") sort_by: String?,
        @Query("sort") sort: String?,
    ): Call<getHistoryListModel?>


    @GET("program-finder/all-programs?is_recommened=true")
    fun getRecommended(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int,
    ): Call<AllProgramModel>

    @GET("banner")
    fun getBanner(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getBannerModel>

    @GET("leads")
    fun getLeads(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int,
    ): Call<getLeadsModal>


    @FormUrlEncoded
    @POST("referrals/links")
    fun createReferAndShareLink(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("referral_type") referral_type: String?,
    ): Call<getRefferalLink?>


    @FormUrlEncoded
    @PUT("referrals/profile")
    fun BecomeaScout(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("lead_identifier") lead_identifier: String?,
    ): Call<BecomeaScout?>?


    @FormUrlEncoded
    @POST("webinar-event/add-attendee")
    fun createattende(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("webinar_event_identifier") webinar_event_identifier: String?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("contact_number") contact_number: String?,
        @Field("attendee_type") attendee_type: String?,
    ): Call<CreateAttende?>


    @GET("ambassadors")
    fun getAmbassadors(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int,
        @Query("type_id") type_id: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
        @Query("search") search: String,
    ): Call<AmbassadorModal>

    @GET("ambassadors/chat/list")
    fun getMyAmbassadorsChat(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("sort_by") sortBy: String,
    ): Call<AmbassadorChatListModal>


    @FormUrlEncoded
    @POST("ambassadors/join-chat")
    fun joinChatAmbassador(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("ambassador_id") ambassador_id: String?,
    ): Call<JoinAmbassadorChatModal?>

    @FormUrlEncoded
    @POST("ambassadors/add-conversation")
    fun addConversationAmbassador(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("relation_identifier") relation_identifier: String?,
        @Field("content") content: String?,
    ): Call<AmbassadorAddConversation?>

    @GET("ambassadors/chat/detail/{identifier}")
    fun getAmbassadorChat(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
    ): Call<AmbassadorGetChatData?>?

    @FormUrlEncoded
    @POST("account/register_fcmtoken")
    fun sendFcmToken(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("fcm_token") fcm_token: String?,
    ): Call<TokenFcmData?>


    @GET("auth/logout")
    fun logoutUser(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<Logout?>


    @GET("status/dropdown")
    fun admissionStatus(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<AdmissionStatus?>


    @POST("foreign_loans/partners/leads")
    fun loanApplied(
        @Header("client-id") clientId: String,
        @Header("Authorization") authorization: String,
        @Body body: LoanRequestBody
    ): Call<LoanAppliedModal?>


    @GET("documents/types-dropdown")
    fun getDocumentType(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("modules") modules: String?,

        ): Call<DocumentTypeModal?>?


    @FormUrlEncoded
    @POST("reviews/lead/{identifier}")
    fun saveReviewLead(
        @Header("fi-client-number") clientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
        @Field("user_identifier") userIdentifier: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("rating") rating: Int
    ): Call<SaveReviewResponse>

    @GET("applications/{identifier}/assigned-staff")
    fun getSingleApplicationsAssignStaff(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,
    ): Call<getApplicationAssignedStaff?>?


    @GET("staffs/staff/dropdown")
    fun getStaffList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("branch_id") identifier: String?,
        @Query("number_only") number_only: Boolean?,
    ): Call<StaffDropdownResponse?>?


    @GET("branches?paginate=false&branch_type=agent&number_only=true")
    fun getBranchList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getDestinationCountry?>?


    @GET("slot/gellSlots")
    fun getStaffSlots(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("date") date: String,
        @Query("branch_id") branch_id: String
    ): Call<GetStaffSlots>

    @FormUrlEncoded
    @POST("slot/bookSlot")
    fun bookSlotForUser(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Field("branch_id") branch_id: String,
        @Field("event_start_datetime") event_start_datetime: String,
        @Field("event_end_datetime") event_end_datetime: String,
        ): Call<SlotResponse>

    @GET("program-finder/dashboard-widget/top-destination/HighDemand")
    fun getTopDestination(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<TopDestinations>


    @GET("program-finder/dashboard-widget/in-demandCourses/HighDemand")
    fun get_InDemandCourses(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<InDemandCourse>


    @GET("program-finder/dashboard-widget/in-demandInstitution/HighDemand")
    fun get_InDemandInstitution(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<InDemandInstitution>

    @GET("testimonials")
    fun getTestimonials(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Query("is_visible") is_visible: Boolean,
        @Query("type") type: String,
    ): Call<getTestimonials>

}

