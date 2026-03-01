package com.smsdndmanager.presentation.screen

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.smsdndmanager.service.MessageNotificationListenerService

@Composable
fun PermissionScreen(onPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    var permissionStatus by remember { mutableStateOf("Checking permissions...") }
    var showCheckButton by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted && canModifyDnd(context) && hasNotificationAccess(context)) {
            onPermissionsGranted()
        } else {
            permissionStatus = getPermissionStatusText(context)
            showCheckButton = true
        }
    }
    
    // Check current permission status
    fun checkPermissions() {
        when {
            !hasSmsPermission(context) -> {
                permissionStatus = "SMS permission required to receive activation messages"
                showCheckButton = false
                // Launch permission request
                val permissionsToRequest = mutableListOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                }
                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }
            !hasNotificationAccess(context) -> {
                permissionStatus = "Notification access required to detect messages (including RCS/Chat)"
                showCheckButton = true
                // Open system settings for notification access
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            }
            !canModifyDnd(context) -> {
                permissionStatus = "Do Not Disturb access required to modify DND settings"
                showCheckButton = true
                // Open system settings for DND access
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                context.startActivity(intent)
            }
            else -> {
                // All permissions granted
                onPermissionsGranted()
            }
        }
    }
    
    LaunchedEffect(Unit) {
        checkPermissions()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = permissionStatus,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (showCheckButton) {
            Button(onClick = { checkPermissions() }) {
                Text("Check Again")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This app needs:\n• SMS access for fallback\n• Notification access for RCS/Chat support\n• DND access to modify settings\n• Audio access to change volume",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun hasSmsPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.RECEIVE_SMS
    ) == PackageManager.PERMISSION_GRANTED
}

private fun hasNotificationAccess(context: Context): Boolean {
    val cn = ComponentName(context, MessageNotificationListenerService::class.java)
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat != null && flat.contains(cn.flattenToString())
}

private fun canModifyDnd(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.isNotificationPolicyAccessGranted
}

private fun getPermissionStatusText(context: Context): String {
    val missing = mutableListOf<String>()
    if (!hasSmsPermission(context)) missing.add("SMS")
    if (!hasNotificationAccess(context)) missing.add("Notification Access")
    if (!canModifyDnd(context)) missing.add("DND Access")
    
    return if (missing.isEmpty()) {
        "All permissions granted"
    } else {
        "Missing permissions: ${missing.joinToString(", ")}"
    }
}
