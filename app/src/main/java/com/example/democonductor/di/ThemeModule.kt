package com.example.democonductor.di

import com.example.democonductor.data.local.ThemeLocalDataSource
import com.example.democonductor.data.repository.ThemeRepositoryImpl
import com.example.democonductor.domain.repository.ThemeRepository
import com.example.democonductor.domain.usecase.GetThemeUseCase
import com.example.democonductor.domain.usecase.UpdateThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemeRepository(
        themeLocalDataSource: ThemeLocalDataSource
    ): ThemeRepository {
        return ThemeRepositoryImpl(themeLocalDataSource)
    }

    @Provides
    fun provideGetThemeUseCase(
        themeRepository: ThemeRepository
    ): GetThemeUseCase {
        return GetThemeUseCase(themeRepository)
    }

    @Provides
    fun provideUpdateThemeUseCase(
        themeRepository: ThemeRepository
    ): UpdateThemeUseCase {
        return UpdateThemeUseCase(themeRepository)
    }
}