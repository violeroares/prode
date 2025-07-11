package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.user.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
    ) : ViewModel() {
        var isLoading = mutableStateOf(false)
            private set

        var loginError = mutableStateOf<String?>(null)
            private set

        fun login(
            email: String,
            password: String,
            onSuccess: () -> Unit,
        ) {
            isLoading.value = true
            loginError.value = null

            viewModelScope.launch {
                try {
                    loginUseCase(email, password)
                    isLoading.value = false
                    onSuccess()
                } catch (e: Exception) {
                    // onError(e.message ?: "Error desconocido")
                    isLoading.value = false
                    loginError.value = e.message
                }
            }
        }
    }
