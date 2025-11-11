package com.student.Compass_Abroad.retrofit



import android.util.Log
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

object RetrofitClient12 {
    private var retrofit: Retrofit? = null
    private val BASE_URL: String
        get() = App.context.getString(R.string.base_url)

    @JvmStatic
    val retrofitCallerObject11: Retrofit?
        get() {
            if (retrofit == null) {
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

                val apiInterface = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiInterface::class.java)


                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(headerLoggingInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }
}