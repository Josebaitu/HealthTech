package com.example.healthtech.data

data class RegistroMedico(
    val id: String,
    val titulo: String,
    val fecha: String,
    val origen: String
)

object MockData {
    fun getInventedInfo(): List<RegistroMedico> {
        return listOf(
            RegistroMedico("1", "Analítica de Sangre", "15 Mar 2025", "Osakidetza"),
            RegistroMedico("2", "Informe Radiología", "15 Mar 2025", "Osakidetza"),
            RegistroMedico("3", "Informe Traumatología", "15 Mar 2025", "Osakidetza"),
            RegistroMedico("4", "Cita Oftalmología", "15 Mar 2025", "Osakidetza"),
            RegistroMedico("5", "Resonancia Magnética", "15 Mar 2025", "Osakidetza")
            )
    }
}