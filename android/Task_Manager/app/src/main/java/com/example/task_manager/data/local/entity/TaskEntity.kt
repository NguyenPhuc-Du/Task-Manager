package com.example.task_manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    val completed: Boolean = false,
    val dueDate: String? = null,
    val categoryId: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)