package com.example.democonductor.presentation.home

sealed interface HomeEvent {
    data class ShowMessage(val message: String) : HomeEvent
}