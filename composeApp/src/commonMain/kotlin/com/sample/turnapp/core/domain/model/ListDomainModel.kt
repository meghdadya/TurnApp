package com.sample.turnapp.core.domain.model

data class ListDomainModel<T : DomainModel>(
    val item: List<T>
) : DomainModel
