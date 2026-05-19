package com.sample.turnapp.core.domain.model

sealed interface Resource<out S, out E> {
    data class Success<out S>(val data: S) : Resource<S, Nothing>
    data class Failure<out E>(val error: E) : Resource<Nothing, E>

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    fun getOrNull(): S? = (this as? Success<S>)?.data

    fun errorOrNull(): E? = (this as? Failure<E>)?.error

}

/**
 * Maps the original success result to another result using [mapTo]. If the original result is a failure,
 * the resulting resource will be the same as the original.
 */
inline fun <S, E, M> Resource<S, E>.map(mapTo: (S) -> M): Resource<M, E> =
    when (this) {
        is Resource.Failure -> this
        is Resource.Success -> Resource.Success(mapTo(this.data))
    }

/**
 * Performs the given [action] if the resource is a success and returns the resource itself afterwards.
 */
inline fun <S, E> Resource<S, E>.onSuccess(action: (S) -> Unit): Resource<S, E> =
    when (this) {
        is Resource.Failure -> this
        is Resource.Success -> {
            action(this.data)
            this
        }
    }