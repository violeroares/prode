package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.rockandcode.prodefutbolero.domain.user.models.User
import com.rockandcode.prodefutbolero.utils.obtenerSaludo

@Composable
fun HomeHeader(
    user: User?,
    onNotificationsClick: () -> Unit = {},
    isDark: Boolean,
) {
    val iconBackgroundColor = if (isDark) Color(0xFF27292C) else Color.White
    val saludo = remember { obtenerSaludo() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "Hola ${user?.firstName},",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF747D8B),
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = saludo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                color = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onNotificationsClick)
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

            AsyncImage(
                model = user?.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier.size(48.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
