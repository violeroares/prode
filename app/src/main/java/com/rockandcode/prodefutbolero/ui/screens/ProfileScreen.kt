package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    controller: NavHostController,
) {
    val isDark = isSystemInDarkTheme()
    val user by viewModel.user.collectAsState()
    var displayedUser by remember { mutableStateOf(user) }
    val buttonColor = if (isDark) Color(0xFFF1FD72) else Color(0xFF4270F6)

    // Actualizar displayedUser solo si user no es null
    LaunchedEffect(user) {
        if (user != null) {
            displayedUser = user
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Mi perfil",
                onBack = { controller.popBackStack() },
                showBackButton = true,
            )
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = Color.Transparent,
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                AsyncImage(
                    model = displayedUser?.avatarUrl,
                    contentDescription = "Avatar",
                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = displayedUser?.firstName ?: "Desconocido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = displayedUser?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(24.dp))
            }

            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.Person,
                    onClick = {},
                    title = "Editar Perfil",
                    description = "Cambiar datos personales",
                    isDark = isDark,
                )
            }
            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.Info,
                    onClick = {},
                    title = "Instrucciones",
                    description = "Guía de juego, realizar predicciones y sistema de puntuación",
                    isDark = isDark,
                )
            }

            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.CheckCircleOutline,
                    onClick = {},
                    title = "Mis aciertos",
                    description = "Historial de aciertos",
                    isDark = isDark,
                )
            }

            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.Lock,
                    onClick = {},
                    title = "Cambiar contraseña",
                    description = "Cambiar la contraseña de inicio de sesión",
                    isDark = isDark,
                )
            }

            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                ) {
                    Button(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        shape = RoundedCornerShape(24.dp),
                        onClick = { viewModel.logout() },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                            ),
                    ) {
                        Text(
                            "Cerrar sesión",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isDark) Color.Black else Color.White,
                        )
                    }
                }
            }
        }
    }
}
// }

@Composable
fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
    val shadowSpot = shadowAmbient
    val iconColor = if (isDark) Color(0xFFF1FD72) else Color(0xFF4270F6)
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = shadowAmbient,
                    spotColor = shadowSpot,
                ),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically, // <-- Esto es clave
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                // modifier = Modifier.padding(top = 4.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    description,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 18.sp,
                        ),
                    color =
                        MaterialTheme.colorScheme.onSurface.copy
                            (alpha = 0.5f),
                )
            }

            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onClick)
                        .background(
                            if (isDark) Color(0xFF2E3134) else MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ir-a-$title",
                    tint = if (isDark) Color.White else Color.Black,
                )
            }
        }
    }
}
