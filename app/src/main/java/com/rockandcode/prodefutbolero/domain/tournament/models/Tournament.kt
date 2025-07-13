package com.rockandcode.prodefutbolero.domain.tournament.models

data class Tournament(
    val id: Int,
    val name: String,
    val tournamentTypeName: String,
    val pictureUrl: String,
)
