package com.example.task_manager.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager.data.datastore.UserPreferences
import com.example.task_manager.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val currentLanguage: String = "vi",
    val userName: String? = null,
    val userEmail: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.language.collect { lang ->
                _uiState.update { it.copy(currentLanguage = lang) }
            }
        }
        viewModelScope.launch {
            userPreferences.userName.collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }
        viewModelScope.launch {
            userPreferences.userEmail.collect { email ->
                _uiState.update { it.copy(userEmail = email) }
            }
        }
    }

    fun selectLanguage(langCode: String) {
        viewModelScope.launch {
            userPreferences.setLanguage(langCode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.logout()
                _uiState.update { it.copy(isLoading = false, isLoggedOut = true) }
            }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}