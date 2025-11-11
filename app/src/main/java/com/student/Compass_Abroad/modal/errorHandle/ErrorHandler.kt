package com.student.Compass_Abroad.modal.errorHandle

import com.google.gson.Gson
import retrofit2.Response

object ErrorHandler {
    @JvmStatic
    fun parseError(response: Response<*>): ApiError {
        return try {
            val gson = Gson()
            gson.fromJson(response.errorBody()!!.string(), ApiError::class.java)
        } catch (e: Exception) {
            ApiError() // Return a default instance to handle parsing errors gracefully
        }
    }

    @JvmStatic
    fun getErrorMessage(error: ApiError?): String? {
        return if (error?.errors != null && error.errors!!.isNotEmpty()) {
            error.errors!![0]?.message // Assumes the first error is the primary message
        } else {
            "Unknown error occurred" // Fallback error message
        }
    }
}
