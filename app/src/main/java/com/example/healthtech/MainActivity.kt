package com.example.healthtech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.healthtech.ui.theme.HealthTechTheme
import com.example.healthtech.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthTechTheme {
                AppNavigation()
            }
        }
    }
}