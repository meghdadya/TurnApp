package com.sample.turnapp.core.ui.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val SIDE_EFFECTS_KEY = "side-effects_key"
const val SEARCH_INPUT_THRESHOLD: Long = 300

abstract class BaseViewModel<Event : BaseContract.UiEvent, UiState : BaseContract.UiState, Effect : BaseContract.UiEffect>(

) : ViewModel() {

    // Create Initial UiState of View
    private val initialState: UiState by lazy { createInitialState() }
    abstract fun createInitialState(): UiState

    // Get Current UiState
    val currentState: UiState
        get() = viewState.value

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    /**
     * Handle each event
     */
    abstract fun handleEvent(event: Event)

    /**
     * Set new Event
     */
    fun setEvent(event: Event) {
        handleEvent(event)
    }


    /**
     * Set new Ui UiState
     */
    protected fun setState(reduce: UiState.() -> UiState) {
        val newState = currentState.reduce()
        _viewState.value = newState
    }

    /**
     * Set new Effect
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.emit(effectValue) }
    }

    protected fun <T> Flow<T>.viewModelStateIn(
        started: SharingStarted,
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = initialValue
    )
}