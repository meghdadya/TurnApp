package com.sample.turnapp.data.model.people

import kotlinx.serialization.Serializable

@Serializable
data class DeletePatientRequest(
    val id: Int,
    val deleteReason: String
)