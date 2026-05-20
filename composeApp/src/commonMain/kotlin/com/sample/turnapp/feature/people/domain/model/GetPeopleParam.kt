package com.sample.turnapp.feature.people.domain.model

data class GetPeopleParam(
    val start: Int = 0,
    val length: Int = 100,
    val filter: PeopleFilter = PeopleFilter.ALL,
    val query: String = ""
)