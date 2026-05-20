package com.sample.turnapp.data.model.appointments

data class AppointmentSaveRequest(
    val id: Int? = 0,

    val personId: Int,

    val startTime: Double,

    val endTime: Double,

    val title: String,

    val description: String? = null
)
