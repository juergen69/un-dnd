package com.smsdndmanager.domain.usecase

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.telephony.SmsManager
import androidx.core.content.getSystemService
import com.smsdndmanager.domain.model.SmsLogEntry
import com.smsdndmanager.domain.model.SmsMessage
import com.smsdndmanager.domain.model.VolumeCommand
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import com.smsdndmanager.domain.repository.SettingsRepository
import com.smsdndmanager.domain.repository.SmsLogRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

/**
 * Use case for processing incoming SMS messages
 * Checks if the sender is authorized, parses the command, and executes volume/DND changes
 */
class ProcessSmsUseCase @Inject constructor(
    private val authorizedNumberRepository: AuthorizedNumberRepository,
    private val smsLogRepository: SmsLogRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private val COMMAND_REGEX = Regex("undnd(\\d{1,3})", RegexOption.IGNORE_CASE)
    }
    
    /**
     * Process an incoming SMS message
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(smsMessage: SmsMessage): Result<ProcessResult> {
        // Check if sender is authorized
        val normalizedNumber = authorizedNumberRepository.normalizePhoneNumber(smsMessage.senderNumber)
        val isAuthorized = authorizedNumberRepository.isAuthorized(normalizedNumber)
        
        if (!isAuthorized) {
            return Result.success(ProcessResult.Ignored("Sender not authorized"))
        }
        
        // Parse command from message body
        val command = parseCommand(smsMessage.body)
            ?: return Result.success(ProcessResult.Ignored("No valid command found"))
        
        // Execute the command
        return try {
            executeCommand(command)
            
            // Log the successful action
            val logEntry = SmsLogEntry(
                id = UUID.randomUUID().toString(),
                senderNumber = smsMessage.senderNumber,
                command = "undnd${command.percentage}",
                volumeSet = command.percentage,
                timestamp = System.currentTimeMillis(),
                success = true
            )
            smsLogRepository.addLog(logEntry)
            
            // Send confirmation if enabled
            if (settingsRepository.shouldSendConfirmation().first()) {
                sendConfirmation(smsMessage.senderNumber, command.percentage)
            }
            
            Result.success(ProcessResult.Success(command.percentage))
        } catch (e: Exception) {
            // Log the failure
            val logEntry = SmsLogEntry(
                id = UUID.randomUUID().toString(),
                senderNumber = smsMessage.senderNumber,
                command = "undnd${command.percentage}",
                volumeSet = command.percentage,
                timestamp = System.currentTimeMillis(),
                success = false
            )
            smsLogRepository.addLog(logEntry)
            
            Result.failure(e)
        }
    }
    
    /**
     * Parse the volume command from SMS body
     * Pattern: undnd[0-100] (e.g., undnd50, undnd100)
     */
    fun parseCommand(message: String): VolumeCommand? {
        return COMMAND_REGEX.find(message)?.let { match ->
            val percentage = match.groupValues[1].toIntOrNull()?.coerceIn(0, 100)
            percentage?.let { VolumeCommand(it) }
        }
    }
    
    /**
     * Execute the volume and DND command
     */
    private fun executeCommand(command: VolumeCommand) {
        val audioManager = context.getSystemService<AudioManager>()
            ?: throw IllegalStateException("AudioManager not available")
        
        // Disable Do Not Disturb if active (API 24+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT ||
                audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE
            ) {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        }
        
        // Set volume to specified percentage
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        val targetVolume = (maxVolume * command.percentage / 100.0).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_RING, targetVolume, 0)
    }
    
    /**
     * Send a confirmation SMS reply
     */
    private fun sendConfirmation(phoneNumber: String, volume: Int) {
        try {
            val message = "DND Manager: Volume set to $volume%"
            SmsManager.getDefault().sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )
        } catch (e: Exception) {
            // Log but don't fail if confirmation can't be sent
            e.printStackTrace()
        }
    }
    
    /**
     * Result of processing an SMS
     */
    sealed class ProcessResult {
        data class Success(val volumeSet: Int) : ProcessResult()
        data class Ignored(val reason: String) : ProcessResult()
    }
}
