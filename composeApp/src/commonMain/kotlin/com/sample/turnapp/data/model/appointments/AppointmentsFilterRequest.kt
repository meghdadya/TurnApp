package com.sample.turnapp.data.model.appointments

import com.sample.turnapp.data.model.RemovedState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentsFilterRequest(

    val start: Int = 0,

    @SerialName("lenght")
    val length: Int = 100,

    val removedState: RemovedState = RemovedState.ACTIVE,

    val from: Double? = null,

    val to: Double? = null,

    val title: String? = null,

    val personId: Int? = null
)