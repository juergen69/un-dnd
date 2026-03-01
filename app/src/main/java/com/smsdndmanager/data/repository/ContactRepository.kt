package com.smsdndmanager.data.repository

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.smsdndmanager.domain.model.ContactInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for accessing device contacts
 */
@Singleton
class ContactRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val contentResolver: ContentResolver = context.contentResolver

    /**
     * Get all contacts with phone numbers
     */
    suspend fun getAllContacts(): List<ContactInfo> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<ContactInfo>()
        val seenIds = mutableSetOf<String>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            "${ContactsContract.Contacts.HAS_PHONE_NUMBER} > 0",
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                if (seenIds.add(id)) {
                    val name = it.getString(nameIndex) ?: "Unknown"
                    val phoneNumbers = getPhoneNumbersForContact(id)
                    
                    phoneNumbers.forEach { phoneNumber ->
                        contacts.add(
                            ContactInfo(
                                id = id,
                                name = name,
                                phoneNumber = phoneNumber
                            )
                        )
                    }
                }
            }
        }

        contacts
    }

    /**
     * Search contacts by name or phone number
     */
    suspend fun searchContacts(query: String): List<ContactInfo> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            return@withContext getAllContacts()
        }

        val contacts = mutableListOf<ContactInfo>()
        val lowerQuery = query.lowercase()

        getAllContacts().filter { contact ->
            contact.name.lowercase().contains(lowerQuery) ||
            contact.phoneNumber.contains(query)
        }
    }

    /**
     * Get phone numbers for a specific contact
     */
    private fun getPhoneNumbersForContact(contactId: String): List<String> {
        val phoneNumbers = mutableListOf<String>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val normalizedIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)

            while (it.moveToNext()) {
                val number = it.getString(normalizedIndex) 
                    ?: it.getString(numberIndex)
                    ?: continue
                phoneNumbers.add(number)
            }
        }

        return phoneNumbers
    }
}
