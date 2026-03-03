package com.example.healthtech.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.healthtech.R
import com.example.healthtech.ui.theme.HealthTechBlueTrack

@Composable
fun LaunchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_healthtech_logo),
            contentDescription = "HealthTech Logo",
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = HealthTechBlueTrack,
            modifier = Modifier
                .width(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}