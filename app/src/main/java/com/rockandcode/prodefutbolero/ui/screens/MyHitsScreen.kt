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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.ui.components.AppHeader
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHitsScreen(
    mainViewModel: MainViewModel,
    viewModel: MyHitsViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.screenState.collectAsState()
    val user by mainViewModel.user.collectAsState()
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val dates by mainViewModel.dates.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val isBusy = state.uiState is HitsUiState.Loading || viewModel.isPaginating || state.isRefreshing
    val snackbarHostState = remember { SnackbarHostState() }
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    // Snackbar listener
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is HitsUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is HitsUiEvent.Navigate -> { /* navController.navigate(event.route) */ }
                is HitsUiEvent.PopBackStack -> {
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
            is HitsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is HitsUiState.Error -> {
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
                        IconButton(onClick = {
                            tournament?.id?.let {
                                viewModel.getHits()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Actualizar",
                            )
                        }
                    }
                }
            }

            is HitsUiState.Success -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = {
                        tournament?.id?.let {
                            viewModel.getHits(
                                teamName = viewModel.searchQuery,
                                dateId = viewModel.selectedDateId,
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
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = paddingValues,
                    ) {
                        item {
                            AppHeader(
                                title = "Calendario",
                                onBack = { viewModel.onBackPressed() },
                                showBackButton = true,
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = viewModel.searchQuery,
                                onValueChange = {
                                    viewModel.onSearchQueryChanged(
                                        it,
                                    )
                                },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                placeholder = { Text("Buscar equipo...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null,
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isFilterSheetOpen = true }) {
                                        Icon(
                                            Icons.Default.FilterList,
                                            contentDescription = "Filtrar por fecha",
                                        )
                                    }
                                },
                                singleLine = true,
                            )
                        }

                        viewModel.selectedDateId?.let { selectedId ->
                            val selectedDate = dates.find { it.id == selectedId }
                            selectedDate?.let {
                                item {
                                    Row(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp, horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Event,
                                            contentDescription = "Fecha actual",
                                            modifier = Modifier.size(20.dp),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Mostrando: ${selectedDate.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.hits.isEmpty() && !isBusy) {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("No hay aciertos disponibles")
                                }
                            }
                        }

                        items(uiState.hits) { hit ->
                            HitCard(hit = hit) {
                                viewModel.onMatchClick(hit)
                            }
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

        LaunchedEffect(user?.id, tournament?.id, dates) {
            val currentUser = user
            val currentTournament = tournament
            val activeDateId = dates.firstOrNull { it.active }?.id

            if (currentUser != null && currentTournament != null && activeDateId != null) {
                viewModel.setContext(userId = currentUser.id, tournamentId = currentTournament.id)
                viewModel.selectedDateId = activeDateId
                viewModel.getHits(
                    teamName = null,
                    dateId = activeDateId,
                    isPullToRefresh = false,
                )
            }
        }

        // Scroll infinito
        LaunchedEffect(
            remember { derivedStateOf { listState.firstVisibleItemIndex } },
            state.uiState,
        ) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { index ->
                    val matches =
                        (state.uiState as? HitsUiState.Success)?.hits ?: return@collect
                    val shouldLoadMore =
                        index + listState.layoutInfo.visibleItemsInfo.size >= matches.size - 3
                    if (shouldLoadMore && !isBusy && viewModel.currentPage <= viewModel.totalPages) {
                        viewModel.loadNextPage()
                    }
                }
        }

        // BottomSheet para filtro por fecha
        if (isFilterSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isFilterSheetOpen = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            ) {
                Text(
                    "Filtrar por fecha",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                )

                LazyColumn {
                    item {
                        val isAllSelected = viewModel.selectedDateId == null
                        ListItem(
                            headlineContent = {
                                Text(
                                    "Mostrar todas",
                                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            trailingContent = {
                                if (isAllSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                }
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectedDateId = null
                                        viewModel.getHits(
                                            viewModel.searchQuery,
                                            null,
                                        )
                                        isFilterSheetOpen = false
                                    },
                        )
                        HorizontalDivider()
                    }

                    items(dates) { date ->
                        val isSelected = date.id == viewModel.selectedDateId
                        ListItem(
                            headlineContent = {
                                Text(
                                    date.name,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            trailingContent = {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                }
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectedDateId = date.id
                                        viewModel.getHits(
                                            viewModel.searchQuery,
                                            date.id,
                                        )
                                        isFilterSheetOpen = false
                                    },
                        )
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HitCard(
    hit: Hit,
    onMatchClick: () -> Unit = {},
) {
    Card(modifier = Modifier.clickable(onClick = onMatchClick)) {
        Text(hit.firstName)
    }
}
