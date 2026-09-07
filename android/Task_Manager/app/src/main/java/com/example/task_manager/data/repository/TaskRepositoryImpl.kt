package com.example.task_manager.data.repository

import com.example.task_manager.data.local.dao.TaskDao
import com.example.task_manager.data.local.entity.TaskEntity
import com.example.task_manager.data.remote.api.TaskApiService
import com.example.task_manager.data.remote.api.SyncApiService
import com.example.task_manager.data.remote.dto.CreateTaskRequest
import com.example.task_manager.data.remote.dto.UpdateTaskRequest
import com.example.task_manager.domain.model.Task
import com.example.task_manager.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService,
    private val syncApiService: SyncApiService
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun createTask(
        title: String,
        description: String?,
        priority: Int,
        dueDate: String?,
        categoryId: String?
    ): Task {
        val dto = taskApiService.createTask(
            CreateTaskRequest(title, description, priority, dueDate, categoryId)
        )

        val entity = dto.toEntity()
        taskDao.insertTask(entity)

        return entity.toDomain()
    }

    override suspend fun updateTask(
        id: String,
        title: String?,
        description: String?,
        priority: Int?,
        completed: Boolean?,
        categoryId: String?
    ): Task {
        val dto = taskApiService.updateTask(
            id,
            UpdateTaskRequest(title, description, priority, completed, categoryId)
        )

        val entity = dto.toEntity()
        taskDao.insertTask(entity)

        return entity.toDomain()
    }

    override suspend fun syncTasks() {
        val tasks = syncApiService.syncTasks()
        taskDao.deleteAllTasks()
        taskDao.insertTasks(tasks.map { it.toEntity() })
    }

    override suspend fun deleteTask(id: String) {
        taskApiService.deleteTask(id)
        taskDao.deleteTask(id)
    }
}