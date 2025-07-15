package com.rockandcode.prodefutbolero.domain.tournament.models

data class RankingUser(
    val posicion: Int,
    val userId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val pictureUrl: String,
    val pictureId: Int,
    val puntos: Int,
)
