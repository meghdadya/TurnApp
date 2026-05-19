package com.sample.turnapp.core.data.base


import com.sample.turnapp.core.data.network.NetworkResponse
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.error.GeneralError.*


abstract class BaseRemoteDataSource {

    suspend fun <T : BaseServerResponse<R>, R> safeApiCall(
        apiCall: suspend () -> NetworkResponse<T, ErrorResponse>
    ): Resource<R, GeneralError> {

        return when (val result = apiCall()) {

            is NetworkResponse.Success -> {
                val body = result.body
                val data = body?.data
                if (data != null) {
                    Resource.Success(data)
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Resource.Success(Unit as R)
                }
            }

            is NetworkResponse.NetworkError -> {
                Resource.Failure(NetworkError)
            }

            is NetworkResponse.UnknownError -> {
                Resource.Failure(
                    UnknownError(result.error)
                )
            }

            is NetworkResponse.BadRequest -> {
                Resource.Failure(
                    BadRequest(result.errorBody?.message)
                )
            }

            is NetworkResponse.Unauthorized -> {
                Resource.Failure(
                    Unauthorized(result.errorBody?.message)
                )
            }

            is NetworkResponse.Forbidden -> {
                Resource.Failure(
                    Forbidden(result.errorBody?.message)
                )
            }

            is NetworkResponse.NotFound -> {
                Resource.Failure(
                    NotFound(result.errorBody?.message)
                )
            }

            is NetworkResponse.UnprocessableEntity -> {
                Resource.Failure(
                    Unprocessable(result.errorBody?.message)
                )
            }

            is NetworkResponse.InternalServerError -> {
                Resource.Failure(
                    ServerError(result.errorBody?.message)
                )
            }

            is NetworkResponse.UnknownHttpError -> {
                Resource.Failure(
                    HttpError(
                        code = result.code,
                        message = result.errorBody?.message
                    )
                )
            }
        }
    }

}

