package com.sample.turnapp.feature.people.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.people.domain.PeopleRepository
import com.sample.turnapp.feature.people.domain.model.DeletePeopleParam

class DeletePeopleUseCase(
    private val repository: PeopleRepository
) : UseCase<DeletePeopleParam, DomainModel>() {

    override suspend fun invoke(
        param: DeletePeopleParam
    ): Resource<DomainModel, GeneralError> {

        return repository.deletePeople(
            id = param.id,
            deleteReason = param.deleteReason
        ).map { object : DomainModel {} }
    }
}