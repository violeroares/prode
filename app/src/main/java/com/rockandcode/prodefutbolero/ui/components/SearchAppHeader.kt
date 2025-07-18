package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppHeader(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String = "",
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    showBackButton: Boolean = true,
    searchQuery: String,
    filtersActive: Int,
    onSearchQueryChanged: (String) -> Unit,
    onFilterClick: () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isDarkTheme) MaterialTheme.colorScheme.surface else Color.White, // fondo se da en el Box
        shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        ) {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                            if (subTitle.isNotEmpty()) {
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = subTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = onBack) {
                                Box(
                                    modifier =
                                        Modifier
                                            .size(36.dp)
                                            .background(
                                                if (!isDarkTheme) {
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
                                            if (!isDarkTheme) {
                                                Color.Black
                                            } else {
                                                // MaterialTheme.colorScheme.onSecondaryContainer
                                                Color.Black
                                            },
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    actions = actions,
                )
                CompactSearchBar(
                    searchQuery = searchQuery,
                    filtersActive = filtersActive,
                    onQueryChange = { onSearchQueryChanged(it) },
                    onFilterClick = onFilterClick,
                )
            }
        }
    }
}
