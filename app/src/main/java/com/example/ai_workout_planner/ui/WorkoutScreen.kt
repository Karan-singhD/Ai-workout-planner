package com.example.ai_workout_planner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {


    var exerciseName by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text("AI Workout Planner")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Exercise Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Sets") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Reps") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Weight (kg)") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.addWorkoutEntry(exerciseName, sets, reps, weight)
                exerciseName = ""
                sets = ""
                reps = ""
                weight = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Exercise")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Workout Entries")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.workoutEntries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Exercise: ${entry.exerciseName}")
                        Text("Sets: ${entry.sets}")
                        Text("Reps: ${entry.reps}")
                        Text("Weight: ${entry.weight} kg")
                    }
                }
            }
        }
    }


}
