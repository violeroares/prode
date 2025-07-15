package com.rockandcode.prodefutbolero.domain.tournament.models

data class Ranking(
    val userId: Int,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val points: Int?,
    val userPictureUrl: String,
)
