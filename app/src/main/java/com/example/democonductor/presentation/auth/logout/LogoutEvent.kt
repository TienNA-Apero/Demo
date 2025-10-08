package com.example.democonductor.presentation.auth.logout

sealed interface LogoutEvent {
    object NavigateToLogin : LogoutEvent
    data class ShowError(val message: String) : LogoutEvent
}
