package com.example.healthtech.viewmodel

import androidx.activity.SystemBarStyle
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthtech.apiclient.ChatIAApiClient
import com.example.healthtech.data.MessageData
import kotlinx.coroutines.launch

class ChatIAViewModel: ViewModel() {
    var messages = mutableStateListOf<MessageData>()
    var isTyping by mutableStateOf(false)

    private val prompt = """
        Te llamas MIA (Médico de Inteligencia Artificial).
        Tu objetivo es dar consejos de salud preventivos y generales.
        REGLA IMPORTANTE: Siempre debes aclarar que no reemplazar a un médico humano y que eres un prototipo.
        Si el usuario describe una emergencia (dolor de pecho fuerte, falta de aire, dolor de cabeza grave...) recomienda llamar inmediatamente a emergencias.
    """.trimIndent()

    fun sendMessage(text: String, userId: String) {
        if (text.isEmpty()) return

        messages.add(MessageData(
            text = text,
            senderId = userId,
            timestamp = System.currentTimeMillis()
        ))
        isTyping = true

        viewModelScope.launch {
            try {
                val response = ChatIAApiClient.generateApiResponse(prompt, text)
                val aiText = response
                    ?: "MIA tiene problemas y no puede contestar. Inténtalo de nuevo más tarde."

                messages.add(
                    MessageData(
                        text = aiText,
                        senderId = "MIA_SYSTEM",
                        timestamp = System.currentTimeMillis()
                    )
                )

            } catch (e: Exception) {
                val errorLog = e.message ?: ""
                val userMessageError = if (errorLog.contains("429") || errorLog.contains("quota")) {
                    "⚠️ MIA está recibiendo muchas consultas en este momento y no puede responderte de inmediato. " +
                    "Si te encuentras mal o tienes una urgencia, por favor no esperes: " +
                    "Llama a Emergencias o acude al hospital más cercano."
                } else {
                    "Lo siento, ha ocurrido un error inesperado. Si tu consulta es urgente, por favor contacta con un profesional sanitario."
                }

                messages.add(
                    MessageData(
                        text = userMessageError,
                        senderId = "MIA_SYSTEM",
                        timestamp = System.currentTimeMillis()
                    )
                )
            } finally {
                isTyping = false
            }
        }
    }
}