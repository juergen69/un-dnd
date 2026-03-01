package com.smsdndmanager.service

import android.app.Notification
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.smsdndmanager.domain.model.SmsMessage
import com.smsdndmanager.domain.usecase.ProcessSmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Notification Listener Service that monitors incoming message notifications
 * Works with both SMS and RCS messages from Google Messages and other SMS apps
 */
@AndroidEntryPoint
class MessageNotificationListenerService : NotificationListenerService() {

    @Inject
    lateinit var processSmsUseCase: ProcessSmsUseCase

    private lateinit var notificationHelper: DndActionNotificationHelper
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        private const val TAG = "MessageNotificationService"
        
        // Known messaging app package names
        private val MESSAGING_PACKAGES = setOf(
            "com.google.android.apps.messaging",    // Google Messages
            "com.samsung.android.messaging",         // Samsung Messages
            "com.android.mms",                       // Android Messages (stock)
            "com.whatsapp",                          // WhatsApp
            "com.whatsapp.w4b",                      // WhatsApp Business
            "com.facebook.orca",                     // Facebook Messenger
            "com.google.android.apps.tachyon",       // Google Duo/Messages
            "com.microsoft.android.smsorganizer",    // SMS Organizer
            "com.jb.gosms",                          // GO SMS Pro
            "com.handcent.nextsms",                  // Handcent Next SMS
            "com.p1.chompsms",                       // Chomp SMS
            "com.textra",                            // Textra SMS
            "com.moez.QKSMS",                        // QKSMS
            "com.simplemobiletools.smsmessenger",    // Simple SMS Messenger
        )
        
        // Notification extras keys that might contain message info
        private const val EXTRA_TITLE = "android.title"
        private const val EXTRA_TEXT = "android.text"
        private const val EXTRA_BIG_TEXT = "android.bigText"
        private const val EXTRA_MESSAGES = "android.messages"
        private const val EXTRA_SENDER = "android.messagingStyleUser"
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = DndActionNotificationHelper(this)
        Log.d(TAG, "Notification Listener Service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Notification Listener Service destroyed")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification Listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        
        Log.d(TAG, "Notification received from: $packageName")
        
        // Only process notifications from messaging apps
        if (!isMessagingApp(packageName)) {
            Log.d(TAG, "Ignoring - not a messaging app")
            return
        }
        
        val notification = sbn.notification
        val extras = notification.extras
        
        // Log all extras for debugging
        Log.d(TAG, "Notification extras keys: ${extras.keySet().joinToString()}")
        
        // Extract sender and message from notification
        val senderDisplay = extractSenderDisplay(extras, packageName)
        val messageText = extractMessageText(extras)
        
        Log.d(TAG, "Extracted sender display: '$senderDisplay', message: '$messageText'")
        
        if (messageText.isBlank()) {
            Log.w(TAG, "Message text is blank - skipping")
            return
        }
        
        // Try to resolve sender to phone number(s)
        val phoneNumbers = resolveSenderToPhoneNumbers(senderDisplay)
        
        Log.d(TAG, "Resolved to phone numbers: $phoneNumbers")
        
        if (phoneNumbers.isEmpty()) {
            // If we can't resolve to a phone number, try using the display name as-is
            // (in case the user authorized the contact name)
            Log.w(TAG, "Could not resolve to phone number, trying display name: $senderDisplay")
            processMessage(senderDisplay, messageText)
        } else {
            // Try each resolved phone number
            phoneNumbers.forEach { phoneNumber ->
                processMessage(phoneNumber, messageText)
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Not needed for this functionality
    }

    private fun processMessage(senderIdentifier: String, messageText: String) {
        val smsMessage = SmsMessage(
            senderNumber = senderIdentifier,
            body = messageText,
            timestamp = System.currentTimeMillis()
        )
        
        scope.launch {
            try {
                val result = processSmsUseCase(smsMessage)
                result.getOrNull()?.let { processResult ->
                    when (processResult) {
                        is ProcessSmsUseCase.ProcessResult.Success -> {
                            Log.d(TAG, "DND disabled by ${processResult.displayName}, volume set to ${processResult.volumeSet}%")
                            // Show notification to user
                            notificationHelper.showDndDisabledNotification(
                                processResult.displayName,
                                processResult.volumeSet
                            )
                        }
                        is ProcessSmsUseCase.ProcessResult.Ignored -> {
                            Log.d(TAG, "Message ignored: sender=$senderIdentifier")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing message", e)
            }
        }
    }

    /**
     * Check if the package is a known messaging app
     */
    private fun isMessagingApp(packageName: String): Boolean {
        if (MESSAGING_PACKAGES.contains(packageName)) {
            return true
        }
        
        // Also check if app has SMS/MMS capability
        return try {
            val pm = packageManager
            pm.checkPermission("android.permission.SEND_SMS", packageName) == 
                PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Extract sender display name from notification extras
     */
    private fun extractSenderDisplay(extras: Bundle, packageName: String): String {
        // Try different keys to find sender info
        val sender = when {
            extras.containsKey(EXTRA_SENDER) -> {
                extras.get(EXTRA_SENDER)?.toString()
            }
            extras.containsKey("android.messagingSender") -> {
                extras.getString("android.messagingSender")
            }
            extras.containsKey(EXTRA_TITLE) -> {
                extras.getString(EXTRA_TITLE)
            }
            else -> null
        }
        
        return sender ?: "unknown"
    }

    /**
     * Try to resolve a sender display name to phone number(s)
     * Returns list of phone numbers or empty list if cannot resolve
     */
    private fun resolveSenderToPhoneNumbers(senderDisplay: String): List<String> {
        val phoneNumbers = mutableListOf<String>()
        
        // If it looks like a phone number already, return it
        if (senderDisplay.matches(Regex("^[+0-9\\s\\-()]+$"))) {
            val cleaned = senderDisplay.filter { it.isDigit() || it == '+' }
            if (cleaned.length >= 7) {  // Minimum phone number length
                return listOf(cleaned)
            }
        }
        
        // Try to look up in contacts
        try {
            // Query contacts by display name
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(senderDisplay)
            
            contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                while (cursor.moveToNext()) {
                    val number = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    phoneNumbers.add(number)
                    Log.d(TAG, "Found phone number for '$senderDisplay': $number")
                }
            }
            
            // Also try partial match
            if (phoneNumbers.isEmpty()) {
                val fuzzySelection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
                val fuzzyArgs = arrayOf("%$senderDisplay%")
                
                contentResolver.query(uri, projection, fuzzySelection, fuzzyArgs, null)?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val number = cursor.getString(
                            cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        phoneNumbers.add(number)
                        Log.d(TAG, "Found phone number (fuzzy) for '$senderDisplay': $number")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error looking up contact", e)
        }
        
        return phoneNumbers
    }

    /**
     * Extract message text from notification extras
     */
    private fun extractMessageText(extras: Bundle): String {
        // Try different keys to find message text
        val text = when {
            extras.containsKey(EXTRA_BIG_TEXT) -> {
                extras.getCharSequence(EXTRA_BIG_TEXT)?.toString()
            }
            extras.containsKey(EXTRA_TEXT) -> {
                extras.getCharSequence(EXTRA_TEXT)?.toString()
            }
            extras.containsKey("android.summaryText") -> {
                extras.getCharSequence("android.summaryText")?.toString()
            }
            else -> null
        }
        
        return text ?: ""
    }
}
