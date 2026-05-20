package com.sample.turnapp.feature.people.domain.model

data class DeletePeopleParam(
        val id: Int,
        val deleteReason: String
    )