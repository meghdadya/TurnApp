package com.sample.turnapp.data.api

import com.sample.turnapp.core.data.base.BaseServerResponse
import com.sample.turnapp.core.data.base.ErrorResponse
import com.sample.turnapp.core.data.network.NetworkResponse
import com.sample.turnapp.data.model.appointments.AppointmentList
import com.sample.turnapp.data.model.appointments.AppointmentSaveRequest
import com.sample.turnapp.data.model.appointments.AppointmentsFilterRequest
import com.sample.turnapp.data.model.appointments.DeleteAppointmentRequest
import com.sample.turnapp.data.model.people.DeletePatientRequest
import com.sample.turnapp.data.model.people.PatientFilterRequest
import com.sample.turnapp.data.model.people.PatientList
import com.sample.turnapp.data.model.people.SavePatientRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

interface AppApi {

    //People
    @POST("People/GetList")
    suspend fun getPeopleList(
       @Body request: PatientFilterRequest
    ): NetworkResponse<BaseServerResponse<PatientList>, ErrorResponse>


    @POST("People/Save")
    suspend fun savePeople(
        @Body request: SavePatientRequest
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>


    @POST("People/Delete")
    suspend fun deletePeople(
        @Body request: DeletePatientRequest
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>


    @POST("People/Restore")
    suspend fun restorePeople(
        @Query("Id") id: Int
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>

    //Appointments

    @POST("Appointments/GetList")
    suspend fun getAppointmentsList(
        @Body request: AppointmentsFilterRequest
    ): NetworkResponse<BaseServerResponse<AppointmentList>, ErrorResponse>


    @POST("Appointments/Save")
    suspend fun saveAppointment(
        @Body request: AppointmentSaveRequest
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>


    @POST("Appointments/Delete")
    suspend fun deleteAppointments(
        @Body request: DeleteAppointmentRequest
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>

    @POST("Appointments/Restore")
    suspend fun restoreAppointments(
        @Query("Id") id: Int
    ): NetworkResponse<BaseServerResponse<Int>, ErrorResponse>

}