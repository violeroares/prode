package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

// @Composable
// fun CompactSearchBar(
//    searchQuery: String,
//    filtersActive: Int,
//    onQueryChange: (String) -> Unit,
//    onFilterClick: () -> Unit,
//    modifier: Modifier = Modifier,
// ) {
//    Box(
//        modifier =
//            modifier
//                .fillMaxWidth()
//                .height(44.dp)
//                .padding(horizontal = 8.dp)
//                .clip(RoundedCornerShape(22.dp))
//                .background(MaterialTheme.colorScheme.surfaceVariant),
//        contentAlignment = Alignment.CenterStart,
//    ) {
//        Row(
//            modifier =
//                Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Buscar",
//                tint = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Box(modifier = Modifier.weight(1f)) {
//                if (searchQuery.isEmpty()) {
//                    Text(
//                        "Buscar equipo",
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
//                        style = MaterialTheme.typography.bodyMedium,
//                    )
//                }
//
//                BasicTextField(
//                    value = searchQuery,
//                    onValueChange = onQueryChange,
//                    singleLine = true,
//                    textStyle =
//                        MaterialTheme.typography.bodyMedium.copy(
//                            color = MaterialTheme.colorScheme.onSurface,
//                        ),
//                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
//                    modifier = Modifier.fillMaxWidth(),
//                )
//            }
//
//            IconButton(
//                onClick = onFilterClick,
//                modifier = Modifier.size(36.dp),
//            ) {
//                BadgedBox(
//                    badge = {
//                        if (filtersActive > 0) {
//                            Badge { Text(filtersActive.toString()) }
//                        }
//                    },
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.FilterList,
//                        contentDescription = "Filtros",
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                    )
//                }
//            }
//        }
//    }
// }

@Composable
fun CompactSearchBar(
    searchQuery: String,
    filtersActive: Int,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar equipo",
) {
    val focusRequester = remember { FocusRequester() }
    // val focusManager = LocalFocusManager.current
    val focusState = remember { mutableStateOf(false) }

    // Animamos el alto
    val animatedHeight by animateDpAsState(
        targetValue = if (focusState.value) 44.dp else 44.dp,
        animationSpec = tween(durationMillis = 250),
        label = "heightAnimation",
    )

    // Animamos el color de fondo
    val backgroundColor by animateColorAsState(
        targetValue =
            if (focusState.value) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        animationSpec = tween(durationMillis = 250),
        label = "backgroundColorAnimation",
    )

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(animatedHeight / 2))
                .background(backgroundColor)
                .clickable { focusRequester.requestFocus() },
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Ícono de búsqueda
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Campo de texto
            BasicTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState.value = it.isFocused },
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    innerTextField()
                },
            )

            // Botón limpiar (X), aparece cuando hay texto
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Botón de filtros con Badge
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.size(36.dp),
            ) {
                BadgedBox(
                    badge = {
                        if (filtersActive > 0) {
                            Badge { Text(filtersActive.toString()) }
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtros",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
