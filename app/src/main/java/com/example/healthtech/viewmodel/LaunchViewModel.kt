package com.example.healthtech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtech.data.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaunchViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            delay(500)
            if (repository.isUserLoggedIn()) {
                _navigateTo.value = "mainView"
            } else {
                _navigateTo.value = "loginScreen"
            }
        }
    }
}