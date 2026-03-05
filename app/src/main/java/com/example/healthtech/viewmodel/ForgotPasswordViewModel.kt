package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ForgotPasswordViewModel: ViewModel() {
    private  val auth = Firebase.auth

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var isSuccess by mutableStateOf(false)

    fun resetPassword(email: String, onEmailSent: () -> Unit) {
        if (email.isBlank()) {
            errorMessage = "Introduce tu correo para enviarte el enlace"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "El formato del correo no es válido"
            return
        }

        isLoading = true
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                isLoading = true
                if (task.isSuccessful) {
                    isSuccess = true
                    isLoading = false
                    onEmailSent()
                } else {
                    isLoading = false
                    errorMessage = "Error: ${task.exception?.localizedMessage}"
                }
            }
    }
}