package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.R
import com.rockandcode.prodefutbolero.utils.obtenerSaludo

@Composable
fun HeaderWelcome(
    userName: String,
    userImage: String, // Imagen del usuario
    onNotificationClick: () -> Unit,
    isDark: Boolean,
) {
    val iconBackgroundColor = if (isDark) Color(0xFF27292C) else Color.White
    val saludo = remember { obtenerSaludo() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            AsyncImage(
                model = userImage,
                contentDescription = "User Avatar",
                modifier = Modifier.size(48.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Texto de bienvenida
            Column {
                Text(
                    text = saludo,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
        }

        // Icono de notificaciones
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onNotificationClick)
                    .background(
                        iconBackgroundColor,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                // Icons.Outlined.Notifications,
                painterResource(id = R.drawable.notifications_24dp_outlined),
                contentDescription = "Icono",
                tint = if (isDark) Color.White else Color.Black,
            )
        }
    }
}
