package com.student.Compass_Abroad.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient2 {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://inventory-api.firmli.com/v1/"

    @JvmStatic
    val retrofitCallerObject2: Retrofit?
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val headerLoggingInterceptor = Interceptor { chain ->

                val originalRequest = chain.request()
                val userAgent = System.getProperty("http.agent") ?: "CompassAbroadApp/1.0"

                val newRequest = originalRequest.newBuilder()
                    .header("User-Agent", userAgent) // 👈 Explicitly set User-Agent
                    .build()

                Log.d("RetrofitHeaders", "➡️ $newRequest")

                chain.proceed(newRequest)
            }

            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().connectTimeout(100, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES).retryOnConnectionFailure(true)
                    .addInterceptor(headerLoggingInterceptor)
                    .addInterceptor(loggingInterceptor).build()
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
                    .build()
            }
            return retrofit
        }

}