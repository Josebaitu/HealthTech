package com.example.healthtech.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.healthtech.data.MessageData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.healthtech.data.ChatData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class ChatDetailViewModel: ViewModel() {
    private val db = Firebase.firestore

    var messages by mutableStateOf<List<MessageData>>(emptyList())
    var chatInfo by mutableStateOf<ChatData?>(null)

    private var listenerRegistration: ListenerRegistration? = null

    fun takeMessages(chatId: String) {
        if (chatId.isEmpty()) return

        listenerRegistration?.remove()

        listenerRegistration = db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                if (snapshot != null) {
                    messages = snapshot.toObjects(MessageData::class.java)
                }
            }
    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        if (text.isBlank()) return

        val message = MessageData(
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        db.collection("chats").document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                db.collection("chats").document(chatId).update(
                    "lastMessage", text,
                    "lastTimestamp", System.currentTimeMillis()
                )
            }
    }

    fun loadChatData(chatId: String) {
        if (chatId.isEmpty()) return

        db.collection("chats").document(chatId).get()
            .addOnSuccessListener { snapshot ->
                chatInfo = snapshot.toObject(ChatData::class.java)
            }
            .addOnFailureListener { e ->
                println("Error al cargar datos del chat: ${e.message}")
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}