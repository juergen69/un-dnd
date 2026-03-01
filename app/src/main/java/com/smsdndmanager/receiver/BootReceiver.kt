package com.smsdndmanager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Receiver for BOOT_COMPLETED to ensure the app is aware of device restart
 * The SMS receiver is automatically re-registered by the system after boot
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_QUICKBOOT_POWERON -> {
                Log.d(TAG, "Device booted, SMS DND Manager is ready")
                // The manifest-declared SMS receiver will be automatically re-registered
                // We could add additional initialization here if needed
            }
        }
    }
}
