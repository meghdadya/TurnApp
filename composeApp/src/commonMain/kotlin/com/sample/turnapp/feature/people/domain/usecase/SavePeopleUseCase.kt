package com.sample.turnapp.feature.people.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.people.domain.PeopleRepository
import com.sample.turnapp.feature.people.domain.model.SavePeopleParam

class SavePeopleUseCase(
    private val repository: PeopleRepository
) : UseCase<SavePeopleParam, DomainModel>() {

    override suspend fun invoke(
        param: SavePeopleParam
    ): Resource<DomainModel, GeneralError> {

        return repository.savePeople(param).map { object : DomainModel {} }
    }
}