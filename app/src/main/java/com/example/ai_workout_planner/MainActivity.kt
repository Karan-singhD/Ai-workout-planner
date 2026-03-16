package com.example.ai_workout_planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.ai_workout_planner.ui.MainScreen
import com.example.ai_workout_planner.ui.theme.AiworkoutplannerTheme
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AiworkoutplannerTheme {
                MainScreen(viewModel)
            }
        }
    }
}