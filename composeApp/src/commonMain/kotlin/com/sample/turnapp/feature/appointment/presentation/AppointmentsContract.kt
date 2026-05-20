package com.sample.turnapp.feature.appointment.presentation

import com.sample.turnapp.core.ui.base.BaseContract
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.AppointmentsFilter
import com.sample.turnapp.feature.people.domain.model.PersonUiModel

interface AppointmentsContract {

    // -------------------- UI EVENTS --------------------
    sealed interface AppointmentsUiEvent : BaseContract.UiEvent {

        data class OnSearchQueryChange(val query: String) : AppointmentsUiEvent

        data class OnFilterSelected(val filter: AppointmentsFilter) : AppointmentsUiEvent

        data class OnSaveAppointment(
            val id: Int? = null,
            val personId: Int,
            val startTime: Long,
            val endTime: Long,
            val title: String,
            val description: String? = null
        ) : AppointmentsUiEvent

        data class OnAppointmentClick(val id: Int) : AppointmentsUiEvent

        data class OnDeleteClick(val id: Int, val deleteReason: String) : AppointmentsUiEvent

        data class OnRestoreClick(val id: Int) : AppointmentsUiEvent

        object Retry : AppointmentsUiEvent
    }

    // -------------------- UI STATE --------------------
    data class AppointmentsUiState(
        val isLoading: Boolean = false,
        val isInitialLoading: Boolean = true,
        val hasError: Boolean = false,

        val searchQuery: String = "",
        val selectedFilter: AppointmentsFilter = AppointmentsFilter.ALL,

        val appointments: List<AppointmentUiModel> = emptyList(),
        val filteredAppointments: List<AppointmentUiModel> = emptyList(),

        val deletingAppointmentId: Int? = null,
        val people: List<PersonUiModel> = emptyList(),
    ) : BaseContract.UiState

    // -------------------- UI EFFECTS --------------------
    sealed interface AppointmentsUiEffect : BaseContract.UiEffect {

        object NavigateToAddAppointment : AppointmentsUiEffect

        data class NavigateToAppointmentDetails(val id: Int) : AppointmentsUiEffect

        data class ShowUndoDeleteSnackbar(val id: Int) : AppointmentsUiEffect

        object ShowError : AppointmentsUiEffect
    }
}