package com.smsdndmanager.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure data source using EncryptedSharedPreferences
 */
@Singleton
class EncryptedPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _numbersFlow = MutableStateFlow<List<AuthorizedNumberEntity>>(emptyList())
    private val _logsFlow = MutableStateFlow<List<SmsLogEntity>>(emptyList())

    companion object {
        private const val PREFS_FILE_NAME = "sms_dnd_secure_prefs"
        private const val KEY_NUMBERS = "authorized_numbers"
        private const val KEY_LOGS = "sms_logs"
        private const val KEY_SEND_CONFIRMATION = "send_confirmation"
        private const val KEY_ACTIVATION_PATTERN = "activation_pattern"
        private const val DEFAULT_PATTERN = "undnd"
    }

    init {
        // Load initial data
        _numbersFlow.value = loadNumbers()
        _logsFlow.value = loadLogs()
    }

    // Authorized Numbers
    fun getNumbersFlow(): Flow<List<AuthorizedNumberEntity>> = _numbersFlow.asStateFlow()

    fun getNumbers(): List<AuthorizedNumberEntity> = _numbersFlow.value

    suspend fun saveNumbers(numbers: List<AuthorizedNumberEntity>) = withContext(Dispatchers.IO) {
        val json = numbersToJson(numbers)
        encryptedPrefs.edit().putString(KEY_NUMBERS, json).apply()
        _numbersFlow.value = numbers
    }

    private fun loadNumbers(): List<AuthorizedNumberEntity> {
        val json = encryptedPrefs.getString(KEY_NUMBERS, null) ?: return emptyList()
        return jsonToNumbers(json)
    }

    // SMS Logs
    fun getLogsFlow(): Flow<List<SmsLogEntity>> = _logsFlow.asStateFlow()

    suspend fun saveLogs(logs: List<SmsLogEntity>) = withContext(Dispatchers.IO) {
        val json = logsToJson(logs)
        encryptedPrefs.edit().putString(KEY_LOGS, json).apply()
        _logsFlow.value = logs
    }

    private fun loadLogs(): List<SmsLogEntity> {
        val json = encryptedPrefs.getString(KEY_LOGS, null) ?: return emptyList()
        return jsonToLogs(json)
    }

    // Settings
    fun shouldSendConfirmation(): Boolean {
        return encryptedPrefs.getBoolean(KEY_SEND_CONFIRMATION, false)
    }

    suspend fun setSendConfirmation(enabled: Boolean) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().putBoolean(KEY_SEND_CONFIRMATION, enabled).apply()
    }

    fun getActivationPattern(): String {
        return encryptedPrefs.getString(KEY_ACTIVATION_PATTERN, DEFAULT_PATTERN) ?: DEFAULT_PATTERN
    }

    suspend fun setActivationPattern(pattern: String) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().putString(KEY_ACTIVATION_PATTERN, pattern).apply()
    }

    // JSON Serialization (simple implementation)
    private fun numbersToJson(numbers: List<AuthorizedNumberEntity>): String {
        return numbers.joinToString("|") { "${it.id}#${it.phoneNumber}#${it.displayName ?: ""}#${it.createdAt}" }
    }

    private fun jsonToNumbers(json: String): List<AuthorizedNumberEntity> {
        if (json.isBlank()) return emptyList()
        return json.split("|").map { entry ->
            val parts = entry.split("#")
            AuthorizedNumberEntity(
                id = parts[0],
                phoneNumber = parts[1],
                displayName = parts[2].takeIf { it.isNotEmpty() },
                createdAt = parts[3].toLong()
            )
        }
    }

    private fun logsToJson(logs: List<SmsLogEntity>): String {
        return logs.joinToString("|") { 
            "${it.id}#${it.senderNumber}#${it.command}#${it.volumeSet}#${it.timestamp}#${it.success}" 
        }
    }

    private fun jsonToLogs(json: String): List<SmsLogEntity> {
        if (json.isBlank()) return emptyList()
        return json.split("|").map { entry ->
            val parts = entry.split("#")
            SmsLogEntity(
                id = parts[0],
                senderNumber = parts[1],
                command = parts[2],
                volumeSet = parts[3].toInt(),
                timestamp = parts[4].toLong(),
                success = parts[5].toBoolean()
            )
        }
    }
}

// Data Entities for local storage
data class AuthorizedNumberEntity(
    val id: String,
    val phoneNumber: String,
    val displayName: String?,
    val createdAt: Long
)

data class SmsLogEntity(
    val id: String,
    val senderNumber: String,
    val command: String,
    val volumeSet: Int,
    val timestamp: Long,
    val success: Boolean
)
