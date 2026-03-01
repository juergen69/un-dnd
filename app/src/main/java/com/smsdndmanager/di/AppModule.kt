package com.smsdndmanager.di

import com.smsdndmanager.data.repository.AuthorizedNumberRepositoryImpl
import com.smsdndmanager.data.repository.SettingsRepositoryImpl
import com.smsdndmanager.data.repository.SmsLogRepositoryImpl
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import com.smsdndmanager.domain.repository.SettingsRepository
import com.smsdndmanager.domain.repository.SmsLogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthorizedNumberRepository(
        impl: AuthorizedNumberRepositoryImpl
    ): AuthorizedNumberRepository

    @Binds
    @Singleton
    abstract fun bindSmsLogRepository(
        impl: SmsLogRepositoryImpl
    ): SmsLogRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
