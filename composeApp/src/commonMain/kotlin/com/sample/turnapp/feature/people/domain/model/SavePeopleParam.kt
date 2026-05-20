package com.sample.turnapp.feature.people.domain.model

data class SavePeopleParam(
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val socialNumber: String,
    val phoneNumber: String
)