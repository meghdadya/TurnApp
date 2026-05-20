package com.sample.turnapp.feature.home.domain

data class HomeStats(
    val todayAppointments: Int,
    val peopleCount: Int,
    val activePeople: Int,
    val deletedPeople: Int,
    val activeAppointments: Int
)