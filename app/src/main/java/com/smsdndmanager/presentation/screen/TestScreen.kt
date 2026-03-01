package com.smsdndmanager.presentation.screen

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService

@Composable
fun TestScreen() {
    val context = LocalContext.current
    var testResults by remember { mutableStateOf("Test results will appear here...") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Test & Diagnostics",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Manual DND Test
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Manual DND Test",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = "Test if the app can disable DND and set volume",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Button(
                    onClick = { testResults = testDndFunctionality(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Test DND Disable + Volume 50%")
                }
                
                Button(
                    onClick = { testResults = testDndFunctionality(context, 100) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Test DND Disable + Volume 100%")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Notification Service Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Service Status",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Button(
                    onClick = { testResults = checkServiceStatus(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Check Notification Service Status")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Test Results
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Results:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = testResults,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun testDndFunctionality(context: Context, volumePercent: Int = 50): String {
    return try {
        val audioManager = context.getSystemService<AudioManager>()
            ?: return "ERROR: AudioManager not available"
        
        // Disable DND
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        
        // Set volume
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        val targetVolume = (maxVolume * volumePercent / 100.0).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_RING, targetVolume, AudioManager.FLAG_SHOW_UI)
        
        "SUCCESS: DND disabled, volume set to $volumePercent% ($targetVolume/$maxVolume)"
    } catch (e: Exception) {
        "ERROR: ${e.message}"
    }
}

private fun checkServiceStatus(context: Context): String {
    val sb = StringBuilder()
    
    // Check notification listener
    val enabledListeners = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    
    sb.appendLine("Notification Listeners enabled:")
    if (enabledListeners.isNullOrBlank()) {
        sb.appendLine("  NONE - Service not enabled!")
    } else {
        enabledListeners.split(":").forEach { 
            sb.appendLine("  - $it")
        }
    }
    
    sb.appendLine()
    
    // Check DND policy access
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    sb.appendLine("DND Policy Access: ${notificationManager.isNotificationPolicyAccessGranted}")
    
    return sb.toString()
}
