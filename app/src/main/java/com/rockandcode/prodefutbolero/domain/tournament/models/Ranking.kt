package com.rockandcode.prodefutbolero.domain.tournament.models

data class Ranking(
    val posicion: Int,
    val userId: Int,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val puntos: Int,
    val userPictureUrl: String,
)
