package com.example.healthtech.navigation

object Routes {
    const val Launch = "LaunchScreen"
    const val Login = "LoginScreen"
    const val SignUp = "SignUpScreen"
    const val MainView = "MainView"
    const val ForgotPassword = "ForgotPassword"
    const val ChatIA = "ChatIA"
    const val ChatMed = "ChatMed"
    const val ChatDetail = "chat_detail/{chatId}"
    const val AddDocScreen = "AddDocScreen"
    const val SettingsScreen = "Settings"
    const val ProfileScreen = "Profile"
    const val BookAppointment = "book_appointment/{doctorId}"

    const val AppointmentsScreen = "AppointmentsScreen"
    fun chatDetailRoute(chatId: String) = "chat_detail/$chatId"
    fun bookAppointmentRoute(doctorId: String) = "book_appointment/$doctorId"
}