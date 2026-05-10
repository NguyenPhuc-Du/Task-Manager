package com.example.task_manager.data.remote.api

import com.example.task_manager.data.remote.dto.CreateTaskRequest
import com.example.task_manager.data.remote.dto.TaskDto
import com.example.task_manager.data.remote.dto.UpdateTaskRequest
import retrofit2.http.*

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("tasks/{id}")
    suspend fun getTasksById(@Path("id") id: String): TaskDto

    @POST("tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body request: UpdateTaskRequest
    ): TaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)
}