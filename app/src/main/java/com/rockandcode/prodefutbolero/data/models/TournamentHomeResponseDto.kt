package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome

data class TournamentHomeResponseDto(
    val tournamentId: Int,
    val tournamentName: String,
    val tournamentPictureUrl: String,
    val myPosition: String,
    val myPoints: String,
    val leaderPoints: String,
    val leaderName: String,
    val leaderPictureUrl: String,
    val sinPronostico: String,
    val activeDateId: String,
    val activeDateName: String,
    val matches: List<MatchDto>,
    val averageByDate: List<AverageDto>,
) {
    fun toDomain() =
        TournamentHome(
            tournamentId = this.tournamentId,
            tournamentName = this.tournamentName,
            tournamentPictureUrl = this.tournamentPictureUrl,
            myPosition = this.myPosition,
            myPoints = this.myPoints,
            leaderPoints = this.leaderPoints,
            leaderName = this.leaderName,
            leaderPictureUrl = this.leaderPictureUrl,
            sinPronostico = this.sinPronostico,
            activeDateId = this.activeDateId,
            activeDateName = this.activeDateName,
            matches = this.matches.map { it.toDomain() },
            averageByDate = this.averageByDate.map { it.toDomain() },
        )
}
