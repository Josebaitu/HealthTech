package com.example.healthtech.data

import kotlin.uuid.Uuid

data class UserProfile(
    val uuid: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: String = "paciente",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val myDoctors: List<String> = emptyList()
)