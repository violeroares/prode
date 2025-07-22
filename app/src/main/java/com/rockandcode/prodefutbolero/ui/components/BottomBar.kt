package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rockandcode.prodefutbolero.R
import com.rockandcode.prodefutbolero.ui.navigation.Routes

data class BottomNavItem(
    val label: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val route: String,
)

@Composable
fun BottomBar(
    navController: NavHostController,
    incompleteCount: Int = 0,
) {
    val isDark = isSystemInDarkTheme()
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val bottomItems =
        listOf(
            BottomNavItem(
                "Inicio",
                painterResource(id = R.drawable.home_24dp_filled),
                painterResource(id = R.drawable.home_24dp_outlined),
                Routes.Home.route,
            ),
            BottomNavItem(
                "Partidos",
                painterResource(id = R.drawable.event_24dp_filled),
                painterResource(id = R.drawable.event_24dp_outlined),
                Routes.Matches.route,
            ),
            BottomNavItem(
                "Pronósticos",
                painterResource(id = R.drawable.add_chart_24dp_filled),
                painterResource(id = R.drawable.add_chart_24dp_outlined),
                Routes.MyPredictions.route,
            ),
            BottomNavItem(
                "Ranking",
                painterResource(id = R.drawable.trophy_24dp_filled),
                painterResource(id = R.drawable.trophy_24dp_outlined),
                Routes.Ranking.route,
            ),
            BottomNavItem(
                "Perfil",
                painterResource(id = R.drawable.person_24dp_filled),
                painterResource(id = R.drawable.person_24dp_outlined),
                Routes.Profile.route,
            ),
        )

    NavigationBar(
        containerColor = if (isDark) Color(0xFF27292C) else Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
    ) {
        bottomItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (incompleteCount > 0 && item.route == Routes.Matches.route) {
                                Badge {
                                    Text(incompleteCount.toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            // imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            painter = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                        )
                    }
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = if (isDark) Color(0xFFA2F7A1) else Color(0xFF4270F6),
                        // unselectedIconColor = Color.White,
                        selectedTextColor = if (isDark) Color(0xFFA2F7A1) else Color(0xFF4270F6),
                        // unselectedTextColor = Color.White,
                        indicatorColor = Color.Transparent,
                        // indicatorColor = if (!isDark) Color(0xFF4F3BC4) else MaterialTheme.colorScheme.secondaryContainer,
                    ),
            )
        }
    }
}
