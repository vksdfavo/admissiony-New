package com.student.Compass_Abroad.retrofit

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.modal.preferCountryList.GetPreferCountryList
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HomeViewModal : ViewModel(){
    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)

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


}