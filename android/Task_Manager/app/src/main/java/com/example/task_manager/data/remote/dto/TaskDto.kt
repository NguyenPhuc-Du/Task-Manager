package com.example.task_manager.data.remote.dto

data class TaskDto(
    val id: String,
    val title: String,
    val description: String?,
    val priority: Int,
    val completed: Boolean,
    val dueDate: String?,
    val categoryId: String?,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)

data class CreateTaskRequest(
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    val dueDate: String? = null,
    val categoryId: String? = null
)

data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val priority: Int? = null,
    val completed: Boolean? = null,
    val dueDate: String? = null,
    val categoryId: String? = null
)