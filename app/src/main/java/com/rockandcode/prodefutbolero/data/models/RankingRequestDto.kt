package com.rockandcode.prodefutbolero.data.models

data class RankingRequestDto(
    val userId: Int? = null,
    val tournamentId: Int? = null,
    val dateId: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val userName: String? = null,
    val fechaDesde: String? = null, // formato ISO "yyyy-MM-dd" si es necesario
    val fechaHasta: String? = null,
    val posicion: Int? = null,
)
