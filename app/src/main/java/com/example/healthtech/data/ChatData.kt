package com.example.healthtech.data

import androidx.compose.runtime.snapshots.StateRecord

data class ChatData (
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
    val doctorName: String = "",
    val patientName: String = ""
)