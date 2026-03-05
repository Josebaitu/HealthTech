package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.healthtech.navigation.Routes
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider

class SignUpViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun registerUser(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = ""

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
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