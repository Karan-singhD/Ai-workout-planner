package com.example.ai_workout_planner.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_workout_planner.data.WorkoutRepository
import com.example.ai_workout_planner.database.SessionEntity
import com.example.ai_workout_planner.database.WorkoutDatabase
import com.example.ai_workout_planner.database.WorkoutEntity
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WorkoutRepository(
        WorkoutDatabase.getDatabase(application).workoutDao(),
        WorkoutDatabase.getDatabase(application).sessionDao()
    )

    var workouts by mutableStateOf<List<WorkoutEntity>>(emptyList())
        private set

    var sessions by mutableStateOf<List<SessionEntity>>(emptyList())
        private set

    var selectedSession by mutableStateOf<SessionEntity?>(null)
        private set

    var sessionWorkouts by mutableStateOf<List<WorkoutEntity>>(emptyList())
        private set

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            sessions = repository.getSessions()
        }
    }

    fun selectSession(session: SessionEntity) {
        selectedSession = session
        viewModelScope.launch {
            sessionWorkouts = repository.getWorkoutsForSession(session.id)
        }
    }

    fun clearSelectedSession() {
        selectedSession = null
        sessionWorkouts = emptyList()
    }

    fun createSession(name: String) {
        viewModelScope.launch {
            repository.insertSession(SessionEntity(name = name))
            loadSessions()
        }
    }

    fun deleteSession(session: SessionEntity) {
        viewModelScope.launch {
            repository.deleteSession(session)
            if (selectedSession?.id == session.id) clearSelectedSession()
            loadSessions()
        }
    }

    fun addWorkout(exerciseName: String, sets: String, reps: String, weight: String) {
        val sessionId = selectedSession?.id ?: 0
        viewModelScope.launch {
            repository.insertWorkout(
                WorkoutEntity(
                    sessionId = sessionId,
                    exerciseName = exerciseName,
                    sets = sets.toIntOrNull() ?: 0,
                    reps = reps.toIntOrNull() ?: 0,
                    weight = weight.toFloatOrNull() ?: 0f
                )
            )
            selectedSession?.let {
                sessionWorkouts = repository.getWorkoutsForSession(it.id)
            }
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
            selectedSession?.let {
                sessionWorkouts = repository.getWorkoutsForSession(it.id)
            }
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.updateWorkout(workout)
            selectedSession?.let {
                sessionWorkouts = repository.getWorkoutsForSession(it.id)
            }
        }
    }
}