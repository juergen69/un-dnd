package com.smsdndmanager.domain.usecase

import com.smsdndmanager.data.repository.ContactRepository
import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.model.ContactInfo
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

/**
 * Use case for importing contacts as authorized numbers
 */
class ImportContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val authorizedNumberRepository: AuthorizedNumberRepository
) {
    /**
     * Get all contacts from device
     */
    suspend fun getAllContacts(): List<ContactInfo> {
        return contactRepository.getAllContacts()
    }

    /**
     * Search contacts by name or phone number
     */
    suspend fun searchContacts(query: String): List<ContactInfo> {
        return contactRepository.searchContacts(query)
    }

    /**
     * Import a contact as an authorized number
     */
    suspend fun importContact(contact: ContactInfo): Result<Unit> {
        val normalizedNumber = authorizedNumberRepository.normalizePhoneNumber(contact.phoneNumber)
        
        // Check if already exists
        if (authorizedNumberRepository.isAuthorized(normalizedNumber)) {
            return Result.failure(IllegalArgumentException("Contact already authorized"))
        }

        val number = AuthorizedNumber(
            id = UUID.randomUUID().toString(),
            phoneNumber = normalizedNumber,
            displayName = contact.name,
            createdAt = System.currentTimeMillis()
        )

        return authorizedNumberRepository.addNumber(number)
    }

    /**
     * Import multiple contacts
     */
    suspend fun importContacts(contacts: List<ContactInfo>): ImportResult {
        var successCount = 0
        var duplicateCount = 0
        var errorCount = 0

        contacts.forEach { contact ->
            when (val result = importContact(contact)) {
                is Result.Success -> successCount++
                is Result.Failure -> {
                    if (result.exceptionOrNull()?.message?.contains("already") == true) {
                        duplicateCount++
                    } else {
                        errorCount++
                    }
                }
            }
        }

        return ImportResult(successCount, duplicateCount, errorCount)
    }

    data class ImportResult(
        val successCount: Int,
        val duplicateCount: Int,
        val errorCount: Int
    ) {
        val totalProcessed: Int get() = successCount + duplicateCount + errorCount
    }
}
