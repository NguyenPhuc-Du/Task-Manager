package com.example.task_manager.domain.repository

import com.example.task_manager.domain.model.User

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String?): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()
    suspend fun getToken(): String?
    suspend fun isLoggedIn(): Boolean
}