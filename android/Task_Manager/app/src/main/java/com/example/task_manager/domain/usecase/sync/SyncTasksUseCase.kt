package com.example.task_manager.domain.usecase.sync

import com.example.task_manager.domain.repository.TaskRepository
import javax.inject.Inject

class SyncTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        taskRepository.syncTasks()
    }
}