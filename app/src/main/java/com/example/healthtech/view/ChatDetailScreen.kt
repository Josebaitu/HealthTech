package com.example.healthtech.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthtech.viewmodel.ChatDetailViewModel
import com.example.healthtech.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.healthtech.data.MessageData
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun ChatDetailScreen(navController: NavController, mainViewModel: MainViewModel, chatId: String, viewModel: ChatDetailViewModel = viewModel()) {
    val myId = mainViewModel.userProfile?.uuid ?: ""
    val isDoctor = mainViewModel.userRole == "doctor"
    val listState = rememberLazyListState()
    val chatName = viewModel.chatInfo?.let {
        if (isDoctor) it.patientName else "Dr. ${it.doctorName}"
    } ?: "Cargando..."

    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadChatData(chatId)
        viewModel.takeMessages(chatId)
    }

    LaunchedEffect(viewModel.messages.size) {
        if (viewModel.messages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CustomHealthTechTopBar(
                title = chatName,
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                state = listState,
                reverseLayout = false
            ) {
                items(viewModel.messages) { message ->
                    MessageBubble(message, isMine = message.senderId == myId)
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        CustomHealthTechTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            label = "Escribe un mensaje..."
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageText.isNotEmpty()) {
                                viewModel.sendMessage(chatId, myId, messageText )
                                messageText = ""
                            }
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageData, isMine: Boolean) {
    val alignment = if (isMine) Alignment.End else Alignment.Start
    val color = if (isMine) MaterialTheme.colorScheme.primary else Color.LightGray
    val textColor = if (isMine) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = color,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(vertical = 4.dp).widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor
            )
        }
    }
}