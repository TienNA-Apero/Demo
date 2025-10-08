package com.example.democonductor.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.domain.usecase.GetThemeUseCase
import com.example.democonductor.domain.usecase.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event = Channel<HomeEvent>(Channel.BUFFERED)
    val event: Flow<HomeEvent> = _event.receiveAsFlow()

    init {
        observeTheme()
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ToggleThemeSelector -> toggleThemeSelector()
            is HomeIntent.DismissThemeSelector -> dismissThemeSelector()
            is HomeIntent.SelectTheme -> selectTheme(intent.themeMode)
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            getThemeUseCase().collect { themeMode ->
                _state.update { it.copy(currentTheme = themeMode) }
            }
        }
    }

    private fun toggleThemeSelector() {
        _state.update { it.copy(showThemeSelector = !it.showThemeSelector) }
    }

    private fun dismissThemeSelector() {
        _state.update { it.copy(showThemeSelector = false) }
    }

    private fun selectTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            updateThemeUseCase(themeMode)
            _state.update { it.copy(showThemeSelector = false) }
        }
    }
}