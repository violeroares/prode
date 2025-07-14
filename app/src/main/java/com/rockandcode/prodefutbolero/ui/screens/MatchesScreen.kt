package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import com.rockandcode.prodefutbolero.ui.components.MatchCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    mainViewModel: MainViewModel,
    viewModel: MatchesViewModel = hiltViewModel(),
) {
    val tournament by mainViewModel.selectedTournament.collectAsState()
    val matches = viewModel.matches
    val isLoading = viewModel.isLoading
    val isRefreshing = viewModel.isRefreshing
    val isPaginating = viewModel.isPaginating
    val isBusy = isLoading || isRefreshing || isPaginating
    val listState = rememberLazyListState()
    val state = rememberPullToRefreshState()
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh(
                tournament?.id,
                viewModel.searchQuery,
                viewModel.selectedDateId,
                isPullToRefresh = true,
            )
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state,
            )
        },
    ) {
        // Mostrar loading sólo si está cargando y no hay matches (carga inicial)
        if (isLoading && matches.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    Column {
                        OutlinedTextField(
                            value = viewModel.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it, tournament?.id) },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            placeholder = { Text("Buscar equipo...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Buscar")
                            },
                            trailingIcon = {
                                IconButton(onClick = { isFilterSheetOpen = true }) {
                                    Icon(Icons.Default.FilterList, contentDescription = "Filtrar por fecha")
                                }
                            },
                            singleLine = true,
                        )
                    }
                }

                if (viewModel.selectedDateId != null) {
                    val selectedDate = viewModel.dates.find { it.id == viewModel.selectedDateId }
                    selectedDate?.let {
                        item {
                            Text(
                                text = "Filtrando por: ${it.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier =
                                    Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            RoundedCornerShape(8.dp),
                                        ).padding(horizontal = 12.dp, vertical = 6.dp),
                            )
                        }
                    }
                }

                if (matches.isEmpty() && !isBusy) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "No hay partidos disponibles",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                items(matches) { match ->
                    MatchCard(match)
                }

                if (isPaginating) {
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

                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }

    LaunchedEffect(matches.size, isBusy) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                val shouldLoadMore =
                    index + listState.layoutInfo.visibleItemsInfo.size >= matches.size - 3
                // Sólo cargar más si NO está ocupado y hay más páginas
                if (shouldLoadMore && !isBusy && viewModel.currentPage <= viewModel.totalPages) {
                    viewModel.loadNextPage(
                        tournament?.id,
                        viewModel.searchQuery,
                        viewModel.selectedDateId,
                    )
                }
            }
    }

    LaunchedEffect(tournament?.id) {
        tournament?.id?.let {
            viewModel.loadDates(it)
            delay(100)
            viewModel.refresh(it, viewModel.searchQuery, viewModel.selectedDateId)
        }
    }

    // BOTTOM SHEET DE FECHAS
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
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectedDateId = null
                                    viewModel.refresh(
                                        tournament?.id,
                                        viewModel.searchQuery,
                                        null,
                                    )
                                    isFilterSheetOpen = false
                                },
                    )
                }

                items(viewModel.dates) { date ->
                    val isSelected = date.id == viewModel.selectedDateId
                    ListItem(
                        headlineContent = {
                            Text(
                                date.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectedDateId = date.id
                                    viewModel.refresh(
                                        tournament?.id,
                                        viewModel.searchQuery,
                                        date.id,
                                    )
                                    isFilterSheetOpen = false
                                },
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
