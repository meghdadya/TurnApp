package com.sample.turnapp.data.repository

import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.core.domain.model.map
import com.sample.turnapp.data.model.people.DeletePatientRequest
import com.sample.turnapp.data.model.people.Patient
import com.sample.turnapp.data.model.people.PatientFilterRequest
import com.sample.turnapp.data.model.people.RemovedState
import com.sample.turnapp.data.model.people.SavePatientRequest
import com.sample.turnapp.data.source.PeopleDataSource
import com.sample.turnapp.feature.people.domain.PeopleRepository
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.model.PeopleFilter
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import com.sample.turnapp.feature.people.domain.model.SavePeopleParam
import kotlinx.coroutines.withContext

class PeopleRepositoryImpl(
    private val peopleDataSource: PeopleDataSource,
    private val dispatcherProvider: DispatcherProvider
) : PeopleRepository {

    // -------------------- GET LIST --------------------

    override suspend fun getPeopleList(
        param: GetPeopleParam
    ): Resource<List<PersonUiModel>, GeneralError> {

        return withContext(dispatcherProvider.io) {

            val request = param.toRequest()

            peopleDataSource.getPeopleList(request)
                .map { response ->
                    response.data.map { it.mapToDomain() }
                }
        }
    }

    // -------------------- SAVE --------------------

    override suspend fun savePeople(
        param: SavePeopleParam
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            peopleDataSource.savePeople(param.toRequest())
        }
    }

    // -------------------- DELETE --------------------

    override suspend fun deletePeople(
        id: Int,
        deleteReason: String
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            peopleDataSource.deletePeople(
                DeletePatientRequest(id = id, deleteReason)
            )
        }
    }

    // -------------------- RESTORE --------------------

    override suspend fun restorePeople(
        id: Int
    ): Resource<Int, GeneralError> {

        return withContext(dispatcherProvider.io) {
            peopleDataSource.restorePeople(id)
        }
    }

    // -------------------- MAPPING --------------------

    private fun GetPeopleParam.toRequest(): PatientFilterRequest {
        return PatientFilterRequest(
            start = start,
            length = length,
            removedState = when (filter) {
                PeopleFilter.ACTIVE -> RemovedState.ACTIVE
                PeopleFilter.DELETED -> RemovedState.REMOVED
                PeopleFilter.ALL -> RemovedState.ALL
                else -> RemovedState.ALL
            },
            name = query,
            phoneNumber = "",
            socialNumber = ""
        )
    }
    private fun Patient.mapToDomain(): PersonUiModel {
        return PersonUiModel(
            id = id ,
            firstName = firstName ,
            lastName = lastName ,
            nationalCode = socialNumber ,
            deleted = isDeleted,
            phoneNumber = phoneNumber
        )
    }

    private fun SavePeopleParam.toRequest(): SavePatientRequest {
        return SavePatientRequest(
            id = id,
            firstName = firstName,
            lastName = lastName,
            socialNumber = socialNumber,
            phoneNumber = phoneNumber
        )
    }
}