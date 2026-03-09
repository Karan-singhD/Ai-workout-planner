package com.example.aiworkoutplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aiworkoutplanner.viewmodel.WorkoutViewModel

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
        // screen text and formatting
        Text("AI Workout Planner")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Exercise Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))
    // text format sets
        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Sets") }
        )

        Spacer(modifier = Modifier.height(8.dp))
    // test format for reps
        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Reps") }
        )

        Spacer(modifier = Modifier.height(8.dp))
    // text fromat for weight involved
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Weight (kg)") }
        )

        Spacer(modifier = Modifier.height(12.dp))
        // button to add exercise
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
    // list of exercises
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