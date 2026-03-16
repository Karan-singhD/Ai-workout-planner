package com.example.ai_workout_planner.database

import androidx.room.Entity
import androidx.room.PrimaryKey
// exercise details 
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: Int = 0,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val date: Long = System.currentTimeMillis()
)