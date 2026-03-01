package com.smsdndmanager.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for app settings
 */
interface SettingsRepository {
    
    /**
     * Get whether to send confirmation SMS replies
     */
    fun shouldSendConfirmation(): Flow<Boolean>
    
    /**
     * Set whether to send confirmation SMS replies
     */
    suspend fun setSendConfirmation(enabled: Boolean)
    
    /**
     * Get the activation code pattern (e.g., "undnd")
     */
    fun getActivationPattern(): Flow<String>
    
    /**
     * Set the activation code pattern
     */
    suspend fun setActivationPattern(pattern: String)
}
