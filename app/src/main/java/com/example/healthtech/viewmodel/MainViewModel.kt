package com.example.healthtech.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.healthtech.data.MedicalRecord
import com.example.healthtech.data.MockData
import com.example.healthtech.data.RegistroMedico
import com.example.healthtech.data.UserProfile

class MainViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var userProfile by mutableStateOf<UserProfile?>(null)
    var userName by mutableStateOf("")
    var userRole by mutableStateOf("")
    var recentActivityList by mutableStateOf(listOf<MedicalRecord>())
    var searchQuery by mutableStateOf("")

    val filteredActivityList: List<MedicalRecord>
        get() {
            return if (searchQuery.isEmpty()) {
                recentActivityList
            } else {
                recentActivityList.filter { record ->
                    record.title.contains(searchQuery, ignoreCase = true) ||
                    record.source.contains(searchQuery, ignoreCase = true)
                }
            }
        }

    fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document != null) {
                        userProfile = document.toObject(UserProfile::class.java)
                        userName = userProfile?.name ?: "Usuario"
                        userRole = userProfile?.role ?: "paciente"

                        if (userRole == "paciente") {
                            fetchPatientRecords(userId)
                        }
                    }
                }
                .addOnFailureListener {
                    userName = "Usuario"
                }
        }
    }

    private fun fetchPatientRecords(userId: String) {
        db.collection("medical_records")
            .whereEqualTo("patientId", userId)
            .get()
            .addOnSuccessListener { result ->
                val records = result.toObjects(MedicalRecord::class.java)
                recentActivityList = records.sortedByDescending { it.date }
            }
    }

    fun openDocument(context: Context, url: String) {
        if (url.isNotEmpty()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al abrir el documento: ${e.message}")
            }
        }
    }

    fun addNewRecord(newRecord: MedicalRecord) {
        recentActivityList = listOf(newRecord) + recentActivityList

    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}