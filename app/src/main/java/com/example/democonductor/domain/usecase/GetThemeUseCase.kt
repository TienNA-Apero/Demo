package com.example.democonductor.domain.usecase

import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Flow<ThemeMode> {
        return themeRepository.getThemeMode()
    }
}