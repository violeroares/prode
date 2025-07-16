package com.rockandcode.prodefutbolero.domain.prediction.models

data class HitFilter(
    val predictionId: String? = null,
    val boardId: String? = null,
    val userId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val userName: String? = null,
    val matchId: String? = null,
    val dateId: String? = null,
    val tournamentId: String? = null,
    val groupId: String? = null,
    val localId: String? = null,
    val visitorId: String? = null,
    val statusMatchId: String? = null,
    val localGoals: String? = null,
    val visitorGoals: String? = null,
    val fechaDesde: String? = null,
    val fechaHasta: String? = null,
    val teamName: String? = null,
    val teamId: String? = null,
)
