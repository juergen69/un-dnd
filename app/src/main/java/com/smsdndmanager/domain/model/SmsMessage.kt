package com.smsdndmanager.domain.model

/**
 * Represents an incoming SMS message
 */
data class SmsMessage(
    val senderNumber: String,
    val body: String,
    val timestamp: Long
)
