package com.student.Compass_Abroad.errorHandle

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class ApiErrorHandler(private val context: Context) {
    fun handleError(exception: Throwable): String {
        return when (exception) {
            is HttpException -> handleHttpException(exception)
            is IOException -> {
                if (!isNetworkAvailable()) {
                    "No internet connection. Please check your network settings."
                } else {
                    "Network error. Please try again later."
                }
            }

            else -> "An unexpected error occurred."
        }
    }

    private fun handleHttpException(exception: HttpException): String {
        val errorBody = exception.response()?.errorBody()?.string()
        val statusCode = exception.code()

        return when (statusCode) {
            400, 401, 403, 404, 422, 406,409, 500 -> extractCleanErrorMessage(errorBody)
            else -> "HTTP Error $statusCode: ${extractCleanErrorMessage(errorBody)}"
        }
    }

    private fun extractCleanErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) {
            return "An unknown error occurred"
        }

        return try {
            val jsonObject = JSONObject(errorBody)

            // Try multiple common error message patterns
            when {
                // Pattern 1: Direct message field
                jsonObject.has("message") -> {
                    val message = jsonObject.getString("message")
                    if (message.isNotEmpty()) return message
                }

                // Pattern 2: Error field with string value
                jsonObject.has("error") -> {
                    val error = jsonObject.getString("error")
                    if (error.isNotEmpty()) return error
                }

                // Pattern 3: Errors object (your API structure)
                jsonObject.has("errors") && jsonObject.get("errors") is JSONObject -> {
                    val errorsObject = jsonObject.getJSONObject("errors")
                    val errorMessages = mutableListOf<String>()
                    for (key in errorsObject.keys()) {
                        val keyString = key as? String ?: continue
                        val value = errorsObject.get(keyString)
                        when (value) {
                            is String -> {
                                // Just return the message without the field name for cleaner display
                                errorMessages.add(value)
                            }
                            is org.json.JSONArray -> {
                                val array = value as org.json.JSONArray
                                for (i in 0 until array.length()) {
                                    errorMessages.add(array.getString(i))
                                }
                            }
                        }
                    }
                    if (errorMessages.isNotEmpty()) {
                        return errorMessages.joinToString("\n")
                    }
                }

                // Pattern 4: Errors array (fallback)
                jsonObject.has("errors") && jsonObject.get("errors") is org.json.JSONArray -> {
                    val errorsArray = jsonObject.getJSONArray("errors")
                    if (errorsArray.length() > 0) {
                        val errorMessages = mutableListOf<String>()
                        for (i in 0 until errorsArray.length()) {
                            val errorItem = errorsArray.get(i)
                            when (errorItem) {
                                is String -> errorMessages.add(errorItem)
                                is JSONObject -> {
                                    for (key in errorItem.keys()) {
                                        val keyString = key as? String ?: continue
                                        val message = errorItem.optString(keyString)
                                        if (message.isNotEmpty()) {
                                            errorMessages.add(message)
                                        }
                                    }
                                }
                            }
                        }
                        if (errorMessages.isNotEmpty()) {
                            return errorMessages.joinToString("\n")
                        }
                    }
                }

                // Pattern 5: Detail field (common in REST APIs)
                jsonObject.has("detail") -> {
                    val detail = jsonObject.getString("detail")
                    if (detail.isNotEmpty()) return detail
                }
            }

            // If no recognized pattern, return a generic message
            return "An error occurred. Please try again."

        } catch (e: Exception) {
            // Log the parsing error for debugging
            android.util.Log.e("ApiErrorHandler", "Failed to parse error JSON: $errorBody", e)

            // Return a user-friendly error message instead of raw JSON
            return "An error occurred. Please try again."
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}