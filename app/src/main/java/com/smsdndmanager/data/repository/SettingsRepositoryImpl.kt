package com.smsdndmanager.data.repository

import com.smsdndmanager.data.local.EncryptedPreferencesDataSource
import com.smsdndmanager.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SettingsRepository using encrypted preferences
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: EncryptedPreferencesDataSource
) : SettingsRepository {

    override fun shouldSendConfirmation(): Flow<Boolean> = flow {
        emit(dataSource.shouldSendConfirmation())
    }

    override suspend fun setSendConfirmation(enabled: Boolean) {
        dataSource.setSendConfirmation(enabled)
    }

    override fun getActivationPattern(): Flow<String> = flow {
        emit(dataSource.getActivationPattern())
    }

    override suspend fun setActivationPattern(pattern: String) {
        dataSource.setActivationPattern(pattern)
    }
}
