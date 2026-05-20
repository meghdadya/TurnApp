package com.sample.turnapp.feature.appointment.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.appointment.domain.DeleteAppointmentParam
import com.sample.turnapp.feature.people.domain.AppointmentsRepository

class DeleteAppointmentsUseCase(
    private val repository: AppointmentsRepository
) : UseCase<DeleteAppointmentParam, DomainModel>() {

    override suspend fun invoke(
        param: DeleteAppointmentParam
    ): Resource<DomainModel, GeneralError> {

        return repository.deleteAppointments(
            id = param.id,
            deleteReason = param.deleteReason
        ).map { object : DomainModel{} }
    }
}