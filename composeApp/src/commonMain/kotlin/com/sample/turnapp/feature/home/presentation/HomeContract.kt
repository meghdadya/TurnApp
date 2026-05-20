package com.sample.turnapp.feature.home.presentation

import com.sample.turnapp.core.ui.base.BaseContract

interface HomeContract {

    // -------------------- UI EVENTS --------------------
    sealed interface HomeUiEvent : BaseContract.UiEvent {

        object OnPeopleClick : HomeUiEvent
        object OnAppointmentsClick : HomeUiEvent
        object OnRetry : HomeUiEvent
    }

    // -------------------- UI STATE --------------------
    data class HomeUiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,

        val todayAppointments: Int = 0,
        val appointments: Int = 0,
        val peopleCount: Int = 0,
        val deletedCount: Int = 0,
        val activePeopleCount: Int = 0
    ) : BaseContract.UiState

    // -------------------- UI EFFECTS --------------------
    sealed interface HomeUiEffect : BaseContract.UiEffect {

        object NavigateToPeople : HomeUiEffect
        object NavigateToAppointments : HomeUiEffect

        object ShowError : HomeUiEffect
    }
}