package com.example.healthtech.data

import kotlin.uuid.Uuid

data class UserProfile(
    val uuid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "paciente",
    val creationDate: Long = System.currentTimeMillis()
)