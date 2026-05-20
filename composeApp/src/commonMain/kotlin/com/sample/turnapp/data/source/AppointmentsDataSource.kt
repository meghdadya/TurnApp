package com.sample.turnapp.data.source

import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.data.model.appointments.AppointmentList
import com.sample.turnapp.data.model.appointments.AppointmentSaveRequest
import com.sample.turnapp.data.model.appointments.AppointmentsFilterRequest
import com.sample.turnapp.data.model.appointments.DeleteAppointmentRequest

interface AppointmentsDataSource {

    suspend fun getAppointmentsList(
        request: AppointmentsFilterRequest
    ): Resource<AppointmentList, GeneralError>

    suspend fun saveAppointment(
        request: AppointmentSaveRequest
    ): Resource<Int, GeneralError>

    suspend fun deleteAppointments(
        request: DeleteAppointmentRequest
    ): Resource<Int, GeneralError>

    suspend fun restoreAppointments(
        id: Int
    ): Resource<Int, GeneralError>
}