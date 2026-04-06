package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.healthtech.data.AppointmentData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.Query

class AppointmentsViewModel: ViewModel() {
    private val db = Firebase.firestore
    var appointments by mutableStateOf<List<AppointmentData>>(emptyList())
    var isLoading by mutableStateOf(false)

    fun fetchAppointments(userId: String, isDoctor: Boolean) {
        isLoading = true
        val filteredRole = if (isDoctor) "doctorId" else "patientId"

        db.collection("appointments")
            .whereEqualTo(filteredRole, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading = false
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.toObject(AppointmentData::class.java)
                    data?.copy(id = doc.id)
                } ?: emptyList()

                appointments = list.sortedBy { it.date }
                isLoading = false
            }
    }

    fun deleteAppointment(appointmentId: String) {
        if (appointmentId.isEmpty()) return

        db.collection("appointments").document(appointmentId)
            .delete()
    }
}