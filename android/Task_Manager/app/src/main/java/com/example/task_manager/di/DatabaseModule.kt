package com.example.task_manager.di

import android.content.Context
import androidx.room.Room
import com.example.task_manager.data.local.AppDatabase
import com.example.task_manager.data.local.dao.CategoryDao
import com.example.task_manager.data.local.dao.PomodoroDao
import com.example.task_manager.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aitaskmanager.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun providePomodoroDao(database: AppDatabase): PomodoroDao = database.pomodoroDao()
}