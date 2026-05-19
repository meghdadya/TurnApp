package com.sample.turnapp.core.domain.model.error

sealed interface GeneralError {

    // Business-level errors returned in successful HTTP responses


        // --- Business layer errors ---
        sealed interface Business : GeneralError {

            val code: Int
            val message: String

            data class Generic(
                override val code: Int,
                override val message: String
            ) : Business


            data class RegionRestricted(
                override val code: Int = 29999,
                override val message: String = "Service not available in your region"
            ) : Business
        }

    // Network level problems
    data object NetworkError : GeneralError

    // Http layer errors
    data class BadRequest(val message: String? = null) : GeneralError
    data class Unauthorized(val message: String? = null) : GeneralError
    data class Forbidden(val message: String? = null) : GeneralError
    data class NotFound(val message: String? = null) : GeneralError
    data class Unprocessable(val message: String? = null) : GeneralError
    data class ServerError(val message: String? = null) : GeneralError

    // Unknown HTTP code
    data class HttpError(
        val code: Int,
        val message: String? = null
    ) : GeneralError

    // Unexpected application crash / parsing / bugs
    data class UnknownError(val error: Throwable) : GeneralError
}
