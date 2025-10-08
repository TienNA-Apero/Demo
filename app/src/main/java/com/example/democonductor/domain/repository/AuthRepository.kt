package com.example.democonductor.domain.repository

interface AuthRepository {
    suspend fun logout(): Result<Unit>
}
