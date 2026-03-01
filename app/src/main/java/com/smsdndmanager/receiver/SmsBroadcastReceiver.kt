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
        android.util.Log.d("SmsBroadcastReceiver", "Received intent: ${intent.action}")
        
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            android.util.Log.d("SmsBroadcastReceiver", "Not SMS_RECEIVED_ACTION, ignoring")
            return
        }

        // Extract SMS messages from the intent
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        if (messages.isNullOrEmpty()) {
            android.util.Log.d("SmsBroadcastReceiver", "No messages in intent")
            return
        }

        android.util.Log.d("SmsBroadcastReceiver", "Found ${messages.size} message(s)")

        // Process each SMS message
        messages.forEach { smsMessage ->
            val sender = smsMessage.originatingAddress ?: run {
                android.util.Log.d("SmsBroadcastReceiver", "Missing originating address, skipping")
                return@forEach
            }
            val body = smsMessage.messageBody ?: run {
                android.util.Log.d("SmsBroadcastReceiver", "Missing message body, skipping")
                return@forEach
            }
            val timestamp = smsMessage.timestampMillis

            android.util.Log.d("SmsBroadcastReceiver", "Processing message from: $sender, body: $body")

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
                                android.util.Log.d("SmsBroadcastReceiver", "Successfully processed message: volume set to ${processResult.volumeSet}%")
                                // Show notification to user
                                val notificationHelper = com.smsdndmanager.service.DndActionNotificationHelper(context)
                                notificationHelper.showDndDisabledNotification(
                                    processResult.displayName,
                                    processResult.volumeSet
                                )
                            }
                            is ProcessSmsUseCase.ProcessResult.Ignored -> {
                                android.util.Log.d("SmsBroadcastReceiver", "Ignored message: ${processResult.reason}")
                            }
                        }
                    }
                    result.exceptionOrNull()?.let {
                        android.util.Log.e("SmsBroadcastReceiver", "Error processing message", it)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("SmsBroadcastReceiver", "Exception processing message", e)
                }
            }
        }
    }
}
