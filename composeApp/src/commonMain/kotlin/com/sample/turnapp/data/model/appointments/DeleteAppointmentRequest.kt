package com.sample.turnapp.data.model.appointments

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAppointmentRequest(
    val id: Int,
    val deleteReason: String
)