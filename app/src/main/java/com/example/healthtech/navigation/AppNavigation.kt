package com.example.healthtech.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthtech.view.AddDocumentScreen
import com.example.healthtech.view.ChatIAScreen
import com.example.healthtech.view.ChatMedScreen
import com.example.healthtech.view.ForgotPasswordScreen
import com.example.healthtech.view.LaunchScreen
import com.example.healthtech.view.LoginScreen
import com.example.healthtech.view.MainScreen
import com.example.healthtech.view.ProfileScreen
import com.example.healthtech.view.SettingsScreen
import com.example.healthtech.view.SignUpScreen
import com.example.healthtech.viewmodel.AddDocumentViewModel
import com.example.healthtech.viewmodel.ForgotPasswordViewModel
import com.example.healthtech.viewmodel.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation() {
    //Va a ser lo que controla la navegaicon de la app
    val navController = rememberNavController()
    val currentUser = Firebase.auth.currentUser
    val startRoute = if (currentUser != null) Routes.MainView else Routes.Login //Uso operador ternario para tener el código en una sola linea

    //El NavHost tiene todas las rutas de las vistas de la aopp
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable(Routes.Launch) {
            LaunchScreen(navController = navController)
        }

        composable(Routes.Login) {
            LoginScreen(navController = navController)
        }

        composable(Routes.SignUp) {
            SignUpScreen(navController = navController)
        }

        composable(Routes.MainView) {
            MainScreen(navController = navController)
        }

        composable(Routes.ForgotPassword) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(Routes.ChatIA) {
            ChatIAScreen(navController = navController)
        }

        composable(Routes.ChatMed) {
            ChatMedScreen(navController = navController)
        }

        composable(Routes.AddDocScreen) {
            AddDocumentScreen(navController = navController)
        }

        composable(Routes.SettingsScreen) {
            SettingsScreen(navController = navController)
        }

        composable(Routes.ProfileScreen) {
            ProfileScreen(navController = navController)
        }
    }
}