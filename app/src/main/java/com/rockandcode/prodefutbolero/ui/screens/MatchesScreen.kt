package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import com.rockandcode.prodefutbolero.ui.components.AppHeader
import com.rockandcode.prodefutbolero.ui.components.MatchCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    mainViewModel: MainViewModel,
    viewModel: MatchesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val screenState by viewModel.screenState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val pullState = rememberPullToRefreshState()
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    val matchesUi = screenState.uiState
    val matches = (matchesUi as? MatchesUiState.Success)?.matches ?: emptyList()
    val isLoading = matchesUi is MatchesUiState.Loading
    val isPaginating = false // Podés mannerly en el futuro si querés
    val isBusy = isLoading || screenState.isRefreshing || isPaginating

    // Snackbar listener
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.Navigate -> { /* navController.navigate(event.route) */ }
                is UiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        PullToRefreshBox(
            isRefreshing = screenState.isRefreshing,
            onRefresh = {
                viewModel.refresh(
                    tournament?.id,
                    viewModel.searchQuery,
                    viewModel.selectedDateId,
                    isPullToRefresh = true,
                )
            },
            state = pullState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = screenState.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullState,
                )
            },
        ) {
//            if (isLoading) {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            } else {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
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
                        onValueChange = { viewModel.onSearchQueryChanged(it, tournament?.id) },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        placeholder = { Text("Buscar equipo...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { isFilterSheetOpen = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filtrar por fecha")
                            }
                        },
                        singleLine = true,
                    )
                }

                viewModel.selectedDateId?.let { selectedId ->
                    val selectedDate = screenState.dates.find { it.id == selectedId }
                    selectedDate?.let {
                        item {
//                            Text(
//                                text = "Filtrando por: ${it.name}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                modifier =
//                                    Modifier
//                                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                                        .background(
//                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                                            RoundedCornerShape(8.dp),
//                                        ).padding(horizontal = 12.dp, vertical = 6.dp),
//                            )

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

                if (matches.isEmpty() && !isBusy) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("No hay partidos disponibles")
                        }
                    }
                }

                items(matches) { match ->
                    MatchCard(match = match) {
                        viewModel.onMatchClick(match)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
//            }
        }
    }

    // Scroll infinito
    LaunchedEffect(matches.size, isBusy) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                val visibleCount = listState.layoutInfo.visibleItemsInfo.size
                val shouldLoadMore = index + visibleCount >= matches.size - 3
                val successState = screenState.uiState as? MatchesUiState.Success
                if (shouldLoadMore && !isBusy && successState != null) {
                    viewModel.loadNextPage(
                        tournament?.id,
                        viewModel.searchQuery,
                        viewModel.selectedDateId,
                    )
                }
            }
    }

    // Carga inicial
    LaunchedEffect(tournament?.id) {
        tournament?.id?.let {
            viewModel.loadDates(it)
            delay(100)
            viewModel.refresh(it, viewModel.searchQuery, viewModel.selectedDateId)
        }
    }

    // BottomSheet para filtro por fecha
    if (isFilterSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isFilterSheetOpen = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Text("Filtrar por fecha", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))

            LazyColumn {
                item {
                    val isAllSelected = viewModel.selectedDateId == null
                    ListItem(
                        headlineContent = {
                            Text("Mostrar todas", fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal)
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
                                    viewModel.refresh(tournament?.id, viewModel.searchQuery, null)
                                    isFilterSheetOpen = false
                                },
                    )
                    HorizontalDivider()
                }

                items(screenState.dates) { date ->
                    val isSelected = date.id == viewModel.selectedDateId
                    ListItem(
                        headlineContent = {
                            Text(date.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
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
                                    viewModel.refresh(tournament?.id, viewModel.searchQuery, date.id)
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
