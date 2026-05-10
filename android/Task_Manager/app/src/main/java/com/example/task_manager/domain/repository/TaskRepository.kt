package com.example.task_manager.domain.repository

import com.example.task_manager.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: String): Task?
    suspend fun createTask(
        title: String,
        description: String?,
        priority: Int,
        dueDate: String?,
        categoryId: String?
    ): Task
    suspend fun updateTask(
        id: String,
        title: String?,
        description: String?,
        priority: Int?,
        completed: Boolean?,
        categoryId: String?
    ): Task
    suspend fun deleteTask(id: String)
    suspend fun syncTasks()
}