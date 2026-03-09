package com.example.ai_workout_planner.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ai_workout_planner.data.WorkoutEntry

class WorkoutViewModel : ViewModel() {


    val workoutEntries = mutableStateListOf<WorkoutEntry>()

    fun addWorkoutEntry(
        exerciseName: String,
        sets: String,
        reps: String,
        weight: String
    ) {
        workoutEntries.add(
            WorkoutEntry(
                exerciseName = exerciseName,
                sets = sets,
                reps = reps,
                weight = weight
            )
        )
    }


}
