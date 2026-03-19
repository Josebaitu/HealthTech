package com.example.healthtech.view

import android.R
import android.R.attr.text
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthtech.viewmodel.AddDocumentViewModel
import com.example.healthtech.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDocumentScreen( navController: NavController, mainViewModel: MainViewModel = viewModel(), viewModel: AddDocumentViewModel = viewModel()) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onFileSelected(uri) }

    Scaffold(
        topBar = {
            CustomHealthTechTopBar(
                title = "HealthTech",
                showBackButton = true,
                showMenuButton = false,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = { CustomHealthTechBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Añadir documento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            CustomHealthTechTextField(
                value = viewModel.reportTitle,
                onValueChange = { viewModel.onTitleChange(it) },
                label = "Título del documento",
                isError = viewModel.reportTitle.isEmpty() && viewModel.isUploading,
                errorMessage = "El título es obligatorio"
            )

            CustomHealthTechTextField(
                value = viewModel.doctorName,
                onValueChange = { viewModel.onDoctorChange(it) },
                label = "Especialista / Doctor",
                isError = viewModel.doctorName.isEmpty() && viewModel.isUploading,
                errorMessage = "El nombre del especialista es necesario"
            )

            Button(
                onClick = { filePickerLauncher.launch("application/pdf") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                if (viewModel.selectedUri == null) {
                    Text("Seleccionar PDF")
                } else {
                    Text("PDF adjuntado")
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveDocument { newRecord ->
                        mainViewModel.addNewRecord(newRecord)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = viewModel.isFormValid() && !viewModel.isUploading
            ) {
                if (viewModel.isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Guardar historial")
                }
            }
        }
    }
}