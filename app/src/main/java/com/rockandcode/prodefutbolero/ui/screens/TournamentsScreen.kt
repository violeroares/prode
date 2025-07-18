package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.R
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun TournamentsScreen(
    viewModel: MainViewModel,
    tournamentsViewModel: TournamentsViewModel = hiltViewModel(),
    onTournamentSelected: () -> Unit,
    modifier: Modifier,
) {
    val uiState by tournamentsViewModel.uiState.collectAsState()

    when (uiState) {
        is TournamentsUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TournamentsUiState.Error -> {
            val message = (uiState as TournamentsUiState.Error).message
            Box(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Error: $message")
                    IconButton(onClick = { tournamentsViewModel.loadTournaments() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                        )
                    }
                }
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
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = paddingValues,
                ) {
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    item {
                        AppHeader(
                            title = "Torneos",
                            onBack = { },
                            showBackButton = false,
                            // subTitle = "Seleccione un torneo",
                        )
                    }
                    item {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_chico),
                                contentDescription = "LogoTorneo",
                                Modifier
                                    .size(140.dp)
                                    .padding(bottom = 24.dp),
                            )
                        }
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

@Composable
fun TournamentCard(
    pictureUrl: String,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(36.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            AsyncImage(
//                model = pictureUrl,
//                contentDescription = "image-$title",
//                modifier = Modifier.size(36.dp).clip(CircleShape),
//                contentScale = ContentScale.Crop,
//            )
            AsyncImage(
                model = pictureUrl,
                contentDescription = "image-$title",
                modifier =
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0xFF2E3134) else Color.White),
                // Opcional, para relleno si hay transparencia
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                )

                Text(
                    text = "Seleccionar este torneo",
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 18.sp,
                        ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }

            Box(
                modifier =
                    Modifier
                        .size(68.dp)
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
                    contentDescription = "Icono",
                    tint = if (isDark) Color.White else Color.Black,
                )
            }
        }
    }
}
