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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonOutline
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rockandcode.prodefutbolero.ui.navigation.Routes

data class FloatingBottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
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
            FloatingBottomNavItem("Inicio", Icons.Rounded.Home, Icons.Outlined.Home, Routes.Home.route),
            FloatingBottomNavItem("Matches", Icons.Rounded.CalendarToday, Icons.Outlined.CalendarToday, Routes.Matches.route),
            FloatingBottomNavItem("Favoritos", Icons.Rounded.BarChart, Icons.Outlined.BarChart, Routes.MyPredictions.route),
            FloatingBottomNavItem("Ranking", Icons.Rounded.EmojiEvents, Icons.Outlined.EmojiEvents, Routes.Ranking.route),
            FloatingBottomNavItem("Perfil", Icons.Rounded.Person, Icons.Rounded.PersonOutline, Routes.Profile.route),
        )

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF27292C) else Color(0xFFFFFFFF)
    val selectedColor = if (isDark) Color(0xFFA2F7A1) else Color(0xFF4270F6)
    val unselectedColor = Color.Gray

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .shadow(8.dp, RoundedCornerShape(28.dp), clip = true)
                    .clip(RoundedCornerShape(28.dp))
                    .background(backgroundColor)
                    .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomItems.forEach { item ->
                val selected = currentRoute == item.route
//                IconButton(
//                    onClick = {
//                        if (!selected) {
//                            navController.navigate(item.route) {
//                                popUpTo(Routes.Home.route) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        }
//                    },
//                    modifier = Modifier.size(68.dp),
//                ) {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = item.label,
//                        tint = if (selected) selectedColor else unselectedColor,
//                    )
//                }

                Box(
                    modifier =
                        Modifier
                            .size(66.dp)
                            // .clip(CircleShape)
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
                                // shape = CircleShape,
                                shape = RoundedCornerShape(24.dp),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
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
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (selected) selectedColor else unselectedColor,
                        )
                    }
                }
            }
        }
    }
}
