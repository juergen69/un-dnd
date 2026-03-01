package com.smsdndmanager.domain.model

/**
 * Represents an authorized phone number that can trigger DND deactivation
 */
data class AuthorizedNumber(
    val id: String,
    val phoneNumber: String,
    val displayName: String?,
    val createdAt: Long
)
