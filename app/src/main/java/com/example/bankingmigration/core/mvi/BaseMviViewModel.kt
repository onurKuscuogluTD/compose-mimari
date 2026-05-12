package com.example.bankingmigration.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<I : MviIntent, S : MviState, E : MviEffect>(
    initialState: S,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<E>(extraBufferCapacity = 1)
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    protected val currentState: S
        get() = _uiState.value

    abstract fun onIntent(intent: I)

    protected fun reduce(reducer: S.() -> S) {
        _uiState.update { current ->
            current.reducer()
        }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
