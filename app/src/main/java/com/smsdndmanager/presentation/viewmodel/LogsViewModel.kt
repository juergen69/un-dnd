package com.smsdndmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smsdndmanager.domain.model.SmsLogEntry
import com.smsdndmanager.domain.usecase.ClearLogsUseCase
import com.smsdndmanager.domain.usecase.GetSmsLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for SMS logs screen
 */
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val getLogsUseCase: GetSmsLogsUseCase,
    private val clearLogsUseCase: ClearLogsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogsUiState())
    val uiState: StateFlow<LogsUiState> = _uiState.asStateFlow()

    init {
        loadLogs()
    }

    private fun loadLogs() {
        viewModelScope.launch {
            getLogsUseCase().collect { logs ->
                _uiState.value = _uiState.value.copy(
                    logs = logs,
                    isLoading = false
                )
            }
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            clearLogsUseCase()
        }
    }
}

data class LogsUiState(
    val logs: List<SmsLogEntry> = emptyList(),
    val isLoading: Boolean = true
)
