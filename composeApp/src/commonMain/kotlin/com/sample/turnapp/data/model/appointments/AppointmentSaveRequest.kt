package com.sample.turnapp.data.model.appointments

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentSaveRequest(
    val id: Int? = 0,

    val personId: Int,

    val startTime: Long,

    val endTime: Long,

    val title: String,

    val description: String? = null
)
