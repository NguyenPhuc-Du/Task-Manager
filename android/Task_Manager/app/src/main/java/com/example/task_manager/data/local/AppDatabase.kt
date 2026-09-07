package com.example.task_manager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task_manager.data.local.dao.CategoryDao
import com.example.task_manager.data.local.dao.PomodoroDao
import com.example.task_manager.data.local.dao.TaskDao
import com.example.task_manager.data.local.entity.CategoryEntity
import com.example.task_manager.data.local.entity.PomodoroEntity
import com.example.task_manager.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class, PomodoroEntity::class],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun pomodoroDao(): PomodoroDao
}