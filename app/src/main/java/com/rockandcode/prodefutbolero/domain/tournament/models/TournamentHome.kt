package com.rockandcode.prodefutbolero.domain.tournament.models

data class TournamentHome(
    val tournamentId: Int,
    val tournamentName: String,
    val tournamentPictureUrl: String,
//    val myPosition: String,
//    val myPoints: String,
//    val leaderPoints: String,
//    val leaderName: String,
//    val leaderPictureUrl: String,
    val sinPronostico: String,
    val activeDateId: String,
    val activeDateName: String,
    val matches: List<Match>,
    val averageByDate: List<AverageByDate>,
)
