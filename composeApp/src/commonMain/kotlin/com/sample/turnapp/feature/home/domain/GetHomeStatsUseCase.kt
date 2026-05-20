package com.sample.turnapp.feature.home.domain

import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.feature.appointment.domain.AppointmentsFilter
import com.sample.turnapp.feature.appointment.domain.GetAppointmentsParam
import com.sample.turnapp.feature.appointment.domain.usecase.GetAppointmentsUseCase
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.usecase.GetPeopleListUseCase

class GetHomeStatsUseCase(
    private val getPeopleList: GetPeopleListUseCase,
    private val getAppointments: GetAppointmentsUseCase
) {

    suspend operator fun invoke(): Resource<HomeStats, GeneralError> {

        val peopleResult = getPeopleList(GetPeopleParam())
        val appointmentsResult = getAppointments(GetAppointmentsParam(filter = AppointmentsFilter.ALL))

        if (peopleResult is Resource.Failure) {
            return Resource.Failure(peopleResult.error)
        }

        if (appointmentsResult is Resource.Failure) {
            return Resource.Failure(appointmentsResult.error)
        }

        val people = (peopleResult as Resource.Success).data.item
        val appointments = (appointmentsResult as Resource.Success).data.item

        val today = todayStartEndRange()

        return Resource.Success(
            HomeStats(
                peopleCount = people.size,
                activePeople = people.count { !it.deleted },
                deletedPeople = people.count { it.deleted },

                activeAppointments = appointments.size,
                todayAppointments = appointments.count {
                    it.startTime in today
                }
            )
        )
    }

    private fun todayStartEndRange(): String {
        // keep simple placeholder (replace with real logic if needed)
        return ""
    }
}