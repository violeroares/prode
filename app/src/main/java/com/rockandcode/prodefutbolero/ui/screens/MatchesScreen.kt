package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.ErrorView
import com.rockandcode.prodefutbolero.ui.components.LoadingView
import com.rockandcode.prodefutbolero.ui.components.MatchStatusCard
import com.rockandcode.prodefutbolero.ui.components.PaginationLoadingItem
import com.rockandcode.prodefutbolero.ui.components.SearchAppHeader
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    mainViewModel: MainViewModel,
    viewModel: MatchesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.screenState.collectAsState()
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val dates by mainViewModel.dates.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val isBusy = state.uiState is MatchesUiState.Loading || viewModel.isPaginating || state.isRefreshing
    val snackbarHostState = remember { SnackbarHostState() }
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    // Snackbar listener
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MatchesUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is MatchesUiEvent.Navigate -> { /* navController.navigate(event.route) */ }
                is MatchesUiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    // --- CARGAR PARTIDOS AL CAMBIAR TORNEO O FECHAS ---
    LaunchedEffect(tournament?.id, dates) {
        val currentTournament = tournament
        val activeDateId = dates.firstOrNull { it.active }?.id
        if (currentTournament != null && activeDateId != null) {
            viewModel.setContext(currentTournament.id)
            viewModel.setSelectedDate(activeDateId)
        }
    }

    // --- SCROLL INFINITO ---
    LaunchedEffect(listState, state.uiState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                val matches = (state.uiState as? MatchesUiState.Success)?.matches ?: return@collect
                val shouldLoadMore = index + listState.layoutInfo.visibleItemsInfo.size >= matches.size - 3
                if (shouldLoadMore && !isBusy && viewModel.currentPage < viewModel.totalPages) {
                    viewModel.loadNextPage(viewModel.searchQuery, viewModel.selectedDateId)
                }
            }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            tournament?.id?.let {
                viewModel.getMatches(
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
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                SearchAppHeader(
                    title = "Calendario",
                    onBack = { navController.popBackStack() },
                    showBackButton = false,
                    searchQuery = viewModel.searchQuery,
                    onSearchQueryChanged = { s -> viewModel.onSearchQueryChanged(s) },
                    onFilterClick = { isFilterSheetOpen = true },
                    filtersActive = if (viewModel.selectedDateId != null) 1 else 0,
                )
            },
        ) { paddingValues ->
            when (val uiState = state.uiState) {
                is MatchesUiState.Loading -> LoadingView()

                is MatchesUiState.Error ->
                    ErrorView(
                        message = uiState.message,
                        onRetry = { tournament?.id?.let { viewModel.getMatches() } },
                    )

                is MatchesUiState.Success -> {
                    // Reset scroll al cambiar la lista
                    LaunchedEffect(uiState.matches) {
                        listState.scrollToItem(0)
                    }

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
                                    text = "(${uiState.totalMatches} partidos)",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        // Info de fecha seleccionada
//                        viewModel.selectedDateId?.let { selectedId ->
//                            val selectedDate = dates.find { it.id == selectedId }
//                            selectedDate?.let {
//                                item {
//                                    Row(
//                                        modifier =
//                                            Modifier
//                                                .fillMaxWidth()
//                                                .padding(vertical = 8.dp, horizontal = 24.dp),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Event,
//                                            contentDescription = "Fecha actual",
//                                            modifier = Modifier.size(20.dp),
//                                        )
//                                        Spacer(modifier = Modifier.width(8.dp))
//                                        Text(
//                                            text = selectedDate.name,
//                                            style = MaterialTheme.typography.labelLarge,
//                                            fontWeight = FontWeight.Bold,
//                                        )
//                                        Spacer(modifier = Modifier.width(8.dp))
//                                        Text(
//                                            text = "(${uiState.matches.size} partidos)",
//                                            style = MaterialTheme.typography.bodySmall,
//                                        )
//                                    }
//                                }
//                            }
//                        }

                        if (uiState.matches.isEmpty() && !isBusy) {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("No hay partidos disponibles")
                                }
                            }
                        }

                        items(
                            uiState.matches,
                            key = { match -> match.matchId },
                        ) { match ->
                            MatchStatusCard(
                                match = match,
                                onClick = { viewModel.onMatchClick(match) },
                            )
                        }

                        if (viewModel.isPaginating) {
                            item { PaginationLoadingItem() }
                        }
                        item {
                            Spacer(Modifier.height(96.dp))
                        }
                    }
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
                                        viewModel.setSelectedDate(null)
                                        isFilterSheetOpen = false
                                    },
                        )
                        HorizontalDivider()
                    }

                    // Fechas del torneo (desde MainViewModel)
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
                                        viewModel.setSelectedDate(date.id)
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
