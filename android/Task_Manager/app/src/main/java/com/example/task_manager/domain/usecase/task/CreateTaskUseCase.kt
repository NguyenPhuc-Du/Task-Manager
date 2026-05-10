package com.example.task_manager.domain.usecase.task

import com.example.task_manager.domain.model.Task
import com.example.task_manager.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: Int = 0,
        dueDate: String? = null,
        categoryId: String? = null
    ): Task {
        if (title.isBlank()) throw IllegalArgumentException("Tiêu đề không được để trống")
        return taskRepository.createTask(title, description, priority, dueDate, categoryId)
    }
}