package com.student.Compass_Abroad.retrofit

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.BecomeScoutModel.BecomeaScout
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.createAttende.CreateAttende
import com.student.Compass_Abroad.modal.createRefreralLink.getRefferalLink
import com.student.Compass_Abroad.modal.generatingPaymentLinkVoucher.generatingPaymentLinkVoucher
import com.student.Compass_Abroad.modal.getBannerModel.getBannerModel
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.GetOffersandUpdates
import com.student.Compass_Abroad.modal.getPaymentApplicationPay.GetPaymentApplicationPay
import com.student.Compass_Abroad.modal.getScholarships.GetScholarships
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.modal.preferCountryList.GetPreferCountryList
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HomeViewModal : ViewModel(){
    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)

    private val _clientEventsCache = MutableLiveData<ClientEventResponse?>()
    private val _bannersCache = MutableLiveData<getBannerModel?>()
    private val _recommendedProgramsCache = MutableLiveData<AllProgramModel?>()
    private val _vouchersCache = MutableLiveData<getVouchers?>()
    private val _webinarsCache = MutableLiveData<getWebinarsResponse?>()
    private val _offersUpdatesCache = MutableLiveData<GetOffersandUpdates?>()
    private val _scholarshipsCache = MutableLiveData<GetScholarships?>()

    // Flags to track if data is already loaded
    private var isClientEventsLoaded = false
    private var isBannersLoaded = false
    private var isRecommendedProgramsLoaded = false
    private var isVouchersLoaded = false
    private var isWebinarsLoaded = false
    private var isOffersUpdatesLoaded = false
    private var isScholarshipsLoaded = false

    fun getDisciplineDataList(
        activity: Activity?,
        clientNumber: String,
        deviceNumber: String,
        accessToken: String
    ): LiveData<GetPreferCountryList?> {

        val liveData = MutableLiveData<GetPreferCountryList?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                CommonUtils.showProgress(act)

                apiInterface.getDisciplineList(clientNumber, deviceNumber, accessToken)
                    ?.enqueue(object : Callback<GetPreferCountryList?> {
                        override fun onResponse(
                            call: Call<GetPreferCountryList?>,
                            response: Response<GetPreferCountryList?>
                        ) {
                            CommonUtils.dismissProgress()

                            if (response.isSuccessful && response.body() != null) {
                                val body = response.body()!!
                                liveData.postValue(body)

                                if (body.statusCode != 200) {
                                    CommonUtils.toast(act, "Not Found")
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
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    GetPreferCountryList().apply {
                        statusCode = 0
                        message = errorMsg
                    }
                )
            }
        }

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
    ): LiveData<StaffProfileModal?> {

        val liveData = MutableLiveData<StaffProfileModal?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getStaffProfileData(
                    client_number,
                    device_number,
                    accessToken
                )!!.enqueue(object : Callback<StaffProfileModal?> {

                    override fun onResponse(
                        call: Call<StaffProfileModal?>,
                        response: Response<StaffProfileModal?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
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

            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    StaffProfileModal(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

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


    fun getBannerModalLiveData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
    ): LiveData<getBannerModel?> {

        val liveData = MutableLiveData<getBannerModel?>()

        activity?.let { act ->

            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {

                apiInterface.getBanner(
                    client_number,
                    device_number,
                    accessToken
                )!!.enqueue(object : Callback<getBannerModel?> {

                    override fun onResponse(
                        call: Call<getBannerModel?>,
                        response: Response<getBannerModel?>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val body = response.body()!!
                            liveData.postValue(body)

                        } else {

                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                getBannerModel(
                                    statusCode = response.code(),
                                    message = errorMsg,
                                    data = null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<getBannerModel?>, t: Throwable) {

                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            getBannerModel(
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
                    getBannerModel(
                        statusCode = 0,
                        message = errorMsg,
                        data = null
                    )
                )
            }
        }

        return liveData
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

}