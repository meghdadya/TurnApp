package com.sample.turnapp.feature.people.presentation

import androidx.lifecycle.viewModelScope
import com.sample.turnapp.core.core.utils.DispatcherProvider
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.ui.base.BaseViewModel
import com.sample.turnapp.feature.people.domain.model.DeletePeopleParam
import com.sample.turnapp.feature.people.domain.model.GetPeopleParam
import com.sample.turnapp.feature.people.domain.model.PeopleFilter
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import com.sample.turnapp.feature.people.domain.model.SavePeopleParam
import com.sample.turnapp.feature.people.domain.usecase.DeletePeopleUseCase
import com.sample.turnapp.feature.people.domain.usecase.GetPeopleListUseCase
import com.sample.turnapp.feature.people.domain.usecase.RestorePeopleUseCase
import com.sample.turnapp.feature.people.domain.usecase.SavePeopleUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleViewModel(
    private val getPeopleList: GetPeopleListUseCase,
    private val deletePeople: DeletePeopleUseCase,
    private val restorePeople: RestorePeopleUseCase,
    private val savePeople: SavePeopleUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<
        PeopleContract.PeopleUiEvent,
        PeopleContract.PeopleUiState,
        PeopleContract.PeopleUiEffect
        >() {

    // -------------------- INIT --------------------

    val initialData = emptyFlow<Unit>()
        .onStart { fetchPeople() }
        .viewModelStateIn(
            started = SharingStarted.WhileSubscribed(0),
            initialValue = Unit
        )

    // -------------------- FETCH --------------------

    private suspend fun fetchPeople() {
        setState {
            copy(
                isInitialLoading = true,
                isLoading = true,
                hasError = false
            )
        }

        when (val result = getPeopleList(GetPeopleParam())) {

            is Resource.Failure -> {
                setState {
                    copy(
                        isInitialLoading = false,
                        isLoading = false,
                        hasError = true
                    )
                }
            }

            is Resource.Success -> {
                val people = result.data.item

                setState {
                    copy(
                        isInitialLoading = false,
                        isLoading = false,
                        hasError = false,
                        people = people,
                        filteredPeople = applyFilters(
                            people,
                            searchQuery,
                            selectedFilter
                        )
                    )
                }
            }
        }
    }

    // -------------------- EVENTS --------------------

    override fun handleEvent(event: PeopleContract.PeopleUiEvent) {
        when (event) {

            is PeopleContract.PeopleUiEvent.OnSearchQueryChange ->
                updateSearch(event.query)

            is PeopleContract.PeopleUiEvent.OnFilterSelected ->
                updateFilter(event.filter)

            is PeopleContract.PeopleUiEvent.OnSavePerson ->
                savePerson(
                    SavePeopleParam(
                        id = event.id,
                        firstName = event.firstName,
                        lastName = event.lastName,
                        socialNumber = event.socialNumber,
                        phoneNumber = event.phoneNumber
                    )
                )

            is PeopleContract.PeopleUiEvent.OnPersonClick ->
                setEffect {
                    PeopleContract.PeopleUiEffect.NavigateToPersonDetails(event.id)
                }

            is PeopleContract.PeopleUiEvent.OnDeleteClick ->
                deletePerson(event.id,event.deleteReason)

            is PeopleContract.PeopleUiEvent.OnRestoreClick ->
                restorePerson(event.id)

            PeopleContract.PeopleUiEvent.Retry ->
                retry()
        }
    }

    // -------------------- SAVE --------------------

    private fun savePerson(param: SavePeopleParam) {
        viewModelScope.launch(dispatcherProvider.io) {

            when (savePeople(param)) {

                is Resource.Failure -> {
                    setEffect { PeopleContract.PeopleUiEffect.ShowError }
                }

                is Resource.Success -> {
                    fetchPeople()
                }
            }
        }
    }

    // -------------------- DELETE --------------------

    private fun deletePerson(id: Int, deleteReason: String) {
        viewModelScope.launch(dispatcherProvider.io) {

            setState { copy(deletingPersonId = id) }

            when (deletePeople(DeletePeopleParam(id, deleteReason))) {

                is Resource.Failure -> {
                    setState { copy(deletingPersonId = null) }

                    setEffect {
                        PeopleContract.PeopleUiEffect.ShowError
                    }
                }

                is Resource.Success -> {

                    val updated = currentState.people.map {
                        if (it.id == id) it.copy(deleted = true) else it
                    }

                    setState {
                        copy(
                            deletingPersonId = null,
                            people = updated,
                            filteredPeople = applyFilters(
                                updated,
                                searchQuery,
                                selectedFilter
                            )
                        )
                    }

                    setEffect {
                        PeopleContract.PeopleUiEffect.ShowUndoDeleteSnackbar(id)
                    }
                }
            }
        }
    }

    // -------------------- RESTORE --------------------

    private fun restorePerson(id: Int) {
        viewModelScope.launch(dispatcherProvider.io) {

            when (restorePeople(id)) {

                is Resource.Failure -> {
                    setEffect { PeopleContract.PeopleUiEffect.ShowError }
                }

                is Resource.Success -> {

                    val updated = currentState.people.map {
                        if (it.id == id) it.copy(deleted = false) else it
                    }

                    setState {
                        copy(
                            people = updated,
                            filteredPeople = applyFilters(
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
                filteredPeople = applyFilters(people, query, selectedFilter)
            )
        }
    }

    // -------------------- FILTER --------------------

    private fun updateFilter(filter: PeopleFilter) {
        setState {
            copy(
                selectedFilter = filter,
                filteredPeople = applyFilters(people, searchQuery, filter)
            )
        }
    }

    // -------------------- RETRY --------------------

    private fun retry() {
        viewModelScope.launch {
            fetchPeople()
        }
    }

    // -------------------- FILTER LOGIC --------------------

    private fun applyFilters(
        people: List<PersonUiModel>,
        query: String,
        filter: PeopleFilter
    ): List<PersonUiModel> {

        return people.filter { person ->

            val matchesFilter = when (filter) {
                PeopleFilter.ALL -> true
                PeopleFilter.ACTIVE -> !person.deleted
                PeopleFilter.DELETED -> person.deleted
            }

            val fullName = "${person.firstName} ${person.lastName}"

            val matchesSearch =
                query.isBlank() ||
                        fullName.contains(query, ignoreCase = true) ||
                        person.nationalCode.contains(query, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    // -------------------- STATE --------------------

    override fun createInitialState(): PeopleContract.PeopleUiState =
        PeopleContract.PeopleUiState()
}