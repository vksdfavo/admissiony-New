package com.student.Compass_Abroad.Scout.retrofitScout

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.student.Compass_Abroad.Scout.modalClass.BankAccountRequest
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.GetAddedBankAccount
import com.student.Compass_Abroad.Scout.modalClass.getBankDetails.GetbankDetailsModal
import com.student.Compass_Abroad.Scout.modalClass.getBankForm.GetBankAccountForm
import com.student.Compass_Abroad.Scout.modalClass.primaryAccount.MakePrimaryAccount
import com.student.Compass_Abroad.Scout.modalClass.scoutsummary.ScoutSummaryModal
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.errorDialogOpen
import com.student.Compass_Abroad.errorHandle.ApiErrorHandler
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.RetrofitClient.retrofitCallerObject
import com.student.Compass_Abroad.retrofit.RetrofitClient2.retrofitCallerObject2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ViewModalScout {

    var apiInterfaceScout = retrofitCallerObject!!.create(ApiInterfaceScout::class.java)
    var apiInterfaceScout2 = retrofitCallerObject2!!.create(ApiInterfaceScout::class.java)

    /** ScoutSummaryLiveData**/

    var checkUserModalMutableLiveData: MutableLiveData<ScoutSummaryModal?>? = null

    fun ScoutSummaryLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
    ): LiveData<ScoutSummaryModal?> {
        checkUserModalMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {

                CommonUtils.showProgress(it)

                apiInterfaceScout.getScoutSummary(authorization, client_number, device_number)
                    .enqueue(object : Callback<ScoutSummaryModal?> {
                        override fun onResponse(
                            call: Call<ScoutSummaryModal?>,
                            response: Response<ScoutSummaryModal?>
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

                        override fun onFailure(call: Call<ScoutSummaryModal?>, t: Throwable) {
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


    /** get user bank details**/


    var addBankAccountMutableLiveData: MutableLiveData<GetAddedBankAccount?>? = null

    fun getBankAccountLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
        identifier: String,
    ): LiveData<GetAddedBankAccount?> {
        addBankAccountMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {

                CommonUtils.showProgress(it)

                apiInterfaceScout.getScoutBankDetails(authorization, client_number, device_number, identifier)
                    .enqueue(object : Callback<GetAddedBankAccount?> {
                        override fun onResponse(
                            call: Call<GetAddedBankAccount?>,
                            response: Response<GetAddedBankAccount?>
                        ) {
                            CommonUtils.dismissProgress()

                            if (response.isSuccessful && response.body() != null) {
                                addBankAccountMutableLiveData!!.postValue(response.body())
                            } else {
                                Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<GetAddedBankAccount?>, t: Throwable) {
                            CommonUtils.dismissProgress()
                            Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })
            } else {
                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return addBankAccountMutableLiveData!!
    }


    /** get all country Api**/


    var getAllCountryListMutableLiveData: MutableLiveData<getDestinationCountry?>? = null

    fun getAllCountryListLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
    ): LiveData<getDestinationCountry?> {
        getAllCountryListMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                apiInterfaceScout2.getAllCountryList(authorization, client_number, device_number)!!
                    .enqueue(object : Callback<getDestinationCountry?> {
                        override fun onResponse(
                            call: Call<getDestinationCountry?>,
                            response: Response<getDestinationCountry?>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                getAllCountryListMutableLiveData!!.postValue(response.body())
                            } else {
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<getDestinationCountry?>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })
            } else {
                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return getAllCountryListMutableLiveData!!
    }


    /** get all country Api**/


    var getBankAccountFormMutableLiveData: MutableLiveData<GetBankAccountForm?>? = null

    fun getBankAccountFormListLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
        identifier: String,
    ): LiveData<GetBankAccountForm?> {
        getBankAccountFormMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                apiInterfaceScout.getBankAccountForm(
                    authorization,
                    client_number,
                    device_number,
                    identifier
                )!!
                    .enqueue(object : Callback<GetBankAccountForm?> {
                        override fun onResponse(
                            call: Call<GetBankAccountForm?>,
                            response: Response<GetBankAccountForm?>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                getBankAccountFormMutableLiveData!!.postValue(response.body())
                            } else {
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<GetBankAccountForm?>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })
            } else {
                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return getBankAccountFormMutableLiveData!!
    }

    /** add bank country Api**/


    var addBankAccountFormMutableLiveData: MutableLiveData<GetBankAccountForm?>? = null

    fun addBankAccountFormListLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
        identifier: String,
        request: BankAccountRequest
    ): LiveData<GetBankAccountForm?> {
        addBankAccountFormMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                apiInterfaceScout.addBankAccountForm(
                    authorization,
                    client_number,
                    device_number,
                    identifier,
                    request).enqueue(object : Callback<GetBankAccountForm?> {
                        override fun onResponse(
                            call: Call<GetBankAccountForm?>,
                            response: Response<GetBankAccountForm?>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                addBankAccountFormMutableLiveData!!.postValue(response.body())
                            } else {
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<GetBankAccountForm?>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                        }
                    })

            } else {

                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return addBankAccountFormMutableLiveData!!
    }



    /** make primary bank Api**/


    var makePrimaryAccountMutableLiveData: MutableLiveData<MakePrimaryAccount?>? = null

    fun makePrimaryAccountFormListLiveData(
        activity: Activity?,
        authorization: String,
        client_number: String,
        device_number: String,
        identifier: String
    ): LiveData<MakePrimaryAccount?> {
        makePrimaryAccountMutableLiveData = MutableLiveData()
        activity?.let {

            val apiErrorHandler = ApiErrorHandler(it.applicationContext)

            if (CommonUtils.isNetworkConnected(it)) {
                CommonUtils.showProgress(it)

                apiInterfaceScout.makeAccountPrimary(
                    authorization,
                    client_number,
                    device_number,
                    identifier,
                    ).enqueue(object : Callback<MakePrimaryAccount?> {
                        override fun onResponse(
                            call: Call<MakePrimaryAccount?>,

                            response: Response<MakePrimaryAccount?>

                        ) {
                            CommonUtils.dismissProgress()

                            if (response.isSuccessful && response.body() != null) {
                                makePrimaryAccountMutableLiveData!!.postValue(response.body())
                            } else {
                                val errorMessage =
                                    apiErrorHandler.handleError(HttpException(response))
                                errorDialogOpen(activity, errorMessage)
                            }
                        }

                        override fun onFailure(call: Call<MakePrimaryAccount?>, t: Throwable) {
                            val errorMessage = apiErrorHandler.handleError(t)
                            errorDialogOpen(activity, errorMessage.toString())
                            CommonUtils.dismissProgress()

                        }
                    })

            } else {

                apiErrorHandler.handleError(IOException("No internet connection"))
            }
        }

        return makePrimaryAccountMutableLiveData!!
    }
}