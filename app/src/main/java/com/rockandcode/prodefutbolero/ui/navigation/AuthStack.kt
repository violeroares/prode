package com.rockandcode.prodefutbolero.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rockandcode.prodefutbolero.ui.screens.ForgotPasswordScreen
import com.rockandcode.prodefutbolero.ui.screens.LoginScreen
import com.rockandcode.prodefutbolero.ui.screens.RegisterScreen

@Composable
fun AuthStack(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.TournamentSelect.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.Register.route) },
                onGoToForgot = { navController.navigate(Routes.Forgot.route) },
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Forgot.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }
    }
}
