package com.smsdndmanager.domain.usecase

import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

/**
 * Use case for managing authorized phone numbers
 */
class ManageAuthorizedNumbersUseCase @Inject constructor(
    private val repository: AuthorizedNumberRepository
) {
    /**
     * Get all authorized numbers
     */
    fun getAllNumbers(): Flow<List<AuthorizedNumber>> = repository.getAllNumbers()
    
    /**
     * Add a new authorized number
     */
    suspend fun addNumber(phoneNumber: String, displayName: String?): Result<Unit> {
        val normalizedNumber = repository.normalizePhoneNumber(phoneNumber)
        
        // Validate phone number
        if (normalizedNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("Phone number cannot be empty"))
        }
        
        // Check if number already exists
        if (repository.isAuthorized(normalizedNumber)) {
            return Result.failure(IllegalArgumentException("Number already exists"))
        }
        
        val number = AuthorizedNumber(
            id = UUID.randomUUID().toString(),
            phoneNumber = normalizedNumber,
            displayName = displayName?.takeIf { it.isNotBlank() },
            createdAt = System.currentTimeMillis()
        )
        
        return repository.addNumber(number)
    }
    
    /**
     * Remove an authorized number
     */
    suspend fun removeNumber(id: String): Result<Unit> {
        return repository.removeNumber(id)
    }
    
    /**
     * Check if a phone number is authorized
     */
    suspend fun isAuthorized(phoneNumber: String): Boolean {
        val normalizedNumber = repository.normalizePhoneNumber(phoneNumber)
        return repository.isAuthorized(normalizedNumber)
    }
}
