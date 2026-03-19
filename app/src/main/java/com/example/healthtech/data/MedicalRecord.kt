package com.example.healthtech.data

import android.net.Uri
import com.google.firebase.firestore.Source

data class MedicalRecord(
    val id: String,
    val title: String,
    val date: String,
    val source: String,
    val fileUri: Uri? = null
)