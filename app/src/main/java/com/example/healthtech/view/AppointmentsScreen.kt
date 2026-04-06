package com.example.healthtech.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthtech.data.AppointmentData
import com.example.healthtech.viewmodel.AppointmentsViewModel
import com.example.healthtech.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun AppointmentsScreen(navController: NavController, mainViewModel: MainViewModel, viewModel: AppointmentsViewModel = viewModel()) {
    val userId = mainViewModel.userProfile?.uuid ?: ""
    val isDoctor = mainViewModel.userRole == "doctor"
    var appointmentDelete by remember { mutableStateOf<AppointmentData?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.fetchAppointments(userId, isDoctor)
        }
    }

    CustomHealthTechAlertDialog(
        show = showDeleteDialog,
        title = "Anular cita",
        text = "¿Estás seguro que deseas anular la cita?",
        isError = true,
        confirmText = "Anular Cita",
        onConfirm = {
            appointmentDelete?.let { viewModel.deleteAppointment(it.id) }
            showDeleteDialog = false
        },
        onDismiss = { showDeleteDialog = false }
    )

    Scaffold(
        topBar = {
            CustomHealthTechTopBar(
                title = "Mis Citas",
                showBackButton = false
            )
        },
        bottomBar = { CustomHealthTechBottomBar(navController) }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewModel.appointments.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes citas programadas", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(viewModel.appointments) { appointment ->
                    AppointmentItem(
                        appointment,
                        isDoctor,
                        onDeleteClick = {
                            appointmentDelete = appointment
                            showDeleteDialog = true
                        }
                        )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AppointmentItem(appointment: AppointmentData, isDoctor: Boolean, onDeleteClick: () -> Unit) {
    val dateText = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(appointment.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(appointment.time, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }

            VerticalDivider(modifier = Modifier.height(40.dp).padding(horizontal = 12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isDoctor) "Paciente: ${appointment.patientName}" else "Dr.${appointment.doctorName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(text = dateText, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Badge(
                    containerColor = Color(0xFFE3F2FD),
                    contentColor = Color(0xFF1976D2),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(appointment.status.uppercase(), modifier = Modifier.padding(horizontal = 4.dp))
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar cita", tint = Color.Red)
            }
        }
    }
}