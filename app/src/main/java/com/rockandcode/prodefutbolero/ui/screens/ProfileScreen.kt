package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    controller: NavHostController,
) {
    val user by viewModel.user.collectAsState()
    var displayedUser by remember { mutableStateOf(user) }

    // Actualizar displayedUser solo si user no es null
    LaunchedEffect(user) {
        if (user != null) {
            displayedUser = user
        }
    }

//    Box(
//        modifier =
//            Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        colors =
//                            listOf(
//                                MaterialTheme.colorScheme.primary.copy(alpha = 0.11f),
//                                MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
//                            ),
//                    ),
//                ),
//    ) {
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
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = displayedUser?.name ?: "Desconocido",
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
                )
            }
            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.Info,
                    onClick = {},
                    title = "Instrucciones",
                    description = "Instrucciones sobre partidos, realizar predicciones y sistema de puntuación",
                )
            }

            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.CheckCircleOutline,
                    onClick = {},
                    title = "Mis aciertos",
                    description = "Historial de aciertos",
                )
            }

            item {
                ProfileOptionCard(
                    icon = Icons.Outlined.Lock,
                    onClick = {},
                    title = "Cambiar contraseña",
                    description = "Cambiar la contraseña de inicio de sesión",
                )
            }

            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = { viewModel.logout() },
                    ) {
                        Text(
                            "Cerrar sesión",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
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
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top, // <-- Esto es clave
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    description,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }

            Icon(
                imageVector = Icons.Filled.ArrowCircleRight,
                contentDescription = "ir-a-$title",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier =
                    Modifier
                        .padding(top = 4.dp),
            )
        }
    }
}
