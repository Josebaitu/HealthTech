package com.example.healthtech.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthtech.data.MedicalRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddDocumentViewModel: ViewModel() {
    private val storage = Firebase.storage.reference
    private val db = Firebase.firestore
    private val auth = Firebase.auth
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

    fun saveDocument(patientName: String, onSuccess: (MedicalRecord) -> Unit) {
        if (isFormValid() && selectedUri != null) {
            isUploading = true
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
            val formattedDate = formatter.format(calendar.time)

            viewModelScope.launch {
                try {
                    val fileRef = storage.child("documents/${UUID.randomUUID()}.pdf")
                    fileRef.putFile(selectedUri!!).await()
                    val downloadUrl = fileRef.downloadUrl.await()

                    val newRecord = MedicalRecord(
                        id = UUID.randomUUID().toString(),
                        title = reportTitle,
                        date = formattedDate,
                        source = "Dr. $doctorName",
                        patientName = patientName,
                        fileUrl = downloadUrl.toString(),
                        patientId = Firebase.auth.currentUser?.uid ?: "",
                        doctorId = ""
                    )

                    db.collection("medical_records")
                        .document(newRecord.id)
                        .set(newRecord)
                        .await()

                    isUploading = false
                    onSuccess(newRecord)
                } catch (e: Exception) {
                    isUploading = false
                    e.printStackTrace()
                }
            }
        }
    }
}