package com.sample.turnapp.feature.appointment.domain

data class SaveAppointmentParam(
    val id: Int? = null,
    val personId: Int,
    val startTime: Double,
    val endTime: Double,
    val title: String,
    val description: String? = null
)