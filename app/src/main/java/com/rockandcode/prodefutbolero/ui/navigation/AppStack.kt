package com.rockandcode.prodefutbolero.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rockandcode.prodefutbolero.ui.components.FloatingBottomNavigationBar
import com.rockandcode.prodefutbolero.ui.screens.HomeScreen
import com.rockandcode.prodefutbolero.ui.screens.MainViewModel
import com.rockandcode.prodefutbolero.ui.screens.MyPredictionsScreen
import com.rockandcode.prodefutbolero.ui.screens.MyTournamentsScreen
import com.rockandcode.prodefutbolero.ui.screens.ProfileScreen
import com.rockandcode.prodefutbolero.ui.screens.TournamentsScreen

@Composable
fun AppStack(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val bottomBarRoutes =
        listOf(
            Routes.Home.route,
        )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                FloatingBottomNavigationBar(navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.TournamentSelect.route,
        ) {
            composable(Routes.TournamentSelect.route) {
                TournamentsScreen(
                    viewModel = mainViewModel,
                    modifier = Modifier.padding(innerPadding),
                    onTournamentSelected = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.TournamentSelect.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(Routes.Home.route) {
                HomeScreen(navController = navController, viewModel = mainViewModel)
            }

            composable(Routes.Profile.route) {
                ProfileScreen(controller = navController, viewModel = mainViewModel)
            }

            composable(Routes.MyTournaments.route) {
                MyTournamentsScreen(navController = navController)
            }

            composable(Routes.MyPredictions.route) {
                MyPredictionsScreen(navController = navController)
            }
        }
    }
}
