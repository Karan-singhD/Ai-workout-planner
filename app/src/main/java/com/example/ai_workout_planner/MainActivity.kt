package com.example.ai_workout_planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai_workout_planner.ui.WorkoutScreen
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val workoutViewModel: WorkoutViewModel = viewModel()
            WorkoutScreen(viewModel = workoutViewModel)
        }
    }
}