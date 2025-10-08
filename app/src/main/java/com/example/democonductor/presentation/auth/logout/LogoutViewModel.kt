package com.example.democonductor.presentation.auth.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democonductor.domain.usecase.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LogoutState())
    val state: StateFlow<LogoutState> = _state.asStateFlow()

    private val _event = Channel<LogoutEvent>(Channel.BUFFERED)
    val event: Flow<LogoutEvent> = _event.receiveAsFlow()

    fun handleIntent(intent: LogoutIntent) {
        when (intent) {
            is LogoutIntent.LogoutClicked -> logout()
        }
    }

    private fun logout() = viewModelScope.launch {
        updateState { copy(isLoading = true) }

        logoutUseCase()
            .onSuccess {
                _event.send(LogoutEvent.NavigateToLogin)
            }
            .onFailure { error ->
                _event.send(LogoutEvent.ShowError(error.message ?: "Logout failed"))
            }

        updateState { copy(isLoading = false) }
    }

    private fun updateState(update: LogoutState.() -> LogoutState) {
        _state.update(update)
    }
}
