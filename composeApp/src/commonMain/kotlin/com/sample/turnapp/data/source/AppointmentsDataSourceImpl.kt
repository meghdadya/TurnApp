package com.sample.turnapp.data.source

import com.sample.turnapp.core.data.base.BaseRemoteDataSource
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.data.api.AppApi
import com.sample.turnapp.data.model.appointments.AppointmentList
import com.sample.turnapp.data.model.appointments.AppointmentSaveRequest
import com.sample.turnapp.data.model.appointments.AppointmentsFilterRequest
import com.sample.turnapp.data.model.appointments.DeleteAppointmentRequest

class AppointmentsDataSourceImpl(
    private val appApi: AppApi
) : AppointmentsDataSource, BaseRemoteDataSource() {

    override suspend fun getAppointmentsList(
        request: AppointmentsFilterRequest
    ): Resource<AppointmentList, GeneralError> =
        safeApiCall {
            appApi.getAppointmentsList(request)
        }

    override suspend fun saveAppointment(
        request: AppointmentSaveRequest
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.saveAppointment(request)
        }

    override suspend fun deleteAppointments(
        request: DeleteAppointmentRequest
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.deleteAppointments(request)
        }

    override suspend fun restoreAppointments(
        id: Int
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.restoreAppointments(id)
        }
}