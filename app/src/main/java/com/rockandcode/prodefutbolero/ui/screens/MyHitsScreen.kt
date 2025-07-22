package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.ui.components.ErrorView
import com.rockandcode.prodefutbolero.ui.components.LoadingView
import com.rockandcode.prodefutbolero.ui.components.PaginationLoadingItem
import com.rockandcode.prodefutbolero.utils.formatFechaHora
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
    val isDark = isSystemInDarkTheme()

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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mis aciertos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(start = 8.dp)) {
                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .background(
                                        if (!isDark) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            Color(0xFFFAFAFA)
                                        },
                                        shape = CircleShape,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint =
                                    if (!isDark) {
                                        Color.Black
                                    } else {
                                        // MaterialTheme.colorScheme.onSecondaryContainer
                                        Color.Black
                                    },
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { isFilterSheetOpen = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.White,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
            )
        },
//        topBar = {
//            SearchAppHeader(
//                title = "Mis aciertos",
//                onBack = { navController.popBackStack() },
//                showBackButton = true,
//                searchQuery = viewModel.searchQuery,
//                onSearchQueryChanged = { s -> viewModel.onSearchQueryChanged(s) },
//                onFilterClick = { isFilterSheetOpen = true },
//                filtersActive = 1,
//            )
//        },
        // contentWindowInsets = WindowInsets(0),
        containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.White,
    ) { innerPadding ->
        when (val uiState = state.uiState) {
            is HitsUiState.Loading -> {
                LoadingView()
            }

            is HitsUiState.Error -> {
                ErrorView(
                    message = uiState.message,
                    onRetry = {
                        tournament?.id?.let {
                            viewModel.getHits()
                        }
                    },
                )
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
//                    LazyColumn(contentPadding = innerPadding) {
//                        items(items) { (name, message) ->
//                            InboxItem(name = name, message = message, time = "11:32 AM", badgeCount = 3)
//                        }
//                    }

                    LazyColumn(
                        state = listState,
                        contentPadding = innerPadding,
                    ) {
                        item {
                            Spacer(Modifier.height(8.dp))
                        }
//                        item {
//                            AppHeader(
//                                title = "Calendario",
//                                onBack = { viewModel.onBackPressed() },
//                                showBackButton = true,
//                            )
//                        }

//                        item {
//                            OutlinedTextField(
//                                value = viewModel.searchQuery,
//                                onValueChange = {
//                                    viewModel.onSearchQueryChanged(
//                                        it,
//                                    )
//                                },
//                                modifier =
//                                    Modifier
//                                        .fillMaxWidth()
//                                        .padding(16.dp),
//                                placeholder = { Text("Buscar equipo...") },
//                                leadingIcon = {
//                                    Icon(
//                                        Icons.Default.Search,
//                                        contentDescription = null,
//                                    )
//                                },
//                                trailingIcon = {
//                                    IconButton(onClick = { isFilterSheetOpen = true }) {
//                                        Icon(
//                                            Icons.Default.FilterList,
//                                            contentDescription = "Filtrar por fecha",
//                                        )
//                                    }
//                                },
//                                singleLine = true,
//                            )
//                        }
                        viewModel.selectedDateId?.let { selectedId ->
                            val selectedDate = dates.find { it.id == selectedId }
                            selectedDate?.let {
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
                                        Text(
                                            text = selectedDate.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "(${uiState.hits.size} partidos)",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(8.dp))
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
                            MyHitItem(hit = hit, isDark = isDark)
                            // HitCard(hit = hit, isDark = isDark)
//                            {
//                                viewModel.onMatchClick(hit)
//                            }
                        }

                        if (viewModel.isPaginating) {
                            item { PaginationLoadingItem() }
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
                viewModel.setSelectedDate(activeDateId)
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
                                        viewModel.setSelectedDate(null)
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

@Composable
fun MyHitItem(
    hit: Hit,
    isDark: Boolean,
) {
    val pointsColor =
        when (hit.points) {
            0 -> Color.Red // Color para 0 puntos
            3 -> Color(0xFFFF9800) // Naranja para 3
            4 -> Color(0xFF2196F3) // Azul para 4
            6 -> Color(0xFF4CAF50) // Verde para 6
            else -> if (isDark) Color.Black else Color.White
        }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar con iniciales
            Box(
                modifier =
                    Modifier
                        .width(72.dp),
                // .background(Color(0xFF5E9EFF)),
                contentAlignment = Alignment.Center,
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = hit.localPictureUrl,
                            contentDescription = "Escudo ${hit.localName}",
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Text("${hit.realLocalGoals}", fontWeight = FontWeight.Bold)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = hit.visitorPictureUrl,
                            contentDescription = "Escudo ${hit.localName}",
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Text("${hit.realVisitorGoals}", fontWeight = FontWeight.Bold)
                    }
                }

//                Text(
//                    text =
//                        hit.localName
//                            .split(" ")
//                            .map { it.first() }
//                            .joinToString(""),
//                    color = Color.White,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                )
                // Estado (punto verde)
//                Box(
//                    Modifier
//                        .size(8.dp)
//                        .align(Alignment.TopEnd)
//                        .offset(x = 2.dp, y = (-2).dp)
//                        .background(Color.Green, CircleShape),
//                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                Text("${hit.localName} vs ${hit.visitorName}", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(
                    formatFechaHora(hit.date),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    "Mi resultado: ${hit.myLocalGoals} - ${hit.myVisitorGoals} ",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDark) Color.LightGray else Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(hit.groupName, color = Color.Gray, fontSize = 12.sp)

                Box(
                    modifier =
                        Modifier
                            .padding(top = 4.dp)
                            .size(24.dp) // tamaño fijo
                            .background(pointsColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "${hit.points}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        // Divider que empieza donde empieza el texto (después del avatar + espacio)
        HorizontalDivider(
            modifier =
                Modifier
                    .padding(start = 84.dp) // 42 avatar + 12 spacer
                    .padding(top = 8.dp),
            thickness = 1.dp,
            color = if (isDark) Color.DarkGray else Color.LightGray,
        )
    }
}
