package com.sample.turnapp.data.model.people
import kotlinx.serialization.Serializable

@Serializable
data class PatientList(
    val data: List<Patient>,
    val totalCount: Int,
    val filteredCount: Int
)




@Serializable
data class Patient(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val isDeleted: Boolean,
    val deleteTime: String? = null,
    val phoneNumber: String,
    val socialNumber: String,
    val deleteReason: String? = null,
    val appointmentsCount: Int
)