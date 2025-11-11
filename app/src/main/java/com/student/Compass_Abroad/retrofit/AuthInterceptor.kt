import android.content.Context
import android.content.Intent
import android.util.Log
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.activities.LoginActivity
import com.student.Compass_Abroad.retrofit.ApiInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val context: Context,
    private val apiInterface: ApiInterface
) : Interceptor {

    @Volatile
    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = App.sharedPre?.getString(AppConstants.ACCESS_TOKEN, null)

        if (!accessToken.isNullOrEmpty()) {

            request = addAuthorizationHeader(request, accessToken)
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            synchronized(this) {
                if (!isRefreshing) {
                    isRefreshing = true
                    try {
                        val newToken = refreshToken()
                        isRefreshing = false

                        if (newToken != null) {
                            val newRequest = addAuthorizationHeader(request, newToken)
                            return chain.proceed(newRequest)
                        } else {
                            logoutUser()
                        }
                    } catch (e: Exception) {
                        Log.e("AuthInterceptor", "Token refresh failed: ${e.message}", e)
                        logoutUser()
                    } finally {
                        isRefreshing = false
                    }
                } else {
                    Log.e("AuthInterceptor", "Token refresh already in progress")
                }
            }
        }

        return response
    }

    private fun addAuthorizationHeader(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }

    private fun refreshToken(): String? {
        return try {
            val identity = App.sharedPre?.getString(AppConstants.USER_ROLE, "")?.trim()?.replace("\"", "")

            if (identity.isNullOrEmpty()) {
                Log.e("AuthInterceptor", "Identity is empty, cannot refresh token")
                null
            } else {
                val refreshCall = apiInterface.getRefreshToken(
                    fiClientNumber = AppConstants.fiClientNumber,
                    device_number = App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, ""),
                    authorization = "Bearer ${App.sharedPre?.getString(AppConstants.REFRESH_TOKEN, "")}",
                    identity = identity
                )

                val refreshResponse = refreshCall?.execute()

                if (refreshResponse?.isSuccessful == true) {
                    val newToken = refreshResponse.body()?.data?.tokensInfo?.accessToken
                    newToken?.let {
                        App.sharedPre?.saveString(AppConstants.ACCESS_TOKEN, it)
                        Log.e("AuthInterceptor", "Token refreshed successfully: $it")
                        return newToken
                    }
                } else {
                    Log.e("AuthInterceptor", "Failed to refresh token: ${refreshResponse?.code()}")
                    null
                }
            }
        } catch (e: IOException) {
            Log.e("AuthInterceptor", "Error during token refresh: ${e.message}", e)
            null
        }
    }

    private fun logoutUser() {
        Log.e("AuthInterceptor", "Logging out user due to expired tokens.")
        App.sharedPre?.clearPreferences()
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        context.startActivity(intent)
    }
}
