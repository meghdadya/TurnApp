package com.sample.turnapp.feature.people.presentation

import com.sample.turnapp.core.ui.base.BaseContract
import com.sample.turnapp.feature.people.domain.model.PeopleFilter
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import io.ktor.websocket.CloseReason

interface PeopleContract {



    // -------------------- UI EVENTS --------------------
    sealed interface PeopleUiEvent : BaseContract.UiEvent {

        data class OnSearchQueryChange(val query: String) : PeopleUiEvent

        data class OnFilterSelected(val filter: PeopleFilter) : PeopleUiEvent

        data class OnSavePerson(
            val id: Long = 0,
            val firstName: String,
            val lastName: String,
            val socialNumber: String,
            val phoneNumber: String
        ) : PeopleUiEvent

        data class OnPersonClick(val id: Int) : PeopleUiEvent

        data class OnDeleteClick(val id: Int, val deleteReason: String) : PeopleUiEvent

        data class OnRestoreClick(val id: Int): PeopleUiEvent
        object Retry : PeopleUiEvent
    }

    // -------------------- UI STATE --------------------
    data class PeopleUiState(
        val isLoading: Boolean = false,
        val isInitialLoading: Boolean = true,
        val hasError: Boolean = false,

        val searchQuery: String = "",
        val selectedFilter: PeopleFilter = PeopleFilter.ALL,

        val people: List<PersonUiModel> = emptyList(),
        val filteredPeople: List<PersonUiModel> = emptyList(),

        val deletingPersonId: Int? = null
    ) : BaseContract.UiState

    // -------------------- UI EFFECTS --------------------
    sealed interface PeopleUiEffect : BaseContract.UiEffect {

        object NavigateToAddPerson : PeopleUiEffect

        data class NavigateToPersonDetails(val id: Int) : PeopleUiEffect

        data class ShowUndoDeleteSnackbar(val id: Int) : PeopleUiEffect

        object ShowError : PeopleUiEffect
    }
}