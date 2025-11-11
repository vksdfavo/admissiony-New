package com.student.Compass_Abroad.modal.errorHandle

class ApiError {
    var statusCode = 0
    var errors: List<ApiErrorMessage?>? = null

    class ApiErrorMessage {
        var message: String? = null
    }
}