package com.example.task_manager.data.local.dao

import androidx.room.*
import com.example.task_manager.data.local.entity.PomodoroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {
    @Query("SELECT * FROM pomodoros ORDER BY createdAt DESC")
    fun getAllPomodoros(): Flow<List<PomodoroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoro(pomodoro: PomodoroEntity)

    @Query("DELETE FROM pomodoros")
    suspend fun deleteAllPomodoros()
}