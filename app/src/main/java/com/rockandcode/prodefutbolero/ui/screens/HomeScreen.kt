package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.ErrorView
import com.rockandcode.prodefutbolero.ui.components.HomeAverageByDateCard
import com.rockandcode.prodefutbolero.ui.components.HomeHeader
import com.rockandcode.prodefutbolero.ui.components.IncompleteCard
import com.rockandcode.prodefutbolero.ui.components.LoadingView
import com.rockandcode.prodefutbolero.ui.components.MoistureGaugeFull
import com.rockandcode.prodefutbolero.ui.components.TestCard
import com.rockandcode.prodefutbolero.ui.components.TournamentStatsCard
import com.rockandcode.prodefutbolero.ui.navigation.Routes

// data class StatItem(
//    val icon: ImageVector,
//    val title: String,
//    val value: String,
// )

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val tournament by viewModel.selectedTournament.collectAsState()
    val user by viewModel.user.collectAsState()
    val predictionSummary by viewModel.predictionSummary.collectAsState()
    var displayedUser by remember { mutableStateOf(user) }
    val state by homeViewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val incompletas by viewModel.prediccionesIncompletas.collectAsState()

    LaunchedEffect(tournament?.id) {
        tournament?.id?.let { homeViewModel.loadTournamentHome(it, user?.id) }
    }

    // Actualizar displayedUser solo si user no es null
    LaunchedEffect(user) {
        if (user != null) {
            displayedUser = user
        }
    }

    when (val uiState = state) {
        is HomeUiState.Loading -> {
            LoadingView()
        }

        is HomeUiState.Error -> {
            ErrorView(
                message = uiState.message,
                onRetry = {
                    tournament?.id?.let {
                        tournament?.id?.let {
                            homeViewModel.loadTournamentHome(
                                it,
                                user?.id,
                            )
                        }
                    }
                },
            )
        }

        is HomeUiState.Success -> {
            val myRanking = uiState.myRanking
            val averageByDate = uiState.averageByDate
            // val topRanking = uiState.topRanking

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.systemBars,
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
//                            onSearchClick = {
//                                navController.navigate(Routes.TournamentSelect.route) {
//                                    popUpTo(Routes.Home.route) { inclusive = true }
//                                }
//                            },
                            isDark = isDark,
                        )
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                    }

                    if (incompletas > 0) {
                        item {
                            IncompleteCard(value = incompletas, dateName = "Fecha 02")
                        }
                    }

                    item {
                        HomeAverageByDateCard(
                            title = tournament?.name ?: "",
                            averageList = averageByDate,
                            myPosition = myRanking?.posicion.toString(),
                            onMoreClick = {
                                navController.navigate(Routes.TournamentSelect.route) {
                                    popUpTo(Routes.Home.route) { inclusive = true }
                                }
                            },
                        )
                    }

                    item {
                        TournamentStatsCard(title = "Mi progreso", predictionSummary = predictionSummary, onMoreClick = {})
                    }
                    if (incompletas > 0) {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                MoistureGaugeFull()
                            }
                        }
                    }
//                    item {
//                        HomeCalendarCard(
//                            matches = uiState.data.matches,
//                            onMoreClick = { },
//                        )
//                    }
                    if (incompletas > 0) {
                        item {
                            TestCard(user = displayedUser)
                        }
                    }

//                    item {
//                        TournamentStatsCard(
//                            tournamentName = uiState.data.tournamentName,
//                            currentDate = uiState.data.activeDateName,
//                            stats =
//                                listOf(
//                                    StatItem(
//                                        Icons.Default.SportsSoccer,
//                                        "Partidos",
//                                        uiState.data.matches.size
//                                            .toString(),
//                                    ),
//                                    StatItem(Icons.Default.Sports, "Locales", "8 victorias"),
//                                    StatItem(Icons.Default.Sports, "Visitantes", "6 victorias"),
//                                ),
//                        )
//                    }

//                    val partidosDelDia =
//                        uiState.data.matches.filter {
//                            it.dateAsLocalDateTime().toLocalDate() == LocalDate.now()
//                        }
//
//                    if (partidosDelDia.isNotEmpty()) {
//                        item {
//                            TodayMatchesCard(matches = partidosDelDia)
//                        }
//                    }
//
//                    item {
//                        // RankingCard(ranking = uiState.data.ranking)
//                        RankingInfoSection(
//                            myPosition = uiState.data.myPosition,
//                            topUserName =
//                                uiState.data.leaderName,
//                            myPoints = uiState.data.myPoints,
//                        )
//                    }

//                    item {
//                        CalendarCard()
//                    }
//
//                    if (uiState.data.matches.isNotEmpty()) {
//                        item {
//                            TodayMatchesCard(matches = uiState.data.matches)
//                        }
//                    }

                    item {
                        Spacer(Modifier.height(96.dp))
                    }
                }
            }
        }
    }
}

// @Composable
// fun RankingInfoSection(
//    myPosition: String,
//    topUserName: String,
//    myPoints: String,
//    modifier: Modifier = Modifier,
// ) {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Ranking",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Ver ranking",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
//        Row(
//            modifier =
//                modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//        ) {
//            // Mi puesto
//            Card(
//                modifier =
//                    Modifier
//                        .weight(1f)
//                        .height(100.dp),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(4.dp),
//            ) {
//                Row(
//                    modifier =
//                        Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.EmojiEvents,
//                        contentDescription = "Ícono de ranking",
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.size(28.dp),
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    // Texto "Mi puesto: X" y "Puntos: Y"
//                    Column {
//                        Text(
//                            text = "Mi Puesto: $myPosition",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Medium,
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = "Puntos: $myPoints",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.SemiBold,
//                        )
//                    }
//                }
//            }
//
//            // Primer puesto
//            Card(
//                modifier =
//                    Modifier
//                        .weight(1f)
//                        .height(100.dp),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(4.dp),
//            ) {
//                Row(
//                    modifier =
//                        Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.EmojiEvents,
//                        contentDescription = "Ícono de ranking",
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.size(28.dp),
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    // Texto "Mi puesto: X" y "Puntos: Y"
//                    Column {
//                        Text(
//                            text = "Primer Puesto",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Medium,
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = topUserName,
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.SemiBold,
//                        )
//                    }
//                }
//            }
//        }
//    }
// }
//
// @Composable
// fun TournamentStatsCard(
//    tournamentName: String,
//    currentDate: String,
//    stats: List<StatItem>,
// ) {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Torneo",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            IconButton(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                onClick = { /* abrir notificaciones */ },
//            ) {
//                Icon(
//                    Icons.Outlined.Notifications,
//                    contentDescription = "Notificaciones",
//                )
//            }
//        }
//        Spacer(Modifier.height(8.dp))
//        Card(
//            shape = RoundedCornerShape(16.dp),
//            modifier =
//                Modifier
//                    .fillMaxWidth()
//                    .height(130.dp)
//                    .padding(horizontal = 16.dp),
//            colors =
//                CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                ),
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    Text(
//                        text = tournamentName,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Normal,
//                    )
//                    Text(
//                        text = currentDate,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.secondary,
//                        modifier = Modifier.padding(top = 8.dp),
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                ) {
//                    stats.forEachIndexed { index, stat ->
//                        Row(
//                            modifier = Modifier.weight(1f),
//                            verticalAlignment = Alignment.Top,
//                            horizontalArrangement = Arrangement.Center,
//                        ) {
//                            StatRowItem(
//                                icon = stat.icon,
//                                title = stat.title,
//                                value = stat.value,
//                            )
//                        }
//
//                        if (index < stats.lastIndex) {
//                            HorizontalDivider(
//                                modifier =
//                                    Modifier
//                                        .fillMaxHeight()
//                                        .width(1.dp),
//                                thickness = 50.dp,
//                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
// }

// @Composable
// fun StatRowItem(
//    icon: ImageVector,
//    title: String,
//    value: String,
//    modifier: Modifier = Modifier,
// ) {
//    Row(
//        modifier = modifier,
//        verticalAlignment = Alignment.Top,
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = title,
//            tint = MaterialTheme.colorScheme.primary,
//            modifier =
//                Modifier
//                    .size(24.dp),
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        Column {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.bodyLarge,
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = value.toString(),
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Normal,
//                color = MaterialTheme.colorScheme.secondary,
//            )
//        }
//    }
// }

// @Composable
// fun TodayMatchesCard(matches: List<Match>) {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Partidos de hoy",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Ver todos",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
//        LazyRow(
//            contentPadding = PaddingValues(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier.fillMaxSize(),
//        ) {
//            items(matches) { match ->
//                // HomeMatchCard(match)
//                // MatchScoreCard(match)
//                HomeMatchCard(match)
//            }
//        }
//    }
// }

// @Composable
// fun RankingCard(ranking: Ranking?) {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Mi ranking",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Ver ranking",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
//        if (ranking == null) {
//            Text("Sin ranking disponible", modifier = Modifier.padding(16.dp))
//        } else {
//            Text(
//                text = "Posición: ${ranking.position} - Puntos: ${ranking.points}",
//                modifier = Modifier.padding(16.dp),
//            )
//            Text(
//                text = "Líder: ${ranking.leaderName ?: "N/A"} - Puntos: ${ranking.leaderPoints}",
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
//    }
// }

// @Composable
// fun CalendarCard() {
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Calendario",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Ver todos",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
//    }
// }
