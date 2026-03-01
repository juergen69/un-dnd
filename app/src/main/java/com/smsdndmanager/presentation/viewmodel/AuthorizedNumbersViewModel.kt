package com.smsdndmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.usecase.ManageAuthorizedNumbersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing authorized numbers screen
 */
@HiltViewModel
class AuthorizedNumbersViewModel @Inject constructor(
    private val manageNumbersUseCase: ManageAuthorizedNumbersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorizedNumbersUiState())
    val uiState: StateFlow<AuthorizedNumbersUiState> = _uiState.asStateFlow()

    init {
        loadNumbers()
    }

    private fun loadNumbers() {
        viewModelScope.launch {
            manageNumbersUseCase.getAllNumbers().collect { numbers ->
                _uiState.value = _uiState.value.copy(
                    numbers = numbers,
                    isLoading = false
                )
            }
        }
    }

    fun addNumber(phoneNumber: String, displayName: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = manageNumbersUseCase.addNumber(phoneNumber, displayName)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        addSuccess = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to add number"
                    )
                }
            )
        }
    }

    fun removeNumber(id: String) {
        viewModelScope.launch {
            val result = manageNumbersUseCase.removeNumber(id)
            result.fold(
                onSuccess = { },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to remove number"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearAddSuccess() {
        _uiState.value = _uiState.value.copy(addSuccess = false)
    }
}

data class AuthorizedNumbersUiState(
    val numbers: List<AuthorizedNumber> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val addSuccess: Boolean = false
)
