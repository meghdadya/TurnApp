package com.sample.turnapp.feature.appointment.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.ListDomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.GetAppointmentsParam
import com.sample.turnapp.feature.appointment.domain.AppointmentsRepository

class GetAppointmentsUseCase(
    private val repository: AppointmentsRepository
) : UseCase<GetAppointmentsParam, ListDomainModel<AppointmentUiModel>>() {

    override suspend fun invoke(
        param: GetAppointmentsParam
    ): Resource<ListDomainModel<AppointmentUiModel>, GeneralError> {

        return repository.getAppointmentsList(param).map { ListDomainModel(it) }
    }
}