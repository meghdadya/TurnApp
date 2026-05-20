package com.sample.turnapp.data.model.people

import kotlinx.serialization.Serializable

@Serializable
data class SavePatientRequest(
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val socialNumber: String,
    val phoneNumber: String
)