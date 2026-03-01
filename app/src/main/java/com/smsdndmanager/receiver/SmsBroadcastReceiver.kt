package com.smsdndmanager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.smsdndmanager.domain.model.SmsMessage
import com.smsdndmanager.domain.usecase.ProcessSmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Broadcast receiver for incoming SMS messages
 * Receives SMS_RECEIVED broadcasts and processes them
 */
@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var processSmsUseCase: ProcessSmsUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }

        // Extract SMS messages from the intent
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        if (messages.isNullOrEmpty()) {
            return
        }

        // Process each SMS message
        messages.forEach { smsMessage ->
            val sender = smsMessage.originatingAddress ?: return@forEach
            val body = smsMessage.messageBody ?: return@forEach
            val timestamp = smsMessage.timestampMillis

            val sms = SmsMessage(
                senderNumber = sender,
                body = body,
                timestamp = timestamp
            )

            // Process in a coroutine
            scope.launch {
                try {
                    val result = processSmsUseCase(sms)
                    result.getOrNull()?.let { processResult ->
                        when (processResult) {
                            is ProcessSmsUseCase.ProcessResult.Success -> {
                                // Show notification to user
                                val notificationHelper = com.smsdndmanager.service.DndActionNotificationHelper(context)
                                notificationHelper.showDndDisabledNotification(
                                    processResult.displayName,
                                    processResult.volumeSet
                                )
                            }
                            is ProcessSmsUseCase.ProcessResult.Ignored -> {
                                // Ignored - not authorized or no valid command
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
