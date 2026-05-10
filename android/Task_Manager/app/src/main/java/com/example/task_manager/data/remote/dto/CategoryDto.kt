package com.example.task_manager.data.remote.dto

data class CategoryDto(
    val id: String,
    val name: String,
    val color: String?,
    val userId: String,
    val createdAt: String
)

data class CreateCategoryRequest(
    val name: String,
    val color: String? = null
)