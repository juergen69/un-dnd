package com.smsdndmanager.domain.repository

import com.smsdndmanager.domain.model.AuthorizedNumber
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing authorized phone numbers
 */
interface AuthorizedNumberRepository {
    
    /**
     * Get all authorized numbers as a Flow
     */
    fun getAllNumbers(): Flow<List<AuthorizedNumber>>
    
    /**
     * Check if a phone number is authorized
     */
    suspend fun isAuthorized(phoneNumber: String): Boolean
    
    /**
     * Add a new authorized number
     */
    suspend fun addNumber(number: AuthorizedNumber): Result<Unit>
    
    /**
     * Remove an authorized number
     */
    suspend fun removeNumber(id: String): Result<Unit>
    
    /**
     * Normalize phone number for consistent comparison
     */
    fun normalizePhoneNumber(phoneNumber: String): String
}
