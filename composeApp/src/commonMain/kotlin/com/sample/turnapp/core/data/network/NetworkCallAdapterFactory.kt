package com.sample.turnapp.core.data.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException

class NetworkCallAdapterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {

        if (typeData.typeInfo.type != NetworkResponse::class) return null

        return object : Converter.SuspendResponseConverter<HttpResponse, Any> {

            override suspend fun convert(result: KtorfitResult): Any {

                val successBodyType = typeData.typeArgs.first().typeInfo
                val errorBodyType = typeData.typeArgs[1].typeInfo

                return when (result) {

                    is KtorfitResult.Failure -> {
                        val throwable = result.throwable

//                        println("NetworkCallAdapterFactory Network Failure:")
//                        println("NetworkCallAdapterFactory Type: ${throwable::class.qualifiedName}")
//                        println("NetworkCallAdapterFactory Message: ${throwable.message}")
//                        println("NetworkCallAdapterFactory Cause: ${throwable.cause}")

                        if (throwable is ResponseException) {
                            val requestUrl = throwable.response.request.url
//                            println("NetworkCallAdapterFactory Request URL: $requestUrl")
//                            println("NetworkCallAdapterFactory Status: ${throwable.response.status}")
                        }

                        throwable.printStackTrace()

                        when (throwable) {
                            is SocketTimeoutException,
                            is HttpRequestTimeoutException,
                            is IOException -> {
                                NetworkResponse.NetworkError
                            }
                            else -> {
                                NetworkResponse.UnknownError(throwable)
                            }
                        }
                    }


                    is KtorfitResult.Success -> {

                        val response = result.response
                        val statusCode = response.status.value

                        if (statusCode in 200..299) {
                            NetworkResponse.Success(
                                response.body(typeInfo = successBodyType)
                            )
                        } else {

                            val errorBody = try {
                                response.body(typeInfo = errorBodyType)
                            } catch (e: Exception) {
                                null
                            }

                            when (statusCode) {
                                400 -> NetworkResponse.BadRequest(errorBody)
                                401 -> NetworkResponse.Unauthorized(errorBody)
                                403 -> NetworkResponse.Forbidden(errorBody)
                                404 -> NetworkResponse.NotFound(errorBody)
                                422 -> NetworkResponse.UnprocessableEntity(errorBody)
                                500 -> NetworkResponse.InternalServerError(errorBody)

                                else -> NetworkResponse.UnknownHttpError(
                                    code = statusCode,
                                    errorBody = errorBody
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
