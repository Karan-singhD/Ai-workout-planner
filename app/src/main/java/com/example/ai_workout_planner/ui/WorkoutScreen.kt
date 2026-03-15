package com.example.ai_workout_planner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ai_workout_planner.database.SessionEntity
import com.example.ai_workout_planner.database.WorkoutEntity
import com.example.ai_workout_planner.viewmodel.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {

    val selectedSession = viewModel.selectedSession

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedSession != null) selectedSession.name
                        else "AI Workout Planner",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (selectedSession != null) {
                        IconButton(onClick = { viewModel.clearSelectedSession() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (selectedSession == null) {
            SessionListScreen(viewModel = viewModel, paddingValues = padding)
        } else {
            ExerciseScreen(viewModel = viewModel, paddingValues = padding)
        }
    }
}

@Composable
fun SessionListScreen(viewModel: WorkoutViewModel, paddingValues: PaddingValues) {

    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ New Workout Session")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Sessions", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.sessions.isEmpty()) {
            Text("No sessions yet. Create one to get started!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.sessions) { session ->
                SessionCard(
                    session = session,
                    onClick = { viewModel.selectSession(session) },
                    onDelete = { viewModel.deleteSession(session) }
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateSessionDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name ->
                viewModel.createSession(name)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun SessionCard(
    session: SessionEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(session.name, style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text(dateFormat.format(Date(session.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun CreateSessionDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Session") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Session Name (e.g. Push Day)") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onCreate(name) }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ExerciseScreen(viewModel: WorkoutViewModel, paddingValues: PaddingValues) {

    var exerciseName by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<WorkoutEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Add Exercise", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text("Exercise Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sets,
                        onValueChange = { sets = it },
                        label = { Text("Sets") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = reps,
                        onValueChange = { reps = it },
                        label = { Text("Reps") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (exerciseName.isNotBlank()) {
                            viewModel.addWorkout(exerciseName, sets, reps, weight)
                            exerciseName = ""
                            sets = ""
                            reps = ""
                            weight = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Exercise")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Exercises", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.sessionWorkouts.isEmpty()) {
            Text("No exercises yet. Add one above!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.sessionWorkouts) { workout ->
                WorkoutCard(
                    workout = workout,
                    onDelete = { viewModel.deleteWorkout(workout) },
                    onEdit = {
                        selectedWorkout = workout
                        showEditDialog = true
                    }
                )
            }
        }
    }

    if (showEditDialog && selectedWorkout != null) {
        EditWorkoutDialog(
            workout = selectedWorkout!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { updated ->
                viewModel.updateWorkout(updated)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun WorkoutCard(
    workout: WorkoutEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(workout.exerciseName, style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Sets: ${workout.sets}  |  Reps: ${workout.reps}  |  ${workout.weight} kg",
                    style = MaterialTheme.typography.bodySmall)
                Text(dateFormat.format(Date(workout.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline)
            }
            TextButton(onClick = onEdit) { Text("Edit") }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun EditWorkoutDialog(
    workout: WorkoutEntity,
    onDismiss: () -> Unit,
    onConfirm: (WorkoutEntity) -> Unit
) {
    var exerciseName by remember { mutableStateOf(workout.exerciseName) }
    var sets by remember { mutableStateOf(workout.sets.toString()) }
    var reps by remember { mutableStateOf(workout.reps.toString()) }
    var weight by remember { mutableStateOf(workout.weight.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Workout") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = exerciseName,
                    onValueChange = { exerciseName = it }, label = { Text("Exercise") })
                OutlinedTextField(value = sets,
                    onValueChange = { sets = it }, label = { Text("Sets") })
                OutlinedTextField(value = reps,
                    onValueChange = { reps = it }, label = { Text("Reps") })
                OutlinedTextField(value = weight,
                    onValueChange = { weight = it }, label = { Text("Weight (kg)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(workout.copy(
                    exerciseName = exerciseName,
                    sets = sets.toIntOrNull() ?: workout.sets,
                    reps = reps.toIntOrNull() ?: workout.reps,
                    weight = weight.toFloatOrNull() ?: workout.weight
                ))
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}