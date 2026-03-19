package com.example.healthtech.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthtech.data.MedicalRecord
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddDocumentViewModel: ViewModel() {
    var reportTitle by mutableStateOf("")
    var doctorName by mutableStateOf("")
    var selectedUri by mutableStateOf<Uri?>(null)
    var isUploading by mutableStateOf(false)
        private set

    fun onTitleChange(newName: String) { reportTitle = newName }
    fun onDoctorChange(newDoctor: String) { doctorName = newDoctor }
    fun onFileSelected(uri: Uri?) { selectedUri = uri }
    fun isFormValid(): Boolean {
        return reportTitle.isNotBlank() && doctorName.isNotBlank() && selectedUri != null
    }

    fun saveDocument(onSuccess: (MedicalRecord) -> Unit) {
        if (isFormValid()) {
            isUploading = true
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
            val formattedDate = formatter.format(calendar.time)

            val newRecord = MedicalRecord(
                id = UUID.randomUUID().toString(),
                title = reportTitle,
                date = formattedDate,
                source = "Dr. $doctorName",
                fileUri = selectedUri
            )

            viewModelScope.launch {
                delay(1000)
                isUploading = false
                onSuccess(newRecord)
            }
        }
    }
}