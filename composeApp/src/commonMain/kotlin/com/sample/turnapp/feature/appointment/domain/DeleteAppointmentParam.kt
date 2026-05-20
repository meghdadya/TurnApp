package com.sample.turnapp.feature.appointment.domain

data class DeleteAppointmentParam(
        val id: Int,
        val deleteReason: String
    )