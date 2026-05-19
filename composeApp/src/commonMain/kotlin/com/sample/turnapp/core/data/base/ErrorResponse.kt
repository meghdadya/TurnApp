package com.sample.turnapp.core.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    @SerialName("error")
    val error: Boolean? = null,

    @SerialName("message")
    val message: String? = null,
)
