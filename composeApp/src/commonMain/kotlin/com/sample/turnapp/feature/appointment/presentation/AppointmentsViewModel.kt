package com.sample.turnapp.feature.appointment.presentation

import androidx.lifecycle.viewModelScope
import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.ui.base.BaseViewModel
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.AppointmentsFilter
import com.sample.turnapp.feature.appointment.domain.DeleteAppointmentParam
import com.sample.turnapp.feature.appointment.domain.GetAppointmentsParam
import com.sample.turnapp.feature.appointment.domain.SaveAppointmentParam
import com.sample.turnapp.feature.appointment.domain.usecase.DeleteAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.GetAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.RestoreAppointmentsUseCase
import com.sample.turnapp.feature.appointment.domain.usecase.SaveAppointmentUseCase
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.usecase.GetPeopleListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AppointmentsViewModel(
    private val getPeople: GetPeopleListUseCase,
    private val getAppointments: GetAppointmentsUseCase,
    private val deleteAppointments: DeleteAppointmentsUseCase,
    private val restoreAppointments: RestoreAppointmentsUseCase,
    private val saveAppointments: SaveAppointmentUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<
        AppointmentsContract.AppointmentsUiEvent,
        AppointmentsContract.AppointmentsUiState,
        AppointmentsContract.AppointmentsUiEffect
        >() {

    // -------------------- INIT --------------------

    val initialData = emptyFlow<Unit>()
        .onStart { fetchAppointments() }
        .viewModelStateIn(
            started = SharingStarted.WhileSubscribed(0),
            initialValue = Unit
        )

    // -------------------- FETCH --------------------

    private suspend fun fetchAppointments() {

        setState {
            copy(
                isInitialLoading = true,
                isLoading = true,
                hasError = false
            )
        }

        coroutineScope {

            val appointmentsDeferred = async {
                getAppointments(GetAppointmentsParam())
            }

            val peopleDeferred = async {
                getPeople(GetPeopleParam())
            }

            val appointmentsResult = appointmentsDeferred.await()
            val peopleResult = peopleDeferred.await()

            val people = when (peopleResult) {
                is Resource.Success -> peopleResult.data.item
                is Resource.Failure -> emptyList()
            }

            when (appointmentsResult) {

                is Resource.Failure -> {
                    setState {
                        copy(
                            isInitialLoading = false,
                            isLoading = false,
                            hasError = true,
                            people = people
                        )
                    }
                }

                is Resource.Success -> {

                    val appointments = appointmentsResult.data.item

                    setState {
                        copy(
                            isInitialLoading = false,
                            isLoading = false,
                            hasError = false,
                            appointments = appointments,
                            people = people,
                            filteredAppointments = applyFilters(
                                appointments,
                                searchQuery,
                                selectedFilter
                            )
                        )
                    }
                }
            }
        }
    }

    // -------------------- EVENTS --------------------

    override fun handleEvent(event: AppointmentsContract.AppointmentsUiEvent) {
        when (event) {

            is AppointmentsContract.AppointmentsUiEvent.OnSearchQueryChange ->
                updateSearch(event.query)

            is AppointmentsContract.AppointmentsUiEvent.OnFilterSelected ->
                updateFilter(event.filter)

            is AppointmentsContract.AppointmentsUiEvent.OnSaveAppointment ->
                saveAppointment(
                    SaveAppointmentParam(
                        id = event.id,
                        personId = event.personId,
                        startTime = event.startTime,
                        endTime = event.endTime,
                        title = event.title,
                        description = event.description
                    )
                )

            is AppointmentsContract.AppointmentsUiEvent.OnAppointmentClick ->
                setEffect {
                    AppointmentsContract.AppointmentsUiEffect.NavigateToAppointmentDetails(event.id)
                }

            is AppointmentsContract.AppointmentsUiEvent.OnDeleteClick ->
                deleteAppointment(event.id, event.deleteReason)

            is AppointmentsContract.AppointmentsUiEvent.OnRestoreClick ->
                restoreAppointment(event.id)

            AppointmentsContract.AppointmentsUiEvent.Retry ->
                retry()
        }
    }

    // -------------------- SAVE --------------------

    private fun saveAppointment(param: SaveAppointmentParam) {
        viewModelScope.launch(dispatcherProvider.io) {

            when (saveAppointments(param)) {

                is Resource.Failure -> {
                    setEffect { AppointmentsContract.AppointmentsUiEffect.ShowError }
                }

                is Resource.Success -> {
                    fetchAppointments()
                }
            }
        }
    }

    // -------------------- DELETE --------------------

    private fun deleteAppointment(id: Int, deleteReason: String) {
        viewModelScope.launch(dispatcherProvider.io) {

            setState { copy(deletingAppointmentId = id) }

            when (deleteAppointments(DeleteAppointmentParam(id, deleteReason))) {

                is Resource.Failure -> {
                    setState { copy(deletingAppointmentId = null) }
                    setEffect { AppointmentsContract.AppointmentsUiEffect.ShowError }
                }

                is Resource.Success -> {

                    val updated = currentState.appointments.map {
                        if (it.id == id) it.copy(deleted = true) else it
                    }

                    setState {
                        copy(
                            deletingAppointmentId = null,
                            appointments = updated,
                            filteredAppointments = applyFilters(
                                updated,
                                searchQuery,
                                selectedFilter
                            )
                        )
                    }

                    setEffect {
                        AppointmentsContract.AppointmentsUiEffect.ShowUndoDeleteSnackbar(id)
                    }
                }
            }
        }
    }

    // -------------------- RESTORE --------------------

    private fun restoreAppointment(id: Int) {
        viewModelScope.launch(dispatcherProvider.io) {

            when (restoreAppointments(id)) {

                is Resource.Failure -> {
                    setEffect { AppointmentsContract.AppointmentsUiEffect.ShowError }
                }

                is Resource.Success -> {

                    val updated = currentState.appointments.map {
                        if (it.id == id) it.copy(deleted = false) else it
                    }

                    setState {
                        copy(
                            appointments = updated,
                            filteredAppointments = applyFilters(
                                updated,
                                searchQuery,
                                selectedFilter
                            )
                        )
                    }
                }
            }
        }
    }

    // -------------------- SEARCH --------------------

    private fun updateSearch(query: String) {
        setState {
            copy(
                searchQuery = query,
                filteredAppointments = applyFilters(appointments, query, selectedFilter)
            )
        }
    }

    // -------------------- FILTER --------------------

    private fun updateFilter(filter: AppointmentsFilter) {
        setState {
            copy(
                selectedFilter = filter,
                filteredAppointments = applyFilters(appointments, searchQuery, filter)
            )
        }
    }

    // -------------------- RETRY --------------------

    private fun retry() {
        viewModelScope.launch {
            fetchAppointments()
        }
    }

    // -------------------- FILTER LOGIC --------------------

    private fun applyFilters(
        list: List<AppointmentUiModel>,
        query: String,
        filter: AppointmentsFilter
    ): List<AppointmentUiModel> {

        return list.filter { item ->

            val matchesFilter = when (filter) {
                AppointmentsFilter.ALL -> true
                AppointmentsFilter.ACTIVE -> !item.deleted
                AppointmentsFilter.DELETED -> item.deleted
            }

            val matchesSearch =
                query.isBlank() ||
                        item.title.contains(query, ignoreCase = true) ||
                        item.personName.contains(query, ignoreCase = true) ||
                        item.phoneNumber.contains(query, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    // -------------------- INITIAL STATE --------------------

    override fun createInitialState(): AppointmentsContract.AppointmentsUiState =
        AppointmentsContract.AppointmentsUiState()
}