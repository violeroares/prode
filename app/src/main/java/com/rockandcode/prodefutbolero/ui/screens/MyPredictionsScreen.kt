package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.AppHeader
import com.rockandcode.prodefutbolero.ui.components.ErrorView
import com.rockandcode.prodefutbolero.ui.components.LoadingView
import com.rockandcode.prodefutbolero.ui.components.PredictionCard
import com.rockandcode.prodefutbolero.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPredictionsScreen(
    mainViewModel: MainViewModel,
    viewModel: MyPredictionsViewModel,
    navController: NavHostController,
) {
    val state by viewModel.screenState.collectAsState()
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val user by mainViewModel.user.collectAsState()
    val dates by mainViewModel.dates.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    // val pullToRefreshState = rememberPullToRefreshState()
    val isBusy = state.uiState is PredictionsUiState.Loading || state.isRefreshing

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is PredictionsUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is PredictionsUiEvent.Navigate -> { /* navController.navigate(event.route) */ }
                is PredictionsUiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    LaunchedEffect(user?.id, tournament?.id, dates) {
        val currentUser = user
        val currentTournament = tournament
        val activeDateId = dates.firstOrNull { it.active }?.id

        if (currentUser != null && currentTournament != null && activeDateId != null) {
            viewModel.setContext(userId = currentUser.id, tournamentId = currentTournament.id)
            viewModel.setSelectedDate(activeDateId)
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Mis predicciones",
                onBack = { viewModel.onBackPressed() },
                showBackButton = true,
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        when (val uiState = state.uiState) {
            is PredictionsUiState.Loading -> LoadingView()

            is PredictionsUiState.Error ->
                ErrorView(
                    message = uiState.message,
                    onRetry = { tournament?.id?.let { viewModel.getPredictions() } },
                )

            is PredictionsUiState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = paddingValues,
                ) {
                    item {
                        Spacer(Modifier.height(8.dp))
                    }

                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = "Fecha actual",
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            val selectedDate = dates.find { it.id == viewModel.selectedDateId }
                            val dateText = selectedDate?.name ?: "Todas las fechas"

                            Text(
                                text = dateText,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${uiState.predictions.size} partidos)",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    if (uiState.predictions.isEmpty() && !isBusy) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxWidth().padding(top = 24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("No hay partidos disponibles")
                            }
                        }
                    }

                    items(uiState.predictions) { prediction ->
                        PredictionCard(
                            prediction = prediction,
                            onClick = {
                                viewModel.selectPrediction(prediction)
                                navController.navigate(Routes.PredictionEdit.route)
                            },
                        )
                    }
                }
            }
        }
    }
}
