package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.healthtech.data.MockData
import com.example.healthtech.data.RegistroMedico

class MainViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    var userName by mutableStateOf("")
    var recentActivityList by mutableStateOf(listOf<RegistroMedico>())

    fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document != null) {
                        userName = document.getString("nombre") ?: "Usuario"
                    }
                }
                .addOnFailureListener {
                    userName = "Usuario"
                }
        }
    }

    fun fetchUserActivityData() {
        recentActivityList = MockData.getInventedInfo()
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}