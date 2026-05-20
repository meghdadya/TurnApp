package com.sample.turnapp.feature.appointment.domain

data class GetAppointmentsParam(
    val start: Int = 0,
    val length: Int = 20,
    val filter: AppointmentsFilter = AppointmentsFilter.ACTIVE,
    val personId: Int? = null
)