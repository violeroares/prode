package com.rockandcode.prodefutbolero.ui.navigation

sealed class Routes(
    val route: String,
) {
    // Auth
    object Login : Routes("login")

    object Register : Routes("register")

    object Forgot : Routes("forgot-password")

    object TournamentSelect : Routes("tournaments")

    object Home : Routes("home")

    object MyPredictions : Routes("predictions")

    object MyTournaments : Routes("mytournaments")

    object Profile : Routes("profile")

    object Matches : Routes("matches")

    object Ranking : Routes("ranking")
}
