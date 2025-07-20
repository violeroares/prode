package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun MyPredictionsScreen() {
//    val exitAlwaysScrollBehavior =
//        FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection = End)
//    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
//
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Calendario", "Ranking", "Pronósticos", "Mi Perfil")
    val selectedIcons =
        listOf(
            Icons.Filled.Home,
            Icons.Filled.CalendarToday,
            Icons.Filled.EmojiEvents,
            Icons.Filled.AddChart,
            Icons.Filled.Person,
        )
    val unselectedIcons =
        listOf(
            Icons.Outlined.Home,
            Icons.Outlined.CalendarToday,
            Icons.Outlined.EmojiEvents,
            Icons.Outlined.AddChart,
            Icons.Outlined.Person,
        )

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = Modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = { Text("Buscar equipo...") },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        IconButton(
                            onClick = { scope.launch { searchBarState.animateToCollapsed() } },
                        ) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = {
                    // Icon(Icons.Default.MoreVert, contentDescription = null)

                    IconButton(
                        onClick = {
                            // scope.launch { searchBarState.animateToCollapsed() }
                        },
                    ) {
                        Icon(Icons.Outlined.FilterList, contentDescription = "Filter")
                    }
                },
            )
        }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopSearchBar(
                scrollBehavior = scrollBehavior,
                state = searchBarState,
                inputField = inputField,
            )
            ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField) {
//                SearchResults(
//                    onResultClick = { result ->
//                        textFieldState.setTextAndPlaceCursorAtEnd(result)
//                        scope.launch { searchBarState.animateToCollapsed() }
//                    },
//                )
            }
        },
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                    )
                }
            }
        },
        // modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // modifier = Modifier.nestedScroll(exitAlwaysScrollBehavior).nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            TwoRowsTopAppBar(
//                title = { expanded ->
//                    if (expanded) {
//                        Text(
//                            "Predicciones",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            // modifier = Modifier.padding(bottom = 24.dp),
//                        )
//                    } else {
//                        Text("Predicciones", maxLines = 1, overflow = TextOverflow.Ellipsis)
//                    }
//                },
//                subtitle = { expanded ->
//                    if (expanded) {
//                        Text(
//                            "Torneo: Clausura 2025",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier.padding(bottom = 24.dp),
//                        )
//                    } else {
//                        Text("Torneo: Clausura 2025", maxLines = 1, overflow = TextOverflow.Ellipsis)
//                    }
//                },
//                collapsedHeight = 64.dp,
//                expandedHeight = 96.dp,
//                navigationIcon = {
//                    TooltipBox(
//                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
//                        tooltip = { PlainTooltip { Text("Atrás") } },
//                        state = rememberTooltipState(),
//                    ) {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
//                        }
//                    }
//                },
//                scrollBehavior = scrollBehavior,
//            )
//        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val list = List(100) { "Text $it" }
            items(count = list.size) { item ->
                ElevatedCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(100.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                ) {
                    Box(Modifier.fillMaxSize().padding(vertical = 8.dp)) {
                        Text("Card content", Modifier.align(Alignment.Center))
                        Text(
                            text = list[item],
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }

//        LazyColumn(
//            modifier =
//                Modifier
//                    .fillMaxSize()
//                    .padding(vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            contentPadding = paddingValues,
//        ) {
//        Box(Modifier.padding(paddingValues)) {
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .verticalScroll(rememberScrollState()),
//            ) {
//                ElevatedCard(Modifier.fillMaxWidth()) {
//                    Box(Modifier.fillMaxSize()) { Text("Card content", Modifier.align(Alignment.Center)) }
//                }
//                Text(text = remember { LoremIpsum().values.first() })
//            }
//            VerticalFloatingToolbar(
//                // Always expanded as the toolbar is right-centered. We will use a
//                // FloatingToolbarScrollBehavior to hide both the toolbar and its FAB on scroll.
//                expanded = true,
//                floatingActionButton = {
//                    // Match the FAB to the vibrantColors. See also StandardFloatingActionButton.
//                    FloatingToolbarDefaults.VibrantFloatingActionButton(
//                        onClick = { /* doSomething() */ },
//                    ) {
//                        Icon(Icons.Filled.Add, "Localized description")
//                    }
//                },
//                modifier = Modifier.align(Alignment.CenterEnd).offset(x = -ScreenOffset),
//                colors = vibrantColors,
//                scrollBehavior = exitAlwaysScrollBehavior,
//                content = {
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(Icons.Filled.Person, contentDescription = "Localized description")
//                    }
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(Icons.Filled.Edit, contentDescription = "Localized description")
//                    }
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
//                    }
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(Icons.Filled.MoreVert, contentDescription = "Localized description")
//                    }
//                },
//            )
//        }
    }
}
