package com.rockandcode.prodefutbolero.ui.navigation

sealed class Routes(
    val route: String,
) {
    // Auth
    data object Login : Routes("login")

    data object Register : Routes("register")

    data object Forgot : Routes("forgot-password")

    // App
    data object TournamentSelect : Routes("tournaments")

    data object Home : Routes("home")

    data object MyPredictions : Routes("predictions")

    data object MyTournaments : Routes("mytournaments")

    data object Profile : Routes("profile")

    data object Matches : Routes("matches")

    data object Ranking : Routes("ranking")

    data object MyHits : Routes("hits")

    data object Inbox : Routes("inbox")

    data object ChangePassword : Routes("change-password")

    data object Instructions : Routes("instructions")
}
