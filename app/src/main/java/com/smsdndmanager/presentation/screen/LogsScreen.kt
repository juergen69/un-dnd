package com.smsdndmanager.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smsdndmanager.R
import com.smsdndmanager.domain.model.SmsLogEntry
import com.smsdndmanager.presentation.theme.SuccessGreen
import com.smsdndmanager.presentation.theme.ErrorRed
import com.smsdndmanager.presentation.viewmodel.LogsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Clear button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (uiState.logs.isNotEmpty()) {
                IconButton(onClick = { viewModel.clearLogs() }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear logs"
                    )
                }
            }
        }

        if (uiState.logs.isEmpty() && !uiState.isLoading) {
            EmptyLogsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.logs, key = { it.id }) { log ->
                    LogEntryCard(log = log)
                }
            }
        }
    }
}

@Composable
private fun EmptyLogsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No activity yet",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "DND deactivation events will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LogEntryCard(log: SmsLogEntry) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.log_from, log.senderNumber),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.log_volume, log.volumeSet),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateFormat.format(Date(log.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = if (log.success) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = if (log.success) "Success" else "Failed",
                tint = if (log.success) SuccessGreen else ErrorRed
            )
        }
    }
}
