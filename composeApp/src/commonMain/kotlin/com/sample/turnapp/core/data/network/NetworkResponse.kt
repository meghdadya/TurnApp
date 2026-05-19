package com.sample.turnapp.core.data.network

 sealed interface  NetworkResponse<out S : Any, out E : Any> {

  data class Success<S : Any>(val body: S?) : NetworkResponse<S, Nothing>

  sealed interface HttpError<E : Any> : NetworkResponse<Nothing, E> {
   val errorBody: E?
  }

  data class BadRequest<E : Any>(override val errorBody: E?) : HttpError<E>
  data class Unauthorized<E : Any>(override val errorBody: E?) : HttpError<E>
  data class Forbidden<E : Any>(override val errorBody: E?) : HttpError<E>
  data class NotFound<E : Any>(override val errorBody: E?) : HttpError<E>
  data class UnprocessableEntity<E : Any>(override val errorBody: E?) : HttpError<E>
  data class InternalServerError<E : Any>(override val errorBody: E?) : HttpError<E>

  data class UnknownHttpError<E : Any>(
   val code: Int,
   override val errorBody: E?
  ) : HttpError<E>

  data object NetworkError : NetworkResponse<Nothing, Nothing>
  data class UnknownError(val error: Throwable) : NetworkResponse<Nothing, Nothing>
}
