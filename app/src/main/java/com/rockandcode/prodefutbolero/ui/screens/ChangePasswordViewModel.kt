package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rockandcode.prodefutbolero.domain.user.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class ChangePasswordViewModel
    @Inject
    constructor(
        private val userRepository: IUserRepository,
    ) : ViewModel() {
        var uiState by mutableStateOf(ChangePasswordUiState())
            private set

        fun onCurrentPasswordChange(value: String) {
            uiState = uiState.copy(currentPassword = value)
        }

        fun onNewPasswordChange(value: String) {
            uiState = uiState.copy(newPassword = value)
        }

        fun onConfirmPasswordChange(value: String) {
            uiState = uiState.copy(confirmPassword = value)
        }

        fun changePassword(
            userId: String,
            // onPasswordChanged: () -> Unit,
        ) {
            if (uiState.newPassword != uiState.confirmPassword) {
                uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden.")
                return
            }

            if (uiState.newPassword.length < 6) {
                uiState = uiState.copy(errorMessage = "La nueva contraseña debe tener al menos 6 caracteres.")
                return
            }

            viewModelScope.launch {
                uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)

                val success =
                    try {
                        userRepository.changePassword(
                            userId,
                            uiState.currentPassword,
                            uiState.newPassword,
                        )
                    } catch (e: Exception) {
                        false
                    }

                uiState =
                    if (success) {
                        uiState.copy(
                            isLoading = false,
                            successMessage = "Contraseña cambiada con éxito.",
                        )
                    } else {
                        uiState.copy(
                            isLoading = false,
                            errorMessage = "Error al cambiar la contraseña. Verifica la contraseña actual.",
                        )
                    }

                // if (success) onPasswordChanged()
            }
        }
    }
