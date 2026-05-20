package com.sample.turnapp.feature.people.domain.model

sealed interface PeopleFilter {
        object ALL : PeopleFilter
        object ACTIVE : PeopleFilter
        object DELETED : PeopleFilter
    }