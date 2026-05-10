package com.example.task_manager.data.remote.api

import com.example.task_manager.data.remote.dto.CategoryDto
import com.example.task_manager.data.remote.dto.TaskDto
import retrofit2.http.GET

interface SyncApiService {
    @GET("tasks")
    suspend fun syncTasks(): List<TaskDto>

    @GET("categories")
    suspend fun syncCategories(): List<CategoryDto>
}