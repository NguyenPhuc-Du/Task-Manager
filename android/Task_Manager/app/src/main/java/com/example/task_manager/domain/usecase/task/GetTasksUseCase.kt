package com.example.task_manager.domain.usecase.task

import com.example.task_manager.domain.model.Task
import com.example.task_manager.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getTasks()
    }
}