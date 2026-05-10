package com.example.task_manager.domain.usecase.task

import com.example.task_manager.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: String) {
        taskRepository.deleteTask(id)
    }
}