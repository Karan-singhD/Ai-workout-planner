package com.example.ai_workout_planner.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_workout_planner.database.WorkoutDatabase
import com.example.ai_workout_planner.database.WorkoutEntity
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutDao =
        WorkoutDatabase.getDatabase(application).workoutDao()

    val workoutEntries = mutableStateListOf<WorkoutEntity>()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            val entries = workoutDao.getAllWorkouts()
            workoutEntries.clear()
            workoutEntries.addAll(entries)
        }
    }

    fun addWorkoutEntry(
        exerciseName: String,
        sets: String,
        reps: String,
        weight: String
    ) {
        viewModelScope.launch {
            val workout = WorkoutEntity(
                exerciseName = exerciseName,
                sets = sets,
                reps = reps,
                weight = weight
            )
            workoutDao.insertWorkout(workout)
            loadWorkouts()
        }
    }
}