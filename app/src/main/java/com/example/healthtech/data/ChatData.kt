package com.example.healthtech.data

import androidx.compose.runtime.snapshots.StateRecord

data class ChatData (
    val id: String,
    val participants: List<String>,
    val lastMessage: String,
    val lastTimestamp: Long,
    val doctorName: String,
    val patientName: String
)