package com.sample.turnapp.feature.appointment.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.people.domain.AppointmentsRepository

class RestoreAppointmentsUseCase(
    private val repository: AppointmentsRepository
) : UseCase<Int, DomainModel>() {

    override suspend fun invoke(
        param: Int
    ): Resource<DomainModel, GeneralError> {

        return repository.restoreAppointments(param).map { object : DomainModel{} }
    }
}