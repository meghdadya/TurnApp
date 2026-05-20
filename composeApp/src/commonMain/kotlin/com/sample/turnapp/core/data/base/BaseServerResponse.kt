package com.sample.turnapp.core.data.base

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BaseServerResponse<T>(
    @SerialName("data")
    val data: T? = null,

    @JsonNames( "message")
    val message: String? = null,

    @SerialName("success")
    val success: Boolean? = null,
)

