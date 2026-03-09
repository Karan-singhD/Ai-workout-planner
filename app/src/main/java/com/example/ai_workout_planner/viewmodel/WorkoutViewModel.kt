package com.example.aiworkoutplanner.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.aiworkoutplanner.data.WorkoutEntry
// stores workout entries
class WorkoutViewModel : ViewModel() {

    val workoutEntries = mutableStateListOf<WorkoutEntry>()

    fun addWorkoutEntry(
        exerciseName: String,
        sets: String,
        reps: String,
        weight: String
    ) {
        if (exerciseName.isNotBlank() && sets.isNotBlank() && reps.isNotBlank() && weight.isNotBlank()) {
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
}