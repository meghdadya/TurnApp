package com.sample.turnapp.feature.appointment.domain

import com.sample.turnapp.core.domain.model.DomainModel

data class AppointmentUiModel(
    val id: Int?,
    val personId: Int,
    val personName: String,
    val phoneNumber: String,
    val startTime: String,
    val endTime: String,
    val title: String,
    val description: String,
    val deleted: Boolean,
    val deleteReason: String? = null,
    val deleteTime: String? = null
): DomainModel