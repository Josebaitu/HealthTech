package com.example.healthtech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthtech.ui.theme.HealthTechTheme
import com.example.healthtech.view.LaunchScreen
import com.example.healthtech.viewmodel.LaunchViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LaunchViewModel by viewModels()

        setContent {
            HealthTechTheme {
                val isLoading by viewModel.isLoading.collectAsState()

                if (isLoading) {
                    LaunchScreen()
                } else {
                    // Pantalla Login
                }
            }
        }
    }
}