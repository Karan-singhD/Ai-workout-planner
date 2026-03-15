package com.example.ai_workout_planner.data

import com.example.ai_workout_planner.database.SessionDao
import com.example.ai_workout_planner.database.SessionEntity
import com.example.ai_workout_planner.database.WorkoutDao
import com.example.ai_workout_planner.database.WorkoutEntity

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val sessionDao: SessionDao
) {

    suspend fun getWorkouts(): List<WorkoutEntity> = workoutDao.getAllWorkouts()
    suspend fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)
    suspend fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)
    suspend fun deleteWorkout(workout: WorkoutEntity) = workoutDao.deleteWorkout(workout)

    suspend fun getSessions(): List<SessionEntity> = sessionDao.getAllSessions()
    suspend fun insertSession(session: SessionEntity): Long = sessionDao.insertSession(session)
    suspend fun deleteSession(session: SessionEntity) {
        sessionDao.deleteWorkoutsForSession(session.id)
        sessionDao.deleteSession(session)
    }
    suspend fun getWorkoutsForSession(sessionId: Int): List<WorkoutEntity> =
        sessionDao.getWorkoutsForSession(sessionId)
}