package com.sample.turnapp.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.ui.base.BaseViewModel
import com.sample.turnapp.feature.home.domain.GetHomeStatsUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeStats: GetHomeStatsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel<
        HomeContract.HomeUiEvent,
        HomeContract.HomeUiState,
        HomeContract.HomeUiEffect
        >() {

    init {
        loadHome()
    }

    private fun loadHome() {
        viewModelScope.launch(dispatcherProvider.io) {

            setState { copy(isLoading = true, hasError = false) }

            when (val result = getHomeStats()) {

                is Resource.Failure -> {
                    setState {
                        copy(isLoading = false, hasError = true)
                    }

                    setEffect { HomeContract.HomeUiEffect.ShowError }
                }

                is Resource.Success -> {
                    val data = result.data

                    setState {
                        copy(
                            isLoading = false,
                            hasError = false,
                            todayAppointments = data.todayAppointments,
                            appointments = data.activeAppointments,
                            peopleCount = data.peopleCount,
                            deletedCount = data.deletedPeople,
                            activePeopleCount = data.activePeople
                        )
                    }
                }
            }
        }
    }

    override fun handleEvent(event: HomeContract.HomeUiEvent) {
        when (event) {

            HomeContract.HomeUiEvent.OnPeopleClick ->
                setEffect { HomeContract.HomeUiEffect.NavigateToPeople }

            HomeContract.HomeUiEvent.OnAppointmentsClick ->
                setEffect { HomeContract.HomeUiEffect.NavigateToAppointments }

            HomeContract.HomeUiEvent.OnRetry ->
                loadHome()
        }
    }

    override fun createInitialState() = HomeContract.HomeUiState()
}