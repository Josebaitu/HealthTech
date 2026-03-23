package com.example.healthtech.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.storage.ktx.storage


class ProfileViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage.reference

    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userRole by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var profileImageUrl by mutableStateOf<String?>(null)

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                userName = document.getString("nombre") ?: "Usuario"
                userEmail = auth.currentUser?.email ?: ""
                userRole = document.getString("role") ?: "paciente"
                isLoading = false
            }
    }

    fun updateUserData(newName: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true

        db.collection("users").document(uid)
            .update("nombre", newName)
            .addOnSuccessListener {
                userName = newName
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    fun uploadProfileImage(uri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true

        val fileRef = storage.child("profile_images/$uid.jpg")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    db.collection("users").document(uid)
                        .update("profileImage", downloadUrl.toString())
                        .addOnSuccessListener {
                            profileImageUrl = downloadUrl.toString()
                            isLoading = false
                        }
                }
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
}