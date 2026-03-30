package com.example.healthtech.viewmodel

import androidx.compose.animation.core.snap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.healthtech.data.ChatData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.healthtech.data.UserProfile
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User

class ChatMedViewModel: ViewModel() {
    private val db = Firebase.firestore

    var chatList by mutableStateOf<List<ChatData>>(emptyList())
    var foundUsers by mutableStateOf<List<UserProfile>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isSearching by mutableStateOf(false)

    fun fetchChats(id: String) {
        if (id.isEmpty()) return
        isLoading = true

        db.collection("chats")
            .whereArrayContains("participants", id)
            .orderBy("lastTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                chatList = snapshot?.toObjects(ChatData::class.java) ?: emptyList()
                isLoading = false
            }
    }

    fun searchUsers(query: String, roleToSearch: String) {
        if (query.length < 2) {
            foundUsers = emptyList()
            return
        }

        isSearching = true
        db.collection("users")
            .whereEqualTo("rol", roleToSearch)
            .whereGreaterThanOrEqualTo("nombre", query)
            .whereLessThanOrEqualTo("nombre", query + "\uf8ff")
            .get()
            .addOnSuccessListener { snapshot ->
                foundUsers = snapshot.toObjects(UserProfile::class.java)
                isSearching = false
            }
    }

    fun startChat(currentUser: UserProfile, targetUser: UserProfile, onComplete: (String) -> Unit) {
        val participants = listOf(currentUser.uuid, targetUser.uuid).sorted()
        val chatId = "${participants[0]}_${participants[1]}"

        val newChat = ChatData(
            id = chatId,
            participants = participants,
            doctorName = if (currentUser.rol == "doctor") currentUser.nombre else targetUser.nombre,
            patientName = if (currentUser.rol == "paciente") currentUser.nombre else targetUser.nombre,
            lastMessage = "Nuevo chat iniciado",
            lastTimestamp = System.currentTimeMillis()
        )

        db.collection("chats").document(chatId)
            .set(newChat, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(chatId)
            }
    }
}