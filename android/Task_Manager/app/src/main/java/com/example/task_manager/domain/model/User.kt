package com.example.task_manager.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val token: String
)