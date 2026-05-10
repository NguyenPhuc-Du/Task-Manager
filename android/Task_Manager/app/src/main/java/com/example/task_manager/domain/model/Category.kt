package com.example.task_manager.domain.model

data class Category(
    val id: String,
    val name: String,
    val color: String? = null,
    val userId: String,
    val createdAt: String
)