package com.sample.turnapp.data.repository

import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.data.model.RemovedState
import com.sample.turnapp.data.model.appointments.Appointment
import com.sample.turnapp.data.model.appointments.AppointmentSaveRequest
import com.sample.turnapp.data.model.appointments.AppointmentsFilterRequest
import com.sample.turnapp.data.model.appointments.DeleteAppointmentRequest
import com.sample.turnapp.data.source.AppointmentsDataSource
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.AppointmentsFilter
import com.sample.turnapp.feature.appointment.domain.GetAppointmentsParam
import com.sample.turnapp.feature.appointment.domain.SaveAppointmentParam
import com.sample.turnapp.feature.people.domain.AppointmentsRepository
import kotlinx.coroutines.withContext

class AppointmentsRepositoryImpl(
    private val appointmentsDataSource: AppointmentsDataSource,
    private val dispatcherProvider: DispatcherProvider
) : AppointmentsRepository {

    // -------------------- GET LIST --------------------

    override suspend fun getAppointmentsList(
        param: GetAppointmentsParam
    ): Resource<List<AppointmentUiModel>, GeneralError> {

        return withContext(dispatcherProvider.io) {

            val request = param.toRequest()

            appointmentsDataSource.getAppointmentsList(request)
                .map { response ->
                    response.data.map { it.mapToDomain() }
                }
        }
    }

    // -------------------- SAVE --------------------

    override suspend fun saveAppointment(
        param: SaveAppointmentParam
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            appointmentsDataSource.saveAppointment(
                param.toRequest()
            )
        }
    }

    // -------------------- DELETE --------------------

    override suspend fun deleteAppointments(
        id: Int,
        deleteReason: String
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            appointmentsDataSource.deleteAppointments(
                DeleteAppointmentRequest(
                    id = id,
                    deleteReason = deleteReason
                )
            )
        }
    }

    // -------------------- RESTORE --------------------

    override suspend fun restoreAppointments(
        id: Int
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            appointmentsDataSource.restoreAppointments(id)
        }
    }

    // -------------------- MAPPING --------------------

    private fun GetAppointmentsParam.toRequest(): AppointmentsFilterRequest {
        return AppointmentsFilterRequest(
            start = start,
            length = length,
            removedState = when (filter) {
                AppointmentsFilter.ACTIVE -> RemovedState.ACTIVE
                AppointmentsFilter.DELETED -> RemovedState.REMOVED
                AppointmentsFilter.ALL -> RemovedState.ALL
                else -> RemovedState.ALL
            },
            personId = personId
        )
    }

    private fun Appointment.mapToDomain(): AppointmentUiModel {
        return AppointmentUiModel(
            id = id,
            personId = personId,
            personName = personName,
            phoneNumber = phoneNumber,
            startTime = startTime,
            endTime = endTime,
            title = title,
            description = description.orEmpty(),
            deleted = isDeleted,
            deleteReason = deleteReason,
            deleteTime = deleteTime
        )
    }

    private fun SaveAppointmentParam.toRequest(): AppointmentSaveRequest {
        return AppointmentSaveRequest(
            id = id,
            personId = personId,
            startTime = startTime,
            endTime = endTime,
            title = title,
            description = description
        )
    }
}