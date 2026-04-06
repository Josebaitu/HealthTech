package com.example.healthtech.view

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomHealthTechAlertDialog(
    show: Boolean,
    title: String,
    text: String,
    isError: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    confirmText: String = "Aceptar"
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss?.invoke() },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(confirmText, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = if (onDismiss != null) {
                {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                }
            } else null,
            title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
            text = { Text(text = text, style = MaterialTheme.typography.bodyMedium) },
            icon = {
                Icon(
                    imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}