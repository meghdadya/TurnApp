package com.sample.turnapp.feature.people.domain

import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.GetAppointmentsParam
import com.sample.turnapp.feature.appointment.domain.SaveAppointmentParam

interface AppointmentsRepository {

    suspend fun getAppointmentsList(
        param: GetAppointmentsParam
    ): Resource<List<AppointmentUiModel>, GeneralError>

    suspend fun saveAppointment(
        param: SaveAppointmentParam
    ): Resource<Int, GeneralError>

    suspend fun deleteAppointments(
        id: Int,
        deleteReason: String
    ): Resource<Int, GeneralError>

    suspend fun restoreAppointments(
        id: Int
    ): Resource<Int, GeneralError>
}