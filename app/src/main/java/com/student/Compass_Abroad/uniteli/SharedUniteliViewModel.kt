package com.student.Compass_Abroad.uniteli

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.modal.ambassadroChatList.AmbassadorChatListModal
import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler.getErrorMessage
import com.student.Compass_Abroad.modal.errorHandle.ErrorHandler.parseError
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedUniteliViewModel : ViewModel() {

    private val _ambassadorChatLiveData = MutableLiveData<AmbassadorChatListModal?>()
    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)
    val ambassadorChatLiveData = MutableLiveData<AmbassadorChatListModal>()

    private var isDataLoaded = false

    fun getMyAmbassadorChatData(
        activity: Activity?,
        client_number: String,
        device_number: String,
        accessToken: String,
        page: Int,
        dataperPage: Int,
        sort: String,
        sort_by: String,
    ) {
        if (isDataLoaded) return // Prevent multiple calls

        if (activity?.let { CommonUtils.isNetworkConnected(it) } == true) {
            apiInterface.getMyAmbassadorsChat(
                client_number,
                device_number,
                accessToken,
                page,
                dataperPage,
                sort,
                sort_by
            )?.enqueue(object : Callback<AmbassadorChatListModal?> {
                override fun onResponse(
                    call: Call<AmbassadorChatListModal?>,
                    response: Response<AmbassadorChatListModal?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        isDataLoaded = true
                        _ambassadorChatLiveData.postValue(response.body())
                    } else {
                        val apiError = parseError(response)
                        handleError(response.code(), getErrorMessage(apiError))
                    }
                }

                override fun onFailure(call: Call<AmbassadorChatListModal?>, t: Throwable) {
                    handleError(0, "Network error: ${t.message}")
                }
            })
        } else {
            handleError(0, "No internet connection.")
        }
    }

    private fun handleError(code: Int, message: String?) {
        val errorResponse = AmbassadorChatListModal().apply {
            statusCode = code
            this.message = message ?: "Unknown error"
        }
        _ambassadorChatLiveData.postValue(errorResponse)
    }

    fun resetData() {
        isDataLoaded = false
        _ambassadorChatLiveData.value = null
    }
}
