package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.AppHeader
import com.rockandcode.prodefutbolero.ui.components.RankingCard
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    mainViewModel: MainViewModel,
    viewModel: RankingViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.screenState.collectAsState()
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val isBusy = state.uiState is RankingUiState.Loading || viewModel.isPaginating || state.isRefreshing
    val snackbarHostState = remember { SnackbarHostState() }

    // Snackbar listener
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RankingUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is RankingUiEvent.Navigate -> { /* navController.navigate(event.route) */ }
                is RankingUiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->

        when (val uiState = state.uiState) {
            is RankingUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RankingUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                        )
                        IconButton(onClick = { tournament?.id?.let { viewModel.getRanking(it.toInt()) } }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                            )
                        }
                    }
                }
            }

            is RankingUiState.Success -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = {
                        tournament?.id?.let {
                            viewModel.getRanking(
                                tournamentId = it,
                                isPullToRefresh = true,
                            )
                        }
                    },
                    state = pullToRefreshState,
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = state.isRefreshing,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            state = pullToRefreshState,
                        )
                    },
                ) {
                    LazyColumn(
                        state = listState,
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        // .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = paddingValues,
                    ) {
                        item {
                            AppHeader(
                                title = "Ranking General",
                                onBack = { viewModel.onBackPressed() },
                                showBackButton = true,
                            )
                        }

                        // Card especial para el primer puesto
//                        if (uiState.rankings.isNotEmpty()) {
//                            item {
//                                TestCard(uiState.rankings.first())
//                                // FirstPlaceCard(uiState.rankings.first())
//                            }
//                        }
//
//                        // Resto del ranking
//                        items(uiState.rankings.drop(1)) { user ->
//                            RankingCard(user)
//                        }

                        items(uiState.rankings) { user ->
                            RankingCard(user)
                        }

                        if (viewModel.isPaginating) {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            tournament?.id?.let {
                viewModel.getRanking(it)
            }
        }

        LaunchedEffect(
            remember { derivedStateOf { listState.firstVisibleItemIndex } },
            state.uiState,
        ) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { index ->
                    val rankings =
                        (state.uiState as? RankingUiState.Success)?.rankings ?: return@collect
                    val shouldLoadMore =
                        index + listState.layoutInfo.visibleItemsInfo.size >= rankings.size - 3
                    if (shouldLoadMore && !isBusy && viewModel.currentPage <= viewModel.totalPages) {
                        viewModel.loadNextPage(tournamentId = tournament?.id ?: return@collect)
                    }
                }
        }
    }
}
