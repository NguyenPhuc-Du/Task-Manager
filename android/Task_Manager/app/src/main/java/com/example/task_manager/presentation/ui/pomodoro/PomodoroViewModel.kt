package com.example.task_manager.presentation.ui.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PomodoroPhase {
    WORK, SHORT_BREAK, LONG_BREAK
}

data class PomodoroUiState(
    val phase: PomodoroPhase = PomodoroPhase.WORK,
    val timeLeftSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val completedSessions: Int = 0,
    val totalSeconds: Int = 25 * 60
)

@HiltViewModel
class PomodoroViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState

    private var timerJob: Job? = null

    // Thời gian mỗi phase (giây)
    private val workDuration = 25 * 60
    private val shortBreakDuration = 5 * 60
    private val longBreakDuration = 15 * 60

    fun start() {
        if (_uiState.value.isRunning) return
        _uiState.value = _uiState.value.copy(isRunning = true)

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeftSeconds > 0) {
                delay(1000L)
                _uiState.value = _uiState.value.copy(
                    timeLeftSeconds = _uiState.value.timeLeftSeconds - 1
                )
            }
            onPhaseComplete()
        }
    }

    fun pause() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    fun reset() {
        timerJob?.cancel()
        val duration = getDuration(_uiState.value.phase)
        _uiState.value = _uiState.value.copy(
            isRunning = false,
            timeLeftSeconds = duration,
            totalSeconds = duration
        )
    }

    fun skipPhase() {
        timerJob?.cancel()
        onPhaseComplete()
    }

    private fun onPhaseComplete() {
        val current = _uiState.value
        val completedSessions = if (current.phase == PomodoroPhase.WORK) {
            current.completedSessions + 1
        } else {
            current.completedSessions
        }

        // Sau 4 pomodoro thì nghỉ dài
        val nextPhase = when {
            current.phase == PomodoroPhase.WORK && completedSessions % 4 == 0 -> PomodoroPhase.LONG_BREAK
            current.phase == PomodoroPhase.WORK -> PomodoroPhase.SHORT_BREAK
            else -> PomodoroPhase.WORK
        }

        val nextDuration = getDuration(nextPhase)
        _uiState.value = PomodoroUiState(
            phase = nextPhase,
            timeLeftSeconds = nextDuration,
            totalSeconds = nextDuration,
            isRunning = false,
            completedSessions = completedSessions
        )
    }

    private fun getDuration(phase: PomodoroPhase): Int {
        return when (phase) {
            PomodoroPhase.WORK -> workDuration
            PomodoroPhase.SHORT_BREAK -> shortBreakDuration
            PomodoroPhase.LONG_BREAK -> longBreakDuration
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}