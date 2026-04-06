package com.example.healthtech.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.healthtech.data.MedicalRecord
import com.example.healthtech.data.UserProfile
import com.google.firebase.firestore.FieldValue

class MainViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var userProfile by mutableStateOf<UserProfile?>(null)
    var userName by mutableStateOf("")
    var userRole by mutableStateOf("")
    var recentActivityList by mutableStateOf(listOf<MedicalRecord>())
    var searchQuery by mutableStateOf("")
    var doctorSearchQuery by mutableStateOf("")
    var doctorList by mutableStateOf<List<UserProfile>>(emptyList())
    var allDoctorsList by mutableStateOf<List<UserProfile>>(emptyList())

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

    val displayDoctors: List<UserProfile>
        get() {
            return if (doctorSearchQuery.isEmpty()) {
                doctorList
            } else {
                allDoctorsList.filter {
                    it.nombre.contains(doctorSearchQuery, ignoreCase = true)
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
                        userName = userProfile?.nombre ?: "Usuario"
                        userRole = userProfile?.rol ?: "paciente"

                        if (userRole == "paciente") {
                            fetchPatientRecords(userId)
                            fetchDoctors()
                            fetchAllDoctors()
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

    fun fetchDoctors() {
        val myId = auth.currentUser?.uid ?: return

        db.collection("users").document(myId).get()
            .addOnSuccessListener { document ->
                val profile = document.toObject(UserProfile::class.java)
                val myDoctorIds = profile?.myDoctors?.filter { it.isNotEmpty() } ?: emptyList()

                if (myDoctorIds.isNotEmpty()) {
                    db.collection("users")
                        .whereIn("uuid", myDoctorIds).get()
                        .addOnSuccessListener { result ->
                            doctorList = result.toObjects(UserProfile::class.java)
                        }
                } else {
                    doctorList = emptyList()
                }
            }
    }

    fun fetchAllDoctors() {
        db.collection("users")
            .whereEqualTo("rol", "doctor").get()
            .addOnSuccessListener {
                allDoctorsList = it.toObjects(UserProfile::class.java)
            }
    }

    fun searchDoctors(query: String, onResult: (List<UserProfile>) -> Unit) {
        db.collection("users")
            .whereEqualTo("rol", "doctor").get()
            .addOnSuccessListener { result ->
                val allDoctors = result.toObjects(UserProfile::class.java)
                val filteredDoctors = allDoctors.filter {
                    it.nombre.contains(query, ignoreCase = true)
                }

                onResult(filteredDoctors)
            }
    }

    fun addDoctorToList(doctorId: String) {
        val myId = auth.currentUser?.uid ?: return
        if (doctorId.isEmpty()) return

        db.collection("users").document(myId)
            .update("myDoctors", FieldValue.arrayUnion(doctorId))
            .addOnSuccessListener {
                fetchDoctors()
                doctorSearchQuery = ""
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