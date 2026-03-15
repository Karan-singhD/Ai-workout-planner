package com.example.ai_workout_planner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM sessions ORDER BY date DESC")
    suspend fun getAllSessions(): List<SessionEntity>

    @Query("SELECT * FROM workouts WHERE sessionId = :sessionId")
    suspend fun getWorkoutsForSession(sessionId: Int): List<WorkoutEntity>

    @Query("DELETE FROM workouts WHERE sessionId = :sessionId")
    suspend fun deleteWorkoutsForSession(sessionId: Int)
}