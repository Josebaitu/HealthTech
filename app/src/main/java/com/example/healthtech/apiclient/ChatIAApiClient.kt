package com.example.healthtech.apiclient

import android.util.Log
import androidx.compose.foundation.interaction.HoverInteraction
import com.example.healthtech.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

object ChatIAApiClient {
    private val API_KEY = BuildConfig.OPENAI_API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3-flash-preview",
        apiKey = API_KEY
    )

    suspend fun generateApiResponse(prompt: String, message: String): String? {

        return try {
            val response = generativeModel.generateContent(
                content {
                    text(prompt)
                    text(message)
                }
            )
            response.text
        } catch (e: Exception) {
            null
        }
    }
}