package com.example.democonductor.domain.usecase

import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.domain.repository.ThemeRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        themeRepository.updateThemeMode(themeMode)
    }
}