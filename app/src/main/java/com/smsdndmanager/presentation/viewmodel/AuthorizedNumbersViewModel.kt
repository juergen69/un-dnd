package com.smsdndmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.model.ContactInfo
import com.smsdndmanager.domain.usecase.ImportContactsUseCase
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
    private val manageNumbersUseCase: ManageAuthorizedNumbersUseCase,
    private val importContactsUseCase: ImportContactsUseCase
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

    fun loadContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingContacts = true)
            try {
                val contacts = importContactsUseCase.getAllContacts()
                _uiState.value = _uiState.value.copy(
                    contacts = contacts,
                    isLoadingContacts = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load contacts: ${e.message}",
                    isLoadingContacts = false
                )
            }
        }
    }

    fun searchContacts(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingContacts = true)
            try {
                val contacts = importContactsUseCase.searchContacts(query)
                _uiState.value = _uiState.value.copy(
                    contacts = contacts,
                    isLoadingContacts = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to search contacts: ${e.message}",
                    isLoadingContacts = false
                )
            }
        }
    }

    fun importContact(contact: ContactInfo) {
        viewModelScope.launch {
            val result = importContactsUseCase.importContact(contact)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        importSuccess = "Contact '${contact.name}' imported successfully"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to import contact"
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

    fun clearImportSuccess() {
        _uiState.value = _uiState.value.copy(importSuccess = null)
    }
}

data class AuthorizedNumbersUiState(
    val numbers: List<AuthorizedNumber> = emptyList(),
    val contacts: List<ContactInfo> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingContacts: Boolean = false,
    val error: String? = null,
    val addSuccess: Boolean = false,
    val importSuccess: String? = null
)
