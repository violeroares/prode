package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.HomeHeader
import com.rockandcode.prodefutbolero.ui.navigation.Routes

data class StatItem(
    val icon: ImageVector,
    val title: String,
    val value: String,
)

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    val tournament by viewModel.selectedTournament.collectAsState()
    val user by viewModel.user.collectAsState()

    var displayedUser by remember { mutableStateOf(user) }

    val stats =
        listOf(
            StatItem(Icons.Default.SportsSoccer, "Partidos", "14"),
            StatItem(Icons.Default.Sports, "Locales", "8 victorias"),
            StatItem(Icons.Default.Sports, "Visitantes", "6 victorias"),
        )

    // Actualizar displayedUser solo si user no es null
    LaunchedEffect(user) {
        if (user != null) {
            displayedUser = user
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = Color.Transparent,
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                HomeHeader(
                    user = displayedUser,
                    onSearchClick = {
                        navController.navigate(Routes.TournamentSelect.route) {
                            popUpTo(Routes.Home.route) { inclusive = true }
                        }
                    },
                )
            }

            item {
                TournamentStatsCard(
                    tournamentName = tournament?.name ?: "Ninguno",
                    currentDate = "Resumen Fecha 1",
                    stats = stats,
                )
            }

            item {
                TodayMatchesCard()
            }

            item {
                RankingCard()
            }

            item {
                CalendarCard()
            }
        }
    }
}

@Composable
fun TournamentStatsCard(
    tournamentName: String,
    currentDate: String,
    stats: List<StatItem>,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Torneo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { /* abrir notificaciones */ },
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(horizontal = 16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = tournamentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = currentDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    stats.forEachIndexed { index, stat ->
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            StatRowItem(
                                icon = stat.icon,
                                title = stat.title,
                                value = stat.value,
                            )
                        }

                        if (index < stats.lastIndex) {
                            HorizontalDivider(
                                modifier =
                                    Modifier
                                        .fillMaxHeight()
                                        .width(1.dp),
                                thickness = 50.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatRowItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier =
                Modifier
                    .size(24.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun TodayMatchesCard() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Partidos de hoy",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Ver todos",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun RankingCard() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Mi ranking",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Ver ranking",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun CalendarCard() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Calendario",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Ver todos",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
