package com.smsdndmanager.domain.model

/**
 * Represents a log entry for DND deactivation events
 */
data class SmsLogEntry(
    val id: String,
    val senderNumber: String,
    val command: String,
    val volumeSet: Int,
    val timestamp: Long,
    val success: Boolean
)
