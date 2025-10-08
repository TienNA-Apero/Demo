package com.example.democonductor.presentation.auth.logout

sealed interface LogoutIntent {
    object LogoutClicked : LogoutIntent
}
