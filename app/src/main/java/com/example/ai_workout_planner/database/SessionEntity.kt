package com.example.ai_workout_planner.database

import androidx.room.Entity
import androidx.room.PrimaryKey
// workout entity
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: Long = System.currentTimeMillis()
)