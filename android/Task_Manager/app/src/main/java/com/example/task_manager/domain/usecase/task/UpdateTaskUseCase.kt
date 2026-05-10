package com.example.task_manager.domain.usecase.task

import com.example.task_manager.domain.model.Task
import com.example.task_manager.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String? = null,
        description: String? = null,
        priority: Int? = null,
        completed: Boolean? = null,
        categoryId: String? = null
    ): Task {
        return taskRepository.updateTask(id, title, description, priority, completed, categoryId)
    }
}