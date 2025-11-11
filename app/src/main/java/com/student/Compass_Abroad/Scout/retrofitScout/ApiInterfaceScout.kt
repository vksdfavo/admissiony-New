package com.student.Compass_Abroad.Scout.retrofitScout

import com.student.Compass_Abroad.Scout.modalClass.BankAccountRequest
import com.student.Compass_Abroad.Scout.modalClass.getAddedBankAccount.GetAddedBankAccount
import com.student.Compass_Abroad.Scout.modalClass.getBankForm.GetBankAccountForm
import com.student.Compass_Abroad.Scout.modalClass.primaryAccount.MakePrimaryAccount
import com.student.Compass_Abroad.Scout.modalClass.scoutsummary.ScoutSummaryModal
import com.student.Compass_Abroad.modal.getDestinationCountryList.getDestinationCountry
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterfaceScout {

    @GET("referrals/summary")
    fun getScoutSummary(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<ScoutSummaryModal>


    @GET("masters/bank-information/user/{identifier}")
    fun getScoutBankDetails(
        @Header("Authorization") authorization: String?,
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Path("identifier") identifier: String
        ): Call<GetAddedBankAccount>


    @GET("dropdown/country?identifier_only=true")
    fun getAllCountryList(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
    ): Call<getDestinationCountry?>?

    @GET("masters/bank-information-fields/{identifier}")
    fun getBankAccountForm(
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String,
    ): Call<GetBankAccountForm?>?


    @POST("masters/bank-information/{identifier}")
    fun addBankAccountForm(
        @Header("Authorization") authorization: String?,
        @Header("fi-client-number") fiClientNumber: String?,
        @Header("fi-device-number") deviceNumber: String?,
        @Path("identifier") identifier: String,
        @Body request: BankAccountRequest
    ): Call<GetBankAccountForm>

    @PUT("masters/bank-information/{identifier}/mark-primary")
    fun makeAccountPrimary(
        @Header("fi-client-number") client_number: String?,
        @Header("fi-device-number") device_number: String?,
        @Header("Authorization") authorization: String?,
        @Path("identifier") identifier: String?,

        ): Call<MakePrimaryAccount?>
}