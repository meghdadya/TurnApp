package com.sample.turnapp.feature.appointment.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.appointment.domain.SaveAppointmentParam
import com.sample.turnapp.feature.appointment.domain.AppointmentsRepository

class SaveAppointmentUseCase(
    private val repository: AppointmentsRepository
) : UseCase<SaveAppointmentParam, DomainModel>() {
    override suspend fun invoke(
        param: SaveAppointmentParam
    ): Resource<DomainModel, GeneralError> {
        return repository.saveAppointment(param).map { object : DomainModel{} }
    }
}