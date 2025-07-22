package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChangePasswordScreen(
    mainViewModel: MainViewModel,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    navController: NavController,
) {
    val isDark = isSystemInDarkTheme()
    val user by mainViewModel.user.collectAsState()
    val buttonColor = if (isDark) Color(0xFFF1FD72) else Color(0xFF4270F6)
    val uiState = viewModel.uiState

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppHeader(title = "Cambiar contraseña", onBack = { navController.popBackStack() })

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                "Ingrese los datos solicitados para cambiar su contraseña",
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 18.sp,
                    ),
                color =
                    MaterialTheme.colorScheme.onSurface.copy
                        (alpha = 0.5f),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.currentPassword,
                onValueChange = { viewModel.onCurrentPasswordChange(it) },
                label = { Text("Contraseña actual") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.newPassword,
                onValueChange = { viewModel.onNewPasswordChange(it) },
                label = { Text("Nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            uiState.errorMessage?.let {
                Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            uiState.successMessage?.let {
                Text(it, color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 24.dp),
        ) {
            Button(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                    ),
                onClick = { viewModel.changePassword(userId = user?.id.toString()) },
                enabled = !uiState.isLoading,
            ) {
                if (uiState.isLoading) {
                    CircularWavyProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        "Cambiar contraseña",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDark) Color.Black else Color.White,
                    )
                }
            }
        }
    }
}
