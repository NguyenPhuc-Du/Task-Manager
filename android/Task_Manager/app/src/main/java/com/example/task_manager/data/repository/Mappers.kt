package com.example.task_manager.data.repository

import com.example.task_manager.data.local.entity.CategoryEntity
import com.example.task_manager.data.local.entity.TaskEntity
import com.example.task_manager.data.remote.dto.TaskDto
import com.example.task_manager.data.remote.dto.CategoryDto
import com.example.task_manager.domain.model.Category
import com.example.task_manager.domain.model.Task

fun TaskDto.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority,
    completed = completed,
    dueDate = dueDate,
    categoryId = categoryId,
    userId = userId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TaskEntity.toDomain() = Task(
    id = id,
    title = title,
    description = description,
    priority = priority,
    completed = completed,
    dueDate = dueDate,
    categoryId = categoryId,
    userId = userId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    name = name,
    color = color,
    userId = userId,
    createdAt = createdAt
)

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    color = color,
    userId = userId,
    createdAt = createdAt
)