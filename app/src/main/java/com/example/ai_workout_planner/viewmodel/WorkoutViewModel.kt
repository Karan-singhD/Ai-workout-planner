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
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import com.example.ai_workout_planner.BuildConfig
class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WorkoutRepository(
        WorkoutDatabase.getDatabase(application).workoutDao(),
        WorkoutDatabase.getDatabase(application).sessionDao()
    )

    // ── Session / workout state ──────────────────────────────────────────────
    var workouts by mutableStateOf<List<WorkoutEntity>>(emptyList())
        private set
    var sessions by mutableStateOf<List<SessionEntity>>(emptyList())
        private set
    var selectedSession by mutableStateOf<SessionEntity?>(null)
        private set
    var sessionWorkouts by mutableStateOf<List<WorkoutEntity>>(emptyList())
        private set

    // ── AI state ─────────────────────────────────────────────────────────────
    var generatedPlan by mutableStateOf("")
        private set
    var isGenerating by mutableStateOf(false)
        private set
    var generationError by mutableStateOf("")
        private set

    private var lastGoal = ""
    private var lastExperience = ""
    private var lastDays = ""

    private val gemini = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    init { loadSessions() }

    // ── Sessions ─────────────────────────────────────────────────────────────
    fun loadSessions() {
        viewModelScope.launch { sessions = repository.getSessions() }
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

    // ── Workouts ─────────────────────────────────────────────────────────────
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

    // ── AI Generator ─────────────────────────────────────────────────────────
    fun generateWorkoutPlan(goal: String, experience: String, daysPerWeek: String) {
        lastGoal = goal
        lastExperience = experience
        lastDays = daysPerWeek

        isGenerating = true
        generatedPlan = ""
        generationError = ""

        val prompt = """
            Create a detailed $daysPerWeek-day per week workout plan for a $experience level person.
            Their goal is: $goal.
            
            Format the response exactly like this:
            
            DAY 1: [Session Name e.g. Push Day]
            - Exercise Name | Sets: X | Reps: X | Weight: Xkg
            - Exercise Name | Sets: X | Reps: X | Weight: Xkg
            
            DAY 2: [Session Name]
            - Exercise Name | Sets: X | Reps: X | Weight: Xkg
            
            Keep it practical. Include rest days if needed. 
            Always use the exact format above for each exercise line.
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = gemini.generateContent(prompt)
                generatedPlan = response.text ?: "No plan generated."
            } catch (e: Exception) {
                generationError = "Error: ${e.message}"
            } finally {
                isGenerating = false
            }
        }
    }

    fun savePlanAsSessions() {
        if (generatedPlan.isBlank()) return

        viewModelScope.launch {
            val lines = generatedPlan.lines()
            var currentSessionId = 0

            for (line in lines) {
                val trimmed = line.trim()

                // Detect day headers e.g. "DAY 1: Push Day"
                if (trimmed.startsWith("DAY", ignoreCase = true) && trimmed.contains(":")) {
                    val sessionName = trimmed.substringAfter(":").trim()
                    currentSessionId = repository.insertSession(
                        SessionEntity(name = sessionName)
                    ).toInt()
                }

                // Detect exercise lines e.g. "- Bench Press | Sets: 3 | Reps: 10 | Weight: 60kg"
                if (trimmed.startsWith("-") && trimmed.contains("|") && currentSessionId != 0) {
                    try {
                        val content = trimmed.removePrefix("-").trim()
                        val parts = content.split("|")
                        val exerciseName = parts[0].trim()
                        val sets = parts.getOrNull(1)
                            ?.replace("Sets:", "", ignoreCase = true)?.trim()?.toIntOrNull() ?: 3
                        val reps = parts.getOrNull(2)
                            ?.replace("Reps:", "", ignoreCase = true)?.trim()?.toIntOrNull() ?: 10
                        val weight = parts.getOrNull(3)
                            ?.replace("Weight:", "", ignoreCase = true)
                            ?.replace("kg", "", ignoreCase = true)?.trim()?.toFloatOrNull() ?: 0f

                        repository.insertWorkout(
                            WorkoutEntity(
                                sessionId = currentSessionId,
                                exerciseName = exerciseName,
                                sets = sets,
                                reps = reps,
                                weight = weight
                            )
                        )
                    } catch (e: Exception) {
                        // skip malformed lines
                    }
                }
            }
            loadSessions()
        }
    }
}