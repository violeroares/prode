package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun TournamentsScreen(
    viewModel: MainViewModel,
    tournamentsViewModel: TournamentsViewModel = hiltViewModel(),
    onTournamentSelected: () -> Unit,
    modifier: Modifier,
) {
    val uiState by tournamentsViewModel.uiState.collectAsState()

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
    when (uiState) {
        is TournamentsUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TournamentsUiState.Error -> {
            val message = (uiState as TournamentsUiState.Error).message
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Error: $message")
            }
        }

        is TournamentsUiState.Success -> {
            val tournaments = (uiState as TournamentsUiState.Success).tournaments

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0),
                containerColor = Color.Transparent,
            ) { paddingValues ->
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = paddingValues,
                ) {
                    item {
                        AppHeader(
                            title = "Torneos",
                            onBack = { },
                            showBackButton = false,
                        )
                    }
                    items(tournaments) { tournament ->
                        TournamentCard(
                            title = tournament.name,
                            onClick = {
                                viewModel.selectTournament(tournament)
                                onTournamentSelected()
                            },
                            pictureUrl = tournament.pictureUrl,
                        )
                    }
                }
            }
        }
    }
}
// }

@Composable
fun TournamentCard(
    pictureUrl: String,
    title: String,
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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = pictureUrl,
                contentDescription = "image-$title",
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Seleccionar este torneo",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ArrowCircleRight,
                contentDescription = "ir-a-$title",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
