package com.example.task_manager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoros")
data class PomodoroEntity(
    @PrimaryKey val id: String,
    val duration: Int,
    val completed: Boolean = false,
    val taskId: String,
    val userId: String,
    val createdAt: String
)