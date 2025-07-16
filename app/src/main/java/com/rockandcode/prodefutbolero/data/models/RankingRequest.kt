package com.rockandcode.prodefutbolero.data.models

data class RankingRequest(
    val boardId: String? = null,
    val userId: String? = null,
    val tournamentId: String? = null,
    val dateId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val userName: String? = null,
    val posicion: String? = null,
    val fechaDesde: String? = null, // formato ISO "yyyy-MM-dd" si es necesario
    val fechaHasta: String? = null,
)
