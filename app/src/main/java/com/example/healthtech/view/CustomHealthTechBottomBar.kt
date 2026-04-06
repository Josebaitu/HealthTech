package com.example.healthtech.view

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.healthtech.navigation.Routes

@Composable
fun CustomHealthTechBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.height(70.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = currentRoute == Routes.MainView,
            onClick = {
                if (currentRoute != Routes.MainView) {
                    navController.navigate(Routes.MainView) {
                        popUpTo(Routes.MainView) { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Event, contentDescription = null) },
            label = { Text("Citas") },
            selected = currentRoute == Routes.AppointmentsScreen,
            onClick = {
                if (currentRoute != Routes.AppointmentsScreen) {
                    navController.navigate(Routes.AppointmentsScreen)
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
            label = { Text("MIA") },
            selected = currentRoute == Routes.ChatIA,
            onClick = {
                if (currentRoute != Routes.ChatIA) {
                    navController.navigate(Routes.ChatIA)
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.ChatBubble, contentDescription = null) },
            label = { Text("Chats") },
            selected = currentRoute == Routes.ChatMed,
            onClick = {
                if (currentRoute != Routes.ChatMed) {
                    navController.navigate(Routes.ChatMed)
                }
            }
        )
    }
}