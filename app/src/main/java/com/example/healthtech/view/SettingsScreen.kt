package com.example.healthtech.view

import androidx.compose.material3.ListItem
import android.icu.text.CaseMap
import android.widget.Switch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthtech.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CustomHealthTechTopBar(
                title = "HealthTech",
                showBackButton = true,
                showMenuButton = false,
                onBackClick = {
                    navController.popBackStack()
                }

            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
        ) {
            Text(
                "Prefrencias del Sistema",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )

            SwitchSettingsItem("Notificaciones", Icons.Default.Notifications)
            SwitchSettingsItem("Modo Oscuro", Icons.Default.Brightness4)
        }
    }
}

@Composable
fun SwitchSettingsItem(title: String, icon: ImageVector) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(icon, null) },
        trailingContent = { Switch(checked = true, onCheckedChange = { /*TODO: Lógica del programa en settings*/ }) }
    )
}