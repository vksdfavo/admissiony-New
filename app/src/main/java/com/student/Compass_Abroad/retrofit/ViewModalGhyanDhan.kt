package com.student.Compass_Abroad.retrofit

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.modal.LoanRequestBody
import com.student.Compass_Abroad.modal.loanApply.LoanAppliedModal
import com.student.Compass_Abroad.retrofit.RetrofitClientGhyanghan.retrofitCallerObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModalGhyanDhan  : ViewModel()  {

    var apiInterface = retrofitCallerObject!!.create(ApiInterface::class.java)

    fun appliedLoad(
        activity: Activity?,
        client_number: String,
        accessToken: String,
        name: String,
        email: String,
        mobile_number: String,
        amount: String,
        target_country_code: String,
        application_status: String
    ): LiveData<LoanAppliedModal?> {

        val liveData = MutableLiveData<LoanAppliedModal?>()

        activity?.let { act ->
            val apiErrorHandler = ApiErrorHandler(act.applicationContext)

            if (CommonUtils.isNetworkConnected(act)) {
                val requestBody = LoanRequestBody(
                    name = name,
                    email = email,
                    mobile_number = mobile_number,
                    amount = amount,
                    target_country_code = target_country_code,
                    application_status = application_status
                )

                apiInterface.loanApplied(
                    client_number,
                    "Bearer $accessToken",
                    requestBody
                ).enqueue(object : Callback<LoanAppliedModal?> {
                    override fun onResponse(
                        call: Call<LoanAppliedModal?>,
                        response: Response<LoanAppliedModal?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveData.postValue(response.body())
                        } else {
                            val errorMsg = apiErrorHandler.handleError(HttpException(response))
                            liveData.postValue(
                                LoanAppliedModal(
                                    data = null,
                                    success = false,
                                    message = errorMsg
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<LoanAppliedModal?>, t: Throwable) {
                        val errorMsg = apiErrorHandler.handleError(t)
                        liveData.postValue(
                            LoanAppliedModal(
                                data = null,
                                success = false,
                                message = errorMsg
                            )
                        )
                    }
                })
            } else {
                val errorMsg = apiErrorHandler.handleError(IOException("No internet connection"))
                liveData.postValue(
                    LoanAppliedModal(
                        data = null,
                        success = false,
                        message = errorMsg
                    )
                )
            }
        }

        return liveData
    }
}