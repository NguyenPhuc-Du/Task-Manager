package com.example.task_manager.domain.model

data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    val completed: Boolean = false,
    val dueDate: String? = null,
    val categoryId: String? = null,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)