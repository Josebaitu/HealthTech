package com.example.healthtech.data

data class AppointmentData(
    val id: String = "",
    val patientId: String = "",
    val patientName: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val date: Long = 0L,
    val time: String = "",
    val status: String = "confirmada",
    val timestamp: Long = System.currentTimeMillis()
)