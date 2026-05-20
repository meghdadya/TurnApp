package com.sample.turnapp.data.source

import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.data.model.people.DeletePatientRequest
import com.sample.turnapp.data.model.people.PatientFilterRequest
import com.sample.turnapp.data.model.people.PatientList
import com.sample.turnapp.data.model.people.SavePatientRequest

interface PeopleDataSource {

    suspend fun getPeopleList(
        request: PatientFilterRequest
    ): Resource<PatientList, GeneralError>

    suspend fun savePeople(
        request: SavePatientRequest
    ): Resource<Int, GeneralError>

    suspend fun deletePeople(
        request: DeletePatientRequest
    ): Resource<Int, GeneralError>

    suspend fun restorePeople(
        id: Int
    ): Resource<Int, GeneralError>
}