package com.smsdndmanager.presentation.screen

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import com.smsdndmanager.domain.model.SmsMessage
import com.smsdndmanager.domain.usecase.ProcessSmsUseCase
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

@HiltViewModel
class TestViewModel @Inject constructor(
    private val processSmsUseCase: ProcessSmsUseCase,
    private val authorizedNumberRepository: AuthorizedNumberRepository
) : ViewModel() {
    
    suspend fun processTestMessage(phoneNumber: String, message: String): String {
        val smsMessage = SmsMessage(
            senderNumber = phoneNumber,
            body = message,
            timestamp = System.currentTimeMillis()
        )
        
        val result = processSmsUseCase(smsMessage)
        
        return result.fold(
            onSuccess = { successResult ->
                when (successResult) {
                    is ProcessSmsUseCase.ProcessResult.Success -> "SUCCESS: DND disabled, volume set to ${successResult.volumeSet}%"
                    is ProcessSmsUseCase.ProcessResult.Ignored -> "IGNORED: ${successResult.reason}"
                }
            },
            onFailure = { "FAILED: ${it.message}" }
        )
    }
    
    suspend fun getAuthorizedNumbers(): List<String> {
        return authorizedNumberRepository.getAllNumbers().first().map { it.phoneNumber }
    }
    
    fun normalizePhoneNumber(number: String): String {
        return authorizedNumberRepository.normalizePhoneNumber(number)
    }
}

@Composable
fun TestScreen(
    viewModel: TestViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var testResults by remember { mutableStateOf("Test results will appear here...") }
    
    // Message simulation state
    var simPhoneNumber by remember { mutableStateOf("") }
    var simMessage by remember { mutableStateOf("undnd50") }
    var authorizedNumbers by remember { mutableStateOf<List<String>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        authorizedNumbers = viewModel.getAuthorizedNumbers()
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Test & Diagnostics",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Authorized Numbers Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Authorized Numbers (${authorizedNumbers.size})",
                    style = MaterialTheme.typography.titleLarge
                )
                
                if (authorizedNumbers.isEmpty()) {
                    Text(
                        text = "No authorized numbers configured!\nAdd numbers in the Authorized Numbers tab.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    authorizedNumbers.forEach { number ->
                        val normalized = viewModel.normalizePhoneNumber(number)
                        Text(
                            text = "• $number\n  (normalized: $normalized)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Message Simulation
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Simulate Incoming Message",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = "Test if the app recognizes a message from a specific number",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                OutlinedTextField(
                    value = simPhoneNumber,
                    onValueChange = { simPhoneNumber = it },
                    label = { Text("Sender Phone Number") },
                    placeholder = { Text("+1234567890 or 0676...") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = simMessage,
                    onValueChange = { simMessage = it },
                    label = { Text("Message Text") },
                    placeholder = { Text("undnd50") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Button(
                    onClick = {
                        scope.launch {
                            val normalizedInput = viewModel.normalizePhoneNumber(simPhoneNumber)
                            val normalizedAuth = authorizedNumbers.map { viewModel.normalizePhoneNumber(it) }
                            
                            testResults = buildString {
                                appendLine("=== SIMULATION ===")
                                appendLine("Input number: $simPhoneNumber")
                                appendLine("Normalized: $normalizedInput")
                                appendLine("Message: $simMessage")
                                appendLine()
                                appendLine("Authorized (normalized):")
                                normalizedAuth.forEach { appendLine("  - $it") }
                                appendLine()
                                appendLine("Match found: ${normalizedAuth.contains(normalizedInput)}")
                                appendLine()
                                
                                val result = viewModel.processTestMessage(simPhoneNumber, simMessage)
                                appendLine("RESULT: $result")
                            }
                            
                            // Show snackbar notification
                            snackbarHostState.showSnackbar("Test simulation completed")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = simPhoneNumber.isNotBlank() && simMessage.isNotBlank()
                ) {
                    Text("Simulate Message & Process")
                }
            }
        }
        
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
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { testResults = testDndFunctionality(context, 50) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vol 50%")
                    }
                    
                    Button(
                        onClick = { testResults = testDndFunctionality(context, 100) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vol 100%")
                    }
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
