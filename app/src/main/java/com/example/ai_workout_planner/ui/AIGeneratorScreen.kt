package com.example.ai_workout_planner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIGeneratorScreen(viewModel: WorkoutViewModel) {

    var goal by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("Beginner") }
    var daysPerWeek by remember { mutableStateOf("3") }
    var expandedExperience by remember { mutableStateOf(false) }
    var expandedDays by remember { mutableStateOf(false) }

    val experienceOptions = listOf("Beginner", "Intermediate", "Advanced")
    val daysOptions = listOf("2", "3", "4", "5", "6")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "AI Workout Generator",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Describe your goal and get a full workout plan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "Your Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Fitness Goal") },
                    placeholder = { Text("e.g. Build muscle, lose weight, get stronger") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedExperience,
                    onExpandedChange = { expandedExperience = it }
                ) {
                    OutlinedTextField(
                        value = experience,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Experience Level") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expandedExperience)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedExperience,
                        onDismissRequest = { expandedExperience = false }
                    ) {
                        experienceOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    experience = option
                                    expandedExperience = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedDays,
                    onExpandedChange = { expandedDays = it }
                ) {
                    OutlinedTextField(
                        value = "$daysPerWeek days per week",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Days Per Week") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expandedDays)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDays,
                        onDismissRequest = { expandedDays = false }
                    ) {
                        daysOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text("$option days per week") },
                                onClick = {
                                    daysPerWeek = option
                                    expandedDays = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (goal.isNotBlank()) {
                            viewModel.generateWorkoutPlan(goal, experience, daysPerWeek)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.isGenerating
                ) {
                    if (viewModel.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (viewModel.isGenerating) "Generating..." else "Generate Workout Plan")
                }
            }
        }

        if (viewModel.generatedPlan.isNotBlank()) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Your Workout Plan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { viewModel.savePlanAsSessions() },
                            enabled = !viewModel.isGenerating
                        ) {
                            Text("Save to Sessions")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        viewModel.generatedPlan,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (viewModel.generationError.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    viewModel.generationError,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}