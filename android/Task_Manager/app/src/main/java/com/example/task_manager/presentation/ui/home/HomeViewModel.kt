package com.example.task_manager.presentation.ui.home

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_manager.domain.model.Task
import com.example.task_manager.domain.repository.AuthRepository
import com.example.task_manager.domain.usecase.task.GetTasksUseCase
import com.example.task_manager.domain.usecase.sync.SyncTasksUseCase
import com.example.task_manager.data.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val userName: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTaskUseCase: GetTasksUseCase,
    private val syncTaskUseCase: SyncTasksUseCase,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadUserInfo()
        loadTasks()
        syncTasks()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            userPreferences.userName.collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            getTaskUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(tasks = tasks)
                }
        }
    }

    fun syncTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try{
                syncTaskUseCase()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
            catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}