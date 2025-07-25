package com.rockandcode.prodefutbolero.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rockandcode.prodefutbolero.ui.components.FloatingBottomNavigationBar
import com.rockandcode.prodefutbolero.ui.screens.ChangePasswordScreen
import com.rockandcode.prodefutbolero.ui.screens.EditPredictionScreen
import com.rockandcode.prodefutbolero.ui.screens.HomeScreen
import com.rockandcode.prodefutbolero.ui.screens.InboxScreen
import com.rockandcode.prodefutbolero.ui.screens.InstructionsScreen
import com.rockandcode.prodefutbolero.ui.screens.MainViewModel
import com.rockandcode.prodefutbolero.ui.screens.MatchesScreen
import com.rockandcode.prodefutbolero.ui.screens.MyHitsScreen
import com.rockandcode.prodefutbolero.ui.screens.MyPredictionsScreen
import com.rockandcode.prodefutbolero.ui.screens.MyPredictionsViewModel
import com.rockandcode.prodefutbolero.ui.screens.MyTournamentsScreen
import com.rockandcode.prodefutbolero.ui.screens.ProfileScreen
import com.rockandcode.prodefutbolero.ui.screens.RankingScreen
import com.rockandcode.prodefutbolero.ui.screens.TournamentsScreen

@Composable
fun AppStack(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val incompletas by mainViewModel.prediccionesIncompletas.collectAsState()
    val predictionsViewModel: MyPredictionsViewModel = hiltViewModel()

    val bottomBarRoutes =
        listOf(
            Routes.Home.route,
            Routes.Ranking.route,
            Routes.Matches.route,
            Routes.Profile.route,
            // Routes.MyPredictions.route,
        )

    val showBottomBar = currentRoute in bottomBarRoutes
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
//            bottomBar = {
//                if (showBottomBar) {
//                    FloatingBottomNavigationBar(
//                        navController = navController,
//                        incompleteCount = incompletas,
//                    )
//                }
//            },
//            bottomBar = {
//                if (showBottomBar) {
//                    BottomBar(navController = navController, incompleteCount = incompletas)
//                }
//            },
            contentWindowInsets = WindowInsets(0),
            containerColor = Color.Transparent,
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.TournamentSelect.route,
                Modifier.padding(innerPadding),
            ) {
                composable(Routes.TournamentSelect.route) {
                    TournamentsScreen(
                        viewModel = mainViewModel,
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

                composable(Routes.Matches.route) {
                    MatchesScreen(mainViewModel = mainViewModel, navController = navController)
                }

                composable(Routes.Ranking.route) {
                    RankingScreen(mainViewModel = mainViewModel, navController = navController)
                }

                composable(Routes.MyHits.route) {
                    MyHitsScreen(mainViewModel = mainViewModel, navController = navController)
                }

                composable(Routes.Inbox.route) {
                    InboxScreen()
                }

                composable(Routes.ChangePassword.route) {
                    ChangePasswordScreen(
                        mainViewModel = mainViewModel,
                        navController = navController,
                    )
                }

                composable(Routes.Instructions.route) {
                    InstructionsScreen(navController = navController)
                }

                composable(Routes.MyPredictions.route) {
                    MyPredictionsScreen(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        viewModel = predictionsViewModel,
                    )
                }

                composable(Routes.PredictionEdit.route) {
                    val prediction by predictionsViewModel.selectedPrediction.collectAsState()

                    prediction?.let {
                        EditPredictionScreen(
                            prediction = it,
                            onBack = { navController.popBackStack() },
                            onSave = { localGoals, visitorGoals ->
                                predictionsViewModel.updatePredictionGoals(it.predictionId, localGoals, visitorGoals)
                            },
                        )
                    }
                }
            }
        }
        // BottomBar flotante sobre el contenido
        if (showBottomBar) {
            FloatingBottomNavigationBar(
                navController = navController,
                incompleteCount = incompletas,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
