package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.healthtech.data.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class BookAppointmentViewModel: ViewModel() {
    private val db = Firebase.firestore

    var doctorInfo by mutableStateOf<UserProfile?>(null)
    var selectedDate by mutableStateOf<Long?>(System.currentTimeMillis())
    var selectedTime by mutableStateOf<String?>(null)
    var isBooking by mutableStateOf(false)

    val availableTimes = listOf("09:00", "09:30", "10:00", "10:30", "11:00","11:30", "12:00","12:30", "16:00", "16:30", "17:00", "17:30", "18:00")

    fun loadDoctor(doctorId: String) {
        db.collection("users").document(doctorId).get()
            .addOnSuccessListener {
                doctorInfo = it.toObject(UserProfile::class.java)
            }
    }

    fun confirmBooking(patientId: String, onComplete: () -> Unit) {
        if (isBooking) return
        isBooking = true

        val doctor = doctorInfo ?: return

        val appointment = hashMapOf(
            "patientId" to patientId,
            "doctorId" to doctor.uuid,
            "doctorName" to doctor.nombre,
            "date" to (selectedDate ?: 0L),
            "time" to (selectedTime ?: ""),
            "status" to "confirmada",
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("appointments").add(appointment)
            .addOnSuccessListener {
                isBooking = false
                onComplete()
            }
            .addOnFailureListener { isBooking = false }
    }
}