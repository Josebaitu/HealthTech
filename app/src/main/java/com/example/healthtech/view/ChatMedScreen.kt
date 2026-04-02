package com.example.healthtech.view

import android.R
import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthtech.data.ChatData
import com.example.healthtech.navigation.Routes
import com.example.healthtech.viewmodel.ChatMedViewModel
import com.example.healthtech.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ModifierInfo
import com.example.healthtech.data.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ChatMedScreen(navController: NavController, mainViewModel: MainViewModel = viewModel(), viewModel: ChatMedViewModel = viewModel()) {

    val currentUserId = Firebase.auth.currentUser?.uid ?: ""
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(currentUserId.isNotEmpty()) {
        if (currentUserId.isNotEmpty()) {
            mainViewModel.fetchUserData()
            viewModel.fetchChats(currentUserId)
        }
    }

    Scaffold(
        topBar = {
            CustomHealthTechTopBar(
                title = if (isSearching) "Buscar Contacto" else "Mis Chats",
                showBackButton = isSearching,
                onBackClick = { isSearching = false}
            )
        },
        bottomBar = { CustomHealthTechBottomBar(navController) },
        floatingActionButton = {
            if (!isSearching) {
                FloatingActionButton(
                    onClick = { isSearching = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Chat")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            if (isSearching) {
                CustomHealthTechTextField(
                    value = searchQuery,
                    onValueChange = { it ->
                        val capitalizedQuery = it.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString()
                        }
                        searchQuery = capitalizedQuery
                        val roleToSearch = if (mainViewModel.userRole == "doctor") "paciente" else "doctor"
                        viewModel.searchUsers(capitalizedQuery, roleToSearch)
                    },
                    label = "Nombre del contacto"
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.foundUsers) { user ->
                        UserSearchItem(user) {
                            val myProfile = mainViewModel.userProfile

                            if (myProfile != null && myProfile.uuid.isNotEmpty()) {
                                viewModel.startChat(myProfile, user) { chatId ->
                                    navController.navigate(Routes.chatDetailRoute(chatId))
                                }
                            }
                        }
                    }
                }
            } else {
                if (viewModel.chatList.isEmpty() && !viewModel.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes conversaciones aún", color = Color.Gray)
                    }
                } else {
                    LazyColumn {
                        items(viewModel.chatList) { chat ->
                            ChatItem(
                                chat = chat,
                                isDoctor = mainViewModel.userRole == "doctor",
                                onClick = {
                                    navController.navigate(Routes.chatDetailRoute(chat.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatData,
    isDoctor: Boolean,
    onClick: () -> Unit
) {
    val title = if (isDoctor) chat.patientName else "Dr ${chat.doctorName}"
    val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    val timeString = timeFormat.format(java.util.Date(chat.lastTimestamp))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = title.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.lastMessage.ifEmpty { "No hay mensajes aún" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(start = 88.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun UserSearchItem(user: UserProfile, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = user.nombre.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = user.nombre,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}