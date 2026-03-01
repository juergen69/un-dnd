package com.smsdndmanager.domain.model

/**
 * Represents a contact from the device's contact list
 */
data class ContactInfo(
    val id: String,
    val name: String,
    val phoneNumber: String
)
