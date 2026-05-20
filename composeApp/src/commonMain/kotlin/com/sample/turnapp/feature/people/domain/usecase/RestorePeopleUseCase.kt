package com.sample.turnapp.feature.people.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.people.domain.PeopleRepository

class RestorePeopleUseCase(
    private val repository: PeopleRepository
) : UseCase<Int, DomainModel>() {

    override suspend fun invoke(param: Int): Resource<DomainModel, GeneralError> {
        return repository.restorePeople(param).map { it -> object : DomainModel {} }
    }
}