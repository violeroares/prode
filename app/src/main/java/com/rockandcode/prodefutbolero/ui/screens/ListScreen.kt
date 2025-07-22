package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen() {
    val items =
        listOf(
            "Maria Fernanda" to "Ok, let me check this out for a moment, thank you for your patience",
            "Mike James" to "Trying to connect my account to a new device and could use a hand ...",
            "David Romano" to "Everything was working great until today—now the screen won’t loading",
            "Shay Levy" to "Looking to update my settings but not sure where to find the right option in the app.",
            "Greg McDonald" to "Loving the platform so far, just need help sorting out one small technical thing.",
            "Maria Fernanda" to "Ok, let me check this out for a moment, thank you for your patience",
            "Mike James" to "Trying to connect my account to a new device and could use a hand ...",
            "David Romano" to "Everything was working great until today—now the screen won’t loading",
            "Shay Levy" to "Looking to update my settings but not sure where to find the right option in the app.",
            "Greg McDonald" to "Loving the platform so far, just need help sorting out one small technical thing.",
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Notificaciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "(3)",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF3AB3FF)),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0E1621),
                        titleContentColor = Color.White,
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFF3AB3FF),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
            }
        },
        bottomBar = { InboxBottomBar() },
        containerColor = Color(0xFF0E1621),
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(items) { (name, message) ->
                InboxItem(name = name, message = message, time = "11:32 AM", badgeCount = 3)
            }
        }
    }
}

@Composable
fun InboxItem(
    name: String,
    message: String,
    time: String,
    badgeCount: Int,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar con iniciales
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5E9EFF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = name.split(" ").map { it.first() }.joinToString(""),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                // Estado (punto verde)
                Box(
                    Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                        .background(Color.Green, CircleShape),
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    message,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(time, color = Color.Gray, fontSize = 12.sp)
                if (badgeCount > 0) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 4.dp)
                                .size(24.dp) // tamaño fijo
                                .background(Color(0xFF3AB3FF), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "$badgeCount",
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        // Divider que empieza donde empieza el texto (después del avatar + espacio)
        HorizontalDivider(
            modifier =
                Modifier
                    .padding(start = 60.dp) // 42 avatar + 12 spacer
                    .padding(top = 8.dp),
            thickness = 1.dp,
            color = Color(0xFF1E2B3A),
        )
    }
}

@Composable
fun InboxBottomBar() {
    NavigationBar(containerColor = Color(0xFF0E1621)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                BadgedBox(badge = { Badge { Text("21") } }) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chats")
                }
            },
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Folder, contentDescription = "Carpetas") },
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Explore, contentDescription = "Explorar") },
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
        )
    }
}
