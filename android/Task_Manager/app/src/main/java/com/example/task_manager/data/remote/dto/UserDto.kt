package com.example.task_manager.data.remote.dto

data class UserDto(
    val id: String,
    val email: String,
    val name: String?
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val user: UserDto,
    val token: String
)