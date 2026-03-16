package com.example.ai_workout_planner.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel

sealed class BottomNavItem(val label: String, val icon: ImageVector) {
    object Sessions : BottomNavItem("Sessions", Icons.Default.FitnessCenter)
    object AIGenerator : BottomNavItem("AI Generator", Icons.Default.AutoAwesome)
}

@Composable
fun MainScreen(viewModel: WorkoutViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(BottomNavItem.Sessions, BottomNavItem.AIGenerator)

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { _ ->
        when (selectedTab) {
            0 -> WorkoutScreen(viewModel)
            1 -> AIGeneratorScreen(viewModel)
        }
    }
}