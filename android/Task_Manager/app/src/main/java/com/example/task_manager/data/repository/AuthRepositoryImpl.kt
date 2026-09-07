package com.example.task_manager.data.repository

import com.example.task_manager.data.datastore.UserPreferences
import com.example.task_manager.data.remote.api.AuthApiService
import com.example.task_manager.data.remote.dto.LoginRequest
import com.example.task_manager.data.remote.dto.RegisterRequest
import com.example.task_manager.domain.model.User
import com.example.task_manager.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun register(email: String, password: String, name: String?): User {
        val response = authApiService.register(RegisterRequest(email, password, name))
        userPreferences.saveUser(response.token, response.user.id, response.user.email, response.user.name)

        return User(
            id = response.user.id,
            email = response.user.email,
            name = response.user.name,
            token = response.token
        )
    }

    override suspend fun login(email: String, password: String): User {
        val response = authApiService.login(LoginRequest(email, password))
        userPreferences.saveUser(response.token, response.user.id, response.user.email, response.user.name)

        return User(
            id = response.user.id,
            email = response.user.email,
            name = response.user.name,
            token = response.token
        )
    }

    override suspend fun logout() {
        try {
            authApiService.logout()
        }
        catch (e: Exception) { }

        userPreferences.clearUser()
    }

    override suspend fun getToken(): String? {
        return userPreferences.token.firstOrNull()
    }

    override suspend fun isLoggedIn(): Boolean {
        return userPreferences.token.firstOrNull() != null
    }
}