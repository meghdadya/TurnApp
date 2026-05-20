package com.sample.turnapp.feature.people.domain.usecase

import com.sample.turnapp.core.domain.base.UseCase
import com.sample.turnapp.core.domain.model.ListDomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.feature.people.domain.PeopleRepository
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.model.PersonUiModel

class GetPeopleListUseCase(
    private val repository: PeopleRepository
) : UseCase<GetPeopleParam, ListDomainModel<PersonUiModel>>() {

    override suspend fun invoke(
        param: GetPeopleParam
    ): Resource<ListDomainModel<PersonUiModel>, GeneralError> {

        return repository.getPeopleList(param).map { it -> ListDomainModel(it) }
    }
}