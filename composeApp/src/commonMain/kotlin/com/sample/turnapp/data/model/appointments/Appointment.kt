package com.sample.turnapp.data.model.appointments

import kotlinx.serialization.Serializable


@Serializable
data class AppointmentList(
    val data: List<Appointment>,
    val totalCount: Int,
    val filteredCount: Int
)

@Serializable
data class Appointment(

    val id: Int? = null,
    val title: String,
    val endTime: String,
    val personId: Int,
    val startTime: String,
    val isDeleted: Boolean,
    val deleteTime: String? = null,
    val description: String? = null,
    val deleteReason: String? = null,
    val phoneNumber: String,
    val personName: String
)