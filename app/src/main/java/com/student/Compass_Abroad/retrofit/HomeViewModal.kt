package com.student.Compass_Abroad.retrofit

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.student.Compass_Abroad.ApiResponseForm
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.fragments.SubmitRequest
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.BecomeScoutModel.BecomeaScout
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.createAttende.CreateAttende
import com.student.Compass_Abroad.modal.createRefreralLink.getRefferalLink
import com.student.Compass_Abroad.modal.generatingPaymentLinkVoucher.generatingPaymentLinkVoucher
import com.student.Compass_Abroad.modal.getBannerModel.GetBannerModal
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.GetOffersandUpdates
import com.student.Compass_Abroad.modal.getPaymentApplicationPay.GetPaymentApplicationPay
import com.student.Compass_Abroad.modal.getScholarships.GetScholarships
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.modal.inDemandCourse.InDemandCourse
import com.student.Compass_Abroad.modal.in_demandInstitution.InDemandInstitution
import com.student.Compass_Abroad.modal.preferCountryList.GetPreferCountryList
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.modal.top_destinations.TopDestinations
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import com.student.Compass_Abroad.retrofit.RetrofitClient2.retrofitCallerObject2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HomeViewModal : ViewModel(){
    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)
    var apiInterface2 = retrofitCallerObject2!!.create(ApiInterface::class.java)
    private val _clientEventsCache = MutableLiveData<ClientEventResponse?>()
    private val _bannersCache = MutableLiveData<GetBannerModal?>()
    private val _recommendedProgramsCache = MutableLiveData<AllProgramModel?>()
    private val _vouchersCache = MutableLiveData<getVouchers?>()
    private val _offersUpdatesCache = MutableLiveData<GetOffersandUpdates?>()
    private val _scholarshipsCache = MutableLiveData<GetScholarships?>()
    private val _topDestinationCache = MutableLiveData<TopDestinations?>()
    private var isTopDestinationLoaded = false
    private val _inDemandInstitutionCache = MutableLiveData<InDemandInstitution?>()
    private var isInDemandInstitutionLoaded = false

    private val _staffProfileCache = MutableLiveData<StaffProfileModal?>()
    private var isStaffProfileLoaded = false

    private val _inDemandCourseCache = MutableLiveData<InDemandCourse?>()
    private var isInDemandCourseLoaded = false

    private val _disciplineCache = MutableLiveData<GetPreferCountryList?>()
    private var isDisciplineLoaded = false



    // Loading flags
    private var isClientEventsLoaded = false
    private var isBannersLoaded = false
    private var isRecommendedProgramsLoaded = false
    private var isVouchersLoaded = false
    private var isWebinarsLoaded = false
    private var isOffersUpdatesLoaded = false
    private var isScholarshipsLoaded = false

    private val _allProgramsCache = MutableLiveData<AllProgramModel?>()
    private var isAllProgramsLoaded = false

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
        forceRefresh: Boolean = false
    ): LiveData<AllProgramModel?> {

        val liveData = MutableLiveData<AllProgramModel?>()

        // ✔ Return cached data if already loaded and not forcing refresh
        if (isAllProgramsLoaded && !forceRefresh && _allProgramsCache.value != null) {
            liveData.postValue(_allProgramsCache.value)
            return liveData
        }

        if (activity == null) {
            liveData.postValue(
                AllProgramModel(
                    statusCode = 0,
                    message = "Invalid activity reference",
                    data = null
                )
            )
            return liveData
        }

        if (!CommonUtils.isNetworkConnected(activity)) {
            liveData.postValue(
                AllProgramModel(
                    statusCode = 0,
                    message = "No internet connection",
                    data = null
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)
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
            english_level_id,
            age,
            isrecommended
        )?.enqueue(object : Callback<AllProgramModel?> {

            override fun onResponse(
                call: Call<AllProgramModel?>,
                response: Response<AllProgramModel?>
            ) {

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!

                    // ✔ Save in cache
                    _allProgramsCache.postValue(body)
                    isAllProgramsLoaded = true

                    liveData.postValue(body)

                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        AllProgramModel(
                            statusCode = response.code(),
                            message = errorMsg,
                            data = null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                val errorMsg = apiErrorHandler.handleError(t)
                liveData.postValue(
                    AllProgramModel(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        })

        return liveData
    }


    fun getDisciplineDataList(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        forceRefresh: Boolean = false

    ): LiveData<GetPreferCountryList?> {

        val liveData = MutableLiveData<GetPreferCountryList?>()

        // ✔ Return cached data if already loaded
        if (isDisciplineLoaded && _disciplineCache.value != null) {
            liveData.postValue(_disciplineCache.value)
            return liveData
        }

        if (activity == null) {
            liveData.postValue(
                GetPreferCountryList().apply {
                    statusCode = 0
                    message = "Activity is null"
                }
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                GetPreferCountryList().apply {
                    statusCode = 0
                    message = errorMsg
                }
            )
            return liveData
        }

        CommonUtils.showProgress(activity)

        apiInterface2.getDisciplineList(clientNumber, deviceNumber, accessToken)
            ?.enqueue(object : Callback<GetPreferCountryList?> {

                override fun onResponse(
                    call: Call<GetPreferCountryList?>,
                    response: Response<GetPreferCountryList?>
                ) {
                    CommonUtils.dismissProgress()

                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!

                        // ✔ Save to cache
                        _disciplineCache.postValue(body)
                        isDisciplineLoaded = true

                        // Return to caller
                        liveData.postValue(body)

                        if (body.statusCode != 200) {
                            CommonUtils.toast(activity, "Not Found")
                        }
                    } else {
                        val errorMsg = apiErrorHandler.handleError(HttpException(response))
                        liveData.postValue(
                            GetPreferCountryList().apply {
                                statusCode = response.code()
                                message = errorMsg
                            }
                        )
                    }
                }

                override fun onFailure(call: Call<GetPreferCountryList?>, t: Throwable) {
                    CommonUtils.dismissProgress()

                    val errorMsg = apiErrorHandler.handleError(t)
                    liveData.postValue(
                        GetPreferCountryList().apply {
                            statusCode = 0
                            message = errorMsg
                        }
                    )
                }
            })

        return liveData
    }




    fun clientEventsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        forceRefresh: Boolean = false
    ): LiveData<ClientEventResponse?> {

        // Return cached data if available and not forcing refresh
        if (isClientEventsLoaded && !forceRefresh) {
            return _clientEventsCache
        }

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                CommonUtils.showProgress(act)

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
                            _clientEventsCache.postValue(response.body())
                            isClientEventsLoaded = true
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _clientEventsCache.postValue(
                                ClientEventResponse(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<ClientEventResponse?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        val errorMsg = apiErrorHandler.handleError(t)
                        _clientEventsCache.postValue(
                            ClientEventResponse(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _clientEventsCache.postValue(
                    ClientEventResponse(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _clientEventsCache
    }

    fun getVouchersModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        forceRefresh: Boolean = false
    ): LiveData<getVouchers?> {

        if (isVouchersLoaded && !forceRefresh) {
            return _vouchersCache
        }

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                CommonUtils.showProgress(act)

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
                            _vouchersCache.postValue(response.body())
                            isVouchersLoaded = true
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _vouchersCache.postValue(
                                getVouchers(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<getVouchers?>, t: Throwable) {
                        CommonUtils.dismissProgress()
                        val errorMsg = apiErrorHandler.handleError(t)
                        _vouchersCache.postValue(
                            getVouchers(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _vouchersCache.postValue(
                    getVouchers(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _vouchersCache
    }


    fun getStaffProfileData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        forceRefresh: Boolean = false

    ): LiveData<StaffProfileModal?> {

        val liveData = MutableLiveData<StaffProfileModal?>()

        // ✔ Return cached data if already loaded
        if (isStaffProfileLoaded && _staffProfileCache.value != null) {
            liveData.postValue(_staffProfileCache.value)
            return liveData
        }

        if (activity == null) {
            liveData.postValue(
                StaffProfileModal(
                    statusCode = 0,
                    message = "Invalid activity reference",
                    data = null
                )
            )
            return liveData
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        // ✔ Internet check
        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            liveData.postValue(
                StaffProfileModal(
                    statusCode = 0,
                    message = errorMsg,
                    data = null
                )
            )
            return liveData
        }

        // (Optional) You can show loader if needed
        // CommonUtils.showProgress(activity)

        apiInterface.getStaffProfileData(
            client_number,
            device_number,
            accessToken,

        )!!.enqueue(object : Callback<StaffProfileModal?> {

            override fun onResponse(
                call: Call<StaffProfileModal?>,
                response: Response<StaffProfileModal?>
            ) {
                // CommonUtils.dismissProgress()

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    // ✔ Save in cache
                    _staffProfileCache.postValue(body)
                    isStaffProfileLoaded = true

                    liveData.postValue(body)

                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    liveData.postValue(
                        StaffProfileModal(
                            statusCode = response.code(),
                            message = errorMsg,
                            data = null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<StaffProfileModal?>, t: Throwable) {
                // CommonUtils.dismissProgress()
                val errorMsg = apiErrorHandler.handleError(t)
                liveData.postValue(
                    StaffProfileModal(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        })

        return liveData
    }



    fun getShortListModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        content: String,
    ): LiveData<ShortListResponse?> {

        val liveData = MutableLiveData<ShortListResponse?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.addShorListProgram(
                    client_number,
                    device_number,
                    accessToken,
                    content
                )!!.enqueue(object : Callback<ShortListResponse?> {

                    override fun onResponse(
                        call: Call<ShortListResponse?>,
                        response: Response<ShortListResponse?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                ShortListResponse(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<ShortListResponse?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            ShortListResponse(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    ShortListResponse(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }

    fun getOffersUpdatesTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<GetOffersandUpdates?> {

        if (isOffersUpdatesLoaded && !forceRefresh) {
            return _offersUpdatesCache
        }

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getOffersupdates(
                    client_number,
                    device_number,
                    accessToken
                )!!.enqueue(object : Callback<GetOffersandUpdates?> {

                    override fun onResponse(
                        call: Call<GetOffersandUpdates?>,
                        response: Response<GetOffersandUpdates?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            _offersUpdatesCache.postValue(response.body())
                            isOffersUpdatesLoaded = true
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _offersUpdatesCache.postValue(
                                GetOffersandUpdates(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<GetOffersandUpdates?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        _offersUpdatesCache.postValue(
                            GetOffersandUpdates(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _offersUpdatesCache.postValue(
                    GetOffersandUpdates(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _offersUpdatesCache
    }

    fun getScholarshipsTabsLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<GetScholarships?> {

        if (isScholarshipsLoaded && !forceRefresh) {
            return _scholarshipsCache
        }

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getScholarships(
                    client_number,
                    device_number,
                    accessToken
                )!!.enqueue(object : Callback<GetScholarships?> {

                    override fun onResponse(
                        call: Call<GetScholarships?>,
                        response: Response<GetScholarships?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            _scholarshipsCache.postValue(response.body())
                            isScholarshipsLoaded = true
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _scholarshipsCache.postValue(
                                GetScholarships(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<GetScholarships?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        _scholarshipsCache.postValue(
                            GetScholarships(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _scholarshipsCache.postValue(
                    GetScholarships(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _scholarshipsCache
    }

    fun getModeOFPaymentDropDownVoucherLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getVoucherPaymentMode?> {

        val liveData = MutableLiveData<getVoucherPaymentMode?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getModeOfPaymentDropdownVoucher(
                    client_number,
                    device_number,
                    accessToken,
                    "true",
                    "gateway"
                )!!.enqueue(object : Callback<getVoucherPaymentMode?> {

                    override fun onResponse(
                        call: Call<getVoucherPaymentMode?>,
                        response: Response<getVoucherPaymentMode?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                getVoucherPaymentMode(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<getVoucherPaymentMode?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            getVoucherPaymentMode(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    getVoucherPaymentMode(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }


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

        val liveData = MutableLiveData<generatingPaymentLinkVoucher?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

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
                ).enqueue(object : Callback<generatingPaymentLinkVoucher?> {

                    override fun onResponse(
                        call: Call<generatingPaymentLinkVoucher?>,
                        response: Response<generatingPaymentLinkVoucher?>
                    ) {

                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                            if (body.statusCode == 201) {
                                CommonUtils.toast(activity, "Generating Payment Link Successfully")
                            } else {
                                CommonUtils.toast(activity, "Generating Payment Link not Successfully")
                            }

                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                generatingPaymentLinkVoucher(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<generatingPaymentLinkVoucher?>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissProgress()
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            generatingPaymentLinkVoucher(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {

                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    generatingPaymentLinkVoucher(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }

    fun getApplicationPayLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
    ): LiveData<GetPaymentApplicationPay?> {

        val liveData = MutableLiveData<GetPaymentApplicationPay?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                CommonUtils.showProgress(activity)

                apiInterface.getPaymentApplicationPay(
                    client_number,
                    device_number,
                    accessToken,
                    identifier
                )!!.enqueue(object : Callback<GetPaymentApplicationPay?> {

                    override fun onResponse(
                        call: Call<GetPaymentApplicationPay?>,
                        response: Response<GetPaymentApplicationPay?>
                    ) {
                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                            if (body.statusCode != 200) {
                                CommonUtils.toast(activity, "Not Found")
                            }

                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                GetPaymentApplicationPay(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<GetPaymentApplicationPay?>,
                        t: Throwable
                    ) {
                        CommonUtils.dismissProgress()
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            GetPaymentApplicationPay(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                // No internet
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    GetPaymentApplicationPay(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }

    fun getAllRecommendedProgramsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        forceRefresh: Boolean = false
    ): LiveData<AllProgramModel?> {

        if (isRecommendedProgramsLoaded && !forceRefresh) {
            return _recommendedProgramsCache
        }

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getRecommended(
                    client_number,
                    device_number,
                    refreshToken,
                    page,
                    perPage
                )!!.enqueue(object : Callback<AllProgramModel?> {

                    override fun onResponse(
                        call: Call<AllProgramModel?>,
                        response: Response<AllProgramModel?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            _recommendedProgramsCache.postValue(response.body()!!)
                            isRecommendedProgramsLoaded = true
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _recommendedProgramsCache.postValue(
                                AllProgramModel(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<AllProgramModel?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        _recommendedProgramsCache.postValue(
                            AllProgramModel(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _recommendedProgramsCache.postValue(
                    AllProgramModel(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _recommendedProgramsCache
    }

    // top destination
    fun get_topdestination(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<TopDestinations?> {

        // Return cached data if already loaded
        if (isTopDestinationLoaded && !forceRefresh) {
            return _topDestinationCache
        }

        if (activity == null) {
            _topDestinationCache.postValue(
                TopDestinations(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return _topDestinationCache
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            _topDestinationCache.postValue(
                TopDestinations(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return _topDestinationCache
        }

        CommonUtils.showProgress(activity)

        apiInterface.getTopDestination(
            clientNumber,
            deviceNumber,
            accessToken
        )?.enqueue(object : Callback<TopDestinations?> {

            override fun onResponse(
                call: Call<TopDestinations?>,
                response: Response<TopDestinations?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful && response.body() != null) {
                    _topDestinationCache.postValue(response.body())
                    isTopDestinationLoaded = true
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    _topDestinationCache.postValue(
                        TopDestinations(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<TopDestinations?>, t: Throwable) {
                CommonUtils.dismissProgress()

                val errorMsg = apiErrorHandler.handleError(t)
                _topDestinationCache.postValue(
                    TopDestinations(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return _topDestinationCache
    }



    fun getBannerModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<GetBannerModal?> {

        if (isBannersLoaded && !forceRefresh) {
            return _bannersCache
        }

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getBanner(
                    client_number,
                    device_number,
                    accessToken
                )!!.enqueue(object : Callback<GetBannerModal?> {

                    override fun onResponse(
                        call: Call<GetBannerModal?>,
                        response: Response<GetBannerModal?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            _bannersCache.postValue(body)
                            isBannersLoaded = true  // Set cache true

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            _bannersCache.postValue(
                                GetBannerModal(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<GetBannerModal?>, t: Throwable) {

                        val errorMsg = apiErrorHandler.handleError(t)
                        _bannersCache.postValue(
                            GetBannerModal(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {

                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                _bannersCache.postValue(
                    GetBannerModal(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return _bannersCache
    }



    fun getWebinarsModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        refreshToken: String,
        page: Int,
        perPage: Int,
        type: String? = null
    ): LiveData<getWebinarsResponse?> {

        val liveData = MutableLiveData<getWebinarsResponse?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getWebinars(
                    client_number,
                    device_number,
                    refreshToken,
                    page,
                    perPage,
                    type
                ).enqueue(object : Callback<getWebinarsResponse?> {

                    override fun onResponse(
                        call: Call<getWebinarsResponse?>,
                        response: Response<getWebinarsResponse?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                getWebinarsResponse(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<getWebinarsResponse?>,
                        t: Throwable
                    ) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            getWebinarsResponse(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {
                // No internet case
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    getWebinarsResponse(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }


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

        val liveData = MutableLiveData<CreateAttende?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

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
                        response: Response<CreateAttende?>
                    ) {

                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                            if (body.statusCode != 200) {
                                CommonUtils.toast(activity, "Not Successfully")
                            }

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                CreateAttende(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<CreateAttende?>,
                        t: Throwable
                    ) {

                        CommonUtils.dismissProgress()
                        val errorMsg = apiErrorHandler.handleError(t)

                        liveData.postValue(
                            CreateAttende(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {

                // No internet
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    CreateAttende(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }


    fun postBecomeaScoutData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String
    ): LiveData<BecomeaScout?> {

        val liveData = MutableLiveData<BecomeaScout?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                CommonUtils.showProgress(activity)

                apiInterface.BecomeaScout(
                    client_number,
                    device_number,
                    accessToken,
                    identifier
                )?.enqueue(object : Callback<BecomeaScout?> {

                    override fun onResponse(
                        call: Call<BecomeaScout?>,
                        response: Response<BecomeaScout?>
                    ) {

                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                            if (body.statusCode != 200) {
                                CommonUtils.toast(activity, response.message())
                            }

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                BecomeaScout(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<BecomeaScout?>, t: Throwable) {

                        CommonUtils.dismissProgress()

                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            BecomeaScout(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {

                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    BecomeaScout(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }


    fun postReferLinkLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        referral_type: String
    ): LiveData<getRefferalLink?> {

        val liveData = MutableLiveData<getRefferalLink?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                CommonUtils.showProgress(activity)

                apiInterface.createReferAndShareLink(
                    client_number,
                    device_number,
                    accessToken,
                    referral_type
                ).enqueue(object : Callback<getRefferalLink?> {

                    override fun onResponse(
                        call: Call<getRefferalLink?>,
                        response: Response<getRefferalLink?>
                    ) {

                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                            if (body.statusCode != 201) {
                                CommonUtils.toast(activity, "Referral Link Generation Failed")
                            }

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                getRefferalLink(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<getRefferalLink?>, t: Throwable) {

                        CommonUtils.dismissProgress()

                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            getRefferalLink(
                                statusCode = 0,
                                message = errorMsg,
                                data = null
                            )
                        )
                    }
                })

            } else {

                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    getRefferalLink(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
    }

    fun clearCache() {
        isClientEventsLoaded = false
        isBannersLoaded = false
        isRecommendedProgramsLoaded = false
        _clientEventsCache.value = null
        _bannersCache.value = null
        _recommendedProgramsCache.value = null
    }

    // in-demandCourses

    fun get_in_demandCourses(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<InDemandCourse?> {

        // Already loaded? return cache
        if (isInDemandCourseLoaded && !forceRefresh) {
            return _inDemandCourseCache
        }

        if (activity == null) {
            _inDemandCourseCache.postValue(
                InDemandCourse(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return _inDemandCourseCache
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            _inDemandCourseCache.postValue(
                InDemandCourse(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return _inDemandCourseCache
        }

        CommonUtils.showProgress(activity)

        apiInterface.get_InDemandCourses(
            clientNumber,
            deviceNumber,
            accessToken
        )?.enqueue(object : Callback<InDemandCourse?> {

            override fun onResponse(
                call: Call<InDemandCourse?>,
                response: Response<InDemandCourse?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful && response.body() != null) {
                    _inDemandCourseCache.postValue(response.body())
                    isInDemandCourseLoaded = true
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    _inDemandCourseCache.postValue(
                        InDemandCourse(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<InDemandCourse?>, t: Throwable) {
                CommonUtils.dismissProgress()

                val errorMsg = apiErrorHandler.handleError(t)
                _inDemandCourseCache.postValue(
                    InDemandCourse(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return _inDemandCourseCache
    }


    fun get_in_demandInstitution(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String,
        forceRefresh: Boolean = false
    ): LiveData<InDemandInstitution?> {

        // Return cache if already loaded
        if (isInDemandInstitutionLoaded && !forceRefresh) {
            return _inDemandInstitutionCache
        }

        if (activity == null) {
            _inDemandInstitutionCache.postValue(
                InDemandInstitution(
                    statusCode = 0,
                    success = false,
                    message = "Invalid activity reference"
                )
            )
            return _inDemandInstitutionCache
        }

        val apiErrorHandler = ApiErrorHandler(activity.applicationContext)

        if (!CommonUtils.isNetworkConnected(activity)) {
            val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
            _inDemandInstitutionCache.postValue(
                InDemandInstitution(
                    statusCode = 0,
                    success = false,
                    message = errorMsg
                )
            )
            return _inDemandInstitutionCache
        }

        CommonUtils.showProgress(activity)

        apiInterface.get_InDemandInstitution(
            clientNumber,
            deviceNumber,
            accessToken
        )?.enqueue(object : Callback<InDemandInstitution?> {

            override fun onResponse(
                call: Call<InDemandInstitution?>,
                response: Response<InDemandInstitution?>
            ) {
                CommonUtils.dismissProgress()

                if (response.isSuccessful && response.body() != null) {
                    _inDemandInstitutionCache.postValue(response.body())
                    isInDemandInstitutionLoaded = true
                } else {
                    val errorMsg = apiErrorHandler.handleError(HttpException(response))
                    _inDemandInstitutionCache.postValue(
                        InDemandInstitution(
                            statusCode = response.code(),
                            success = false,
                            message = errorMsg
                        )
                    )
                }
            }

            override fun onFailure(call: Call<InDemandInstitution?>, t: Throwable) {
                CommonUtils.dismissProgress()

                val errorMsg = apiErrorHandler.handleError(t)
                _inDemandInstitutionCache.postValue(
                    InDemandInstitution(
                        statusCode = 0,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        })

        return _inDemandInstitutionCache
    }

    fun leadFormResponseLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        identifier: String,
    ): LiveData<ApiResponseForm?> {

        val liveData = MutableLiveData<ApiResponseForm?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                CommonUtils.showProgress(activity)

                apiInterface.getLeadForm(
                    client_number,
                    device_number,
                    accessToken, identifier
                )!!.enqueue(object : Callback<ApiResponseForm?> {

                    override fun onResponse(
                        call: Call<ApiResponseForm?>,
                        response: Response<ApiResponseForm?>
                    ) {

                        CommonUtils.dismissProgress()

                        if (response.isSuccessful && response.body() != null) {

                            liveData.postValue(response.body())

                        } else {

                            val errorMsg =
                                apiErrorHandler.handleError(HttpException(response))

                            liveData.postValue(
                                ApiResponseForm().apply {
                                    statusCode = response.code()
                                    message = errorMsg
                                }
                            )
                        }
                    }

                    override fun onFailure(call: Call<ApiResponseForm?>, t: Throwable) {

                        CommonUtils.dismissProgress()

                        val errorMsg = apiErrorHandler.handleError(t)

                        liveData.postValue(
                            ApiResponseForm().apply {
                                statusCode = 0
                                message = errorMsg
                            }
                        )
                    }
                })

            } else {

                val errorMsg =
                    apiErrorHandler.handleError(IOException("No internet connection"))

                liveData.postValue(
                    ApiResponseForm().apply {
                        statusCode = 0
                        message = errorMsg
                    }
                )
            }
        }

        return liveData
    }



    var submitDataStatus: MutableLiveData<Pair<Boolean, String?>>? = null

    fun signUpFormModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        content: SubmitRequest
    ): LiveData<Pair<Boolean, String?>> {

        submitDataStatus = MutableLiveData()
        activity?.let {
            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                apiInterface.submitLeadForm(client_number, device_number, accessToken, content)
                    ?.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                // ✅ Success (status 200 / 201)
                                submitDataStatus?.postValue(Pair(true, null))
                            } else {
                                // ❌ Error
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = apiErrorHandler.handleErrorFromBody(
                                    errorBody,
                                    response.code()
                                )
                                submitDataStatus?.postValue(Pair(false, errorMessage))
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            submitDataStatus?.postValue(Pair(false, errorMessage))
                        }
                    })
            } else {
                submitDataStatus?.postValue(Pair(false, "No internet connection"))
            }
        }

        return submitDataStatus!!
    }





}