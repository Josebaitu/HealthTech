package com.example.healthtech.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.healthtech.data.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SignUpViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun registerUser(email: String, password: String, nombre: String, role: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = ""

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    if (userId != null) {
                        val nuevoUsuario = UserProfile(
                            uuid = userId,
                            nombre = nombre,
                            email = email,
                            rol = role
                        )

                        db.collection("users").document(userId)
                            .set(nuevoUsuario)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Perfil de usuario creado.")
                                isLoading = false
                                onSuccess()
                            }
                            .addOnFailureListener { error ->
                                isLoading = false
                                errorMessage = "Error al guardar el perfil: ${error.localizedMessage}"
                                Log.w("Firestore", "Error al crear perfil", error)
                            }
                    }
                } else {
                    isLoading = false
                    val exception = task.exception
                    val message = exception?.localizedMessage ?: "Ha ocurrido un error inesperado"

                    errorMessage = when (exception) {
                        is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "Esta cuenta ya existe"
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "El formato del correo no es válido."
                        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
                        else -> message
                    }
                }
            }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\\"?]).{6,}$"
        return password.matches(passwordPattern.toRegex())
    }
}