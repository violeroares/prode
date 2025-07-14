package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Tour
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val icon: ImageVector,
    val route: String,
)

@Composable
fun FloatingBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val bottomItems =
        listOf(
            FloatingBottomNavItem("Inicio", Icons.Outlined.Home, Routes.Home.route),
            FloatingBottomNavItem("Matches", Icons.Outlined.AddChart, Routes.Matches.route),
            FloatingBottomNavItem("Torneos", Icons.Outlined.Tour, Routes.MyTournaments.route),
            FloatingBottomNavItem("Favoritos", Icons.Outlined.StarOutline, Routes.MyTournaments.route),
            FloatingBottomNavItem("Perfil", Icons.Outlined.PersonOutline, Routes.Profile.route),
        )

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFF8F8F8)
    val selectedColor = if (isDark) Color.White else Color.Black
    val unselectedColor = Color.Gray

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .shadow(8.dp, RoundedCornerShape(24.dp), clip = true)
                    .clip(RoundedCornerShape(24.dp))
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomItems.forEach { item ->
                val selected = currentRoute == item.route
                IconButton(
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
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) selectedColor else unselectedColor,
                    )
                }
            }
        }
    }
}
