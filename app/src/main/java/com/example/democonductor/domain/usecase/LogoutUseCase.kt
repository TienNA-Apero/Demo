package com.example.democonductor.domain.usecase

import com.example.democonductor.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = authRepository.logout()
}
