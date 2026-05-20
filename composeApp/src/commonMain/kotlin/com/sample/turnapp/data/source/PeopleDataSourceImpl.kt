package com.sample.turnapp.data.source

import com.sample.turnapp.core.data.base.BaseRemoteDataSource
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import com.sample.turnapp.data.api.AppApi
import com.sample.turnapp.data.model.people.DeletePatientRequest
import com.sample.turnapp.data.model.people.PatientFilterRequest
import com.sample.turnapp.data.model.people.PatientList
import com.sample.turnapp.data.model.people.SavePatientRequest

class PeopleDataSourceImpl(
    private val appApi: AppApi
) : PeopleDataSource, BaseRemoteDataSource() {

    override suspend fun getPeopleList(
        request: PatientFilterRequest
    ): Resource<PatientList, GeneralError> =
        safeApiCall {
            appApi.getPeopleList(request)
        }

    override suspend fun savePeople(
        request: SavePatientRequest
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.savePeople(request)
        }

    override suspend fun deletePeople(
        request: DeletePatientRequest
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.deletePeople(request)
        }

    override suspend fun restorePeople(
        id: Int
    ): Resource<Int, GeneralError> =
        safeApiCall {
            appApi.restorePeople(id)
        }
}