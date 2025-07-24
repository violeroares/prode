package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rockandcode.prodefutbolero.R
import com.rockandcode.prodefutbolero.ui.navigation.Routes

data class FloatingBottomNavItem(
    val label: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val route: String,
)

@Composable
fun FloatingBottomNavigationBar(
    modifier: Modifier = Modifier,
    incompleteCount: Int = 0,
    navController: NavHostController,
) {
    val bottomItems =
        listOf(
            FloatingBottomNavItem(
                "Inicio",
                painterResource(id = R.drawable.home_24dp_filled),
                painterResource(id = R.drawable.home_24dp_outlined),
                Routes.Home.route,
            ),
            FloatingBottomNavItem(
                "Partidos",
                painterResource(id = R.drawable.event_24dp_filled),
                painterResource(id = R.drawable.event_24dp_outlined),
                Routes.Matches.route,
            ),
            FloatingBottomNavItem(
                "Pronósticos",
                painterResource(id = R.drawable.add_chart_24dp_filled),
                painterResource(id = R.drawable.add_chart_24dp_outlined),
                Routes.MyPredictions.route,
            ),
            FloatingBottomNavItem(
                "Ranking",
                painterResource(id = R.drawable.trophy_24dp_filled),
                painterResource(id = R.drawable.trophy_24dp_outlined),
                Routes.Ranking.route,
            ),
            FloatingBottomNavItem(
                "Perfil",
                painterResource(id = R.drawable.person_24dp_filled),
                painterResource(id = R.drawable.person_24dp_outlined),
                Routes.Profile.route,
            ),
        )

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF27292C) else Color(0xFFFFFFFF)
    val selectedColor = if (isDark) Color(0xFFA2F7A1) else Color(0xFF4270F6)
    val unselectedColor = if (isDark) Color.White else Color.DarkGray

    // Calculamos el ancho total = (número de ítems * ancho ítem) + (espacios entre ítems)
    val itemSize = 66.dp
    val spacing = 4.dp
    val totalWidth = (bottomItems.size * itemSize) + ((bottomItems.size - 1) * spacing) + 8.dp // +8dp (4dp a cada lado)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .width(totalWidth)
                    .height(74.dp)
                    // .shadow(8.dp, RoundedCornerShape(28.dp), clip = true)
                    .clip(RoundedCornerShape(28.dp))
                    .background(backgroundColor)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomItems.forEach { item ->
                val selected = currentRoute == item.route
                Box(
                    modifier =
                        Modifier
                            .size(itemSize)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable(onClick = {
                                if (!selected) {
                                    navController.navigate(item.route) {
                                        popUpTo(Routes.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            })
                            .background(
                                if (isDark) Color(0xFF2E3033) else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(24.dp),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    BadgedBox(
                        badge = {
                            if (incompleteCount > 0 && item.route == Routes.MyPredictions.route) {
                                Badge {
                                    Text(incompleteCount.toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            painter = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (selected) selectedColor else unselectedColor,
                        )
                    }
                }
            }
        }
    }
}
