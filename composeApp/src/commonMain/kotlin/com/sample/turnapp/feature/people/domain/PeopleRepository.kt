package com.sample.turnapp.feature.people.domain

import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import com.sample.turnapp.feature.people.domain.model.SavePeopleParam

interface PeopleRepository {

    suspend fun getPeopleList(
        param: GetPeopleParam
    ): Resource<List<PersonUiModel>, GeneralError>

    suspend fun savePeople(
        param: SavePeopleParam
    ): Resource<Int, GeneralError>

    suspend fun deletePeople(id: Int, deleteReason: String): Resource<Int, GeneralError>

    suspend fun restorePeople(id: Int): Resource<Int, GeneralError>
}