package com.example.task_manager.data.remote.api

import com.example.task_manager.data.remote.dto.AuthResponse
import com.example.task_manager.data.remote.dto.LoginRequest
import com.example.task_manager.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout()
}