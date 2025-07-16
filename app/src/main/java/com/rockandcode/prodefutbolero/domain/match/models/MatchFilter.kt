package com.rockandcode.prodefutbolero.domain.match.models

data class MatchFilter(
    val matchId: String? = null,
    val tournamentId: String? = null,
    val groupId: String? = null,
    val localId: String? = null,
    val visitorId: String? = null,
    val fechaDesde: String? = null,
    val fechaHasta: String? = null,
    val statusMatchId: String? = null,
    val localGoals: String? = null,
    val visitorGoals: String? = null,
    val teamName: String? = null,
    val dateId: String? = null,
    val teamId: String? = null,
)
