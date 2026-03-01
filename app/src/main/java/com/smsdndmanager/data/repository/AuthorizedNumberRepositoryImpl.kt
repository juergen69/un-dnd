package com.smsdndmanager.data.repository

import com.smsdndmanager.data.local.EncryptedPreferencesDataSource
import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthorizedNumberRepository using encrypted preferences
 */
@Singleton
class AuthorizedNumberRepositoryImpl @Inject constructor(
    private val dataSource: EncryptedPreferencesDataSource
) : AuthorizedNumberRepository {

    override fun getAllNumbers(): Flow<List<AuthorizedNumber>> {
        return dataSource.getNumbersFlow().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun isAuthorized(phoneNumber: String): Boolean {
        val normalizedInput = normalizePhoneNumber(phoneNumber)
        return dataSource.getNumbers().any { 
            normalizePhoneNumber(it.phoneNumber) == normalizedInput 
        }
    }

    override suspend fun getDisplayName(phoneNumber: String): String? {
        val normalizedInput = normalizePhoneNumber(phoneNumber)
        val matchingNumber = dataSource.getNumbers().find {
            normalizePhoneNumber(it.phoneNumber) == normalizedInput
        }
        return matchingNumber?.displayName ?: matchingNumber?.phoneNumber
    }

    override suspend fun addNumber(number: AuthorizedNumber): Result<Unit> {
        return try {
            val currentNumbers = dataSource.getNumbers().map { it.toDomainModel() }.toMutableList()
            currentNumbers.add(number)
            dataSource.saveNumbers(currentNumbers.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeNumber(id: String): Result<Unit> {
        return try {
            val currentNumbers = dataSource.getNumbers().map { it.toDomainModel() }.toMutableList()
            currentNumbers.removeAll { it.id == id }
            dataSource.saveNumbers(currentNumbers.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun normalizePhoneNumber(phoneNumber: String): String {
        // Remove all non-digit characters except leading +
        val hasPlus = phoneNumber.trim().startsWith("+")
        val digitsOnly = phoneNumber.filter { it.isDigit() }
        
        return if (hasPlus && digitsOnly.isNotEmpty()) {
            "+$digitsOnly"
        } else {
            digitsOnly
        }
    }

    // Mapping functions
    private fun com.smsdndmanager.data.local.AuthorizedNumberEntity.toDomainModel(): AuthorizedNumber {
        return AuthorizedNumber(
            id = id,
            phoneNumber = phoneNumber,
            displayName = displayName,
            createdAt = createdAt
        )
    }

    private fun AuthorizedNumber.toEntity(): com.smsdndmanager.data.local.AuthorizedNumberEntity {
        return com.smsdndmanager.data.local.AuthorizedNumberEntity(
            id = id,
            phoneNumber = phoneNumber,
            displayName = displayName,
            createdAt = createdAt
        )
    }
}
