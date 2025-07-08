package com.rockandcode.prodefutbolero.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rockandcode.prodefutbolero.ui.components.BottomNavigationBar
import com.rockandcode.prodefutbolero.ui.screens.HomeScreen

@Composable
fun AppStack(
    controller: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(
                navController = controller,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = controller,
            startDestination = "home",
        ) {
            composable("home") {
                HomeScreen(innerPadding = innerPadding)
            }
        }
    }
}
