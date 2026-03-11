package com.example.healthtech.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthtech.navigation.Routes
import com.example.healthtech.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomHealthTechTextField(
            value = name,
            onValueChange = {
                name = it
                viewModel.errorMessage = ""
            },
            label = "Nombre",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomHealthTechTextField(
            value = surname,
            onValueChange = {
                surname = it
                viewModel.errorMessage = ""
            },
            label = "Apellidos",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomHealthTechTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                viewModel.errorMessage = ""
            },
            label = "Correo Electrónico",
            isError = emailError != null,
            errorMessage = emailError,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomHealthTechTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                viewModel.errorMessage = ""
            },
            label = "Contraseña",
            isPassword = true,
            isError = passwordError != null,
            errorMessage = passwordError,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomHealthTechTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
                viewModel.errorMessage = ""
            },
            label = "Confirmar Contraseña",
            isPassword = true,
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(
                text = viewModel.errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomHealthTechButton(
            text = "Registrarse",
            enabled = !viewModel.isLoading,
            onClick = {
                when {
                    email.isBlank() || password.isBlank() || name.isBlank() -> {
                        viewModel.errorMessage = "Por favor, rellena todos los campos"
                    }

                    !viewModel.isValidEmail(email) -> {
                        emailError = "Escribe un correo electrónico válido."
                    }

                    !viewModel.isValidPassword(password) -> {
                        passwordError = "Mín. 6 caracteres, una mayúscula, un número y un símbolo"
                    }

                    !viewModel.isValidPassword(confirmPassword) -> {
                        confirmPasswordError = "Mín. 6 caracteres, una mayúscula, un número y un símbolo"
                    }

                    password != confirmPassword -> {
                        viewModel.errorMessage = "Las contraseñas no coinciden."
                    }

                    else -> {
                        val completeName = "${name.trim()} ${surname.trim()}"
                        viewModel.registerUser(email, password, nombre = completeName) {
                            navController.navigate(Routes.MainView) {
                                popUpTo(Routes.SignUp) { inclusive = true }
                            }
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "¿Ya tienes una cuenta?", fontSize = 14.sp, color = Color.Gray)

            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text(
                    text = "Inicia sesión",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}