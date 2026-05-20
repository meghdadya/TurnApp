package com.sample.turnapp.feature.appointment.domain

data class SaveAppointmentParam(
    val id: Int? = null,
    val personId: Int,
    val startTime: Long,
    val endTime: Long,
    val title: String,
    val description: String? = null
)