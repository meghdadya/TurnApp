package com.sample.turnapp.feature.people.domain.model

import com.sample.turnapp.core.domain.model.DomainModel

data class PersonUiModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nationalCode: String,
    val phoneNumber: String,
    val deleted: Boolean = false
): DomainModel