package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.RankingUser

data class RankingResultDto(
    val posicion: Int,
    val userId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val pictureId: Int,
    val pictureUrl: String,
    val puntos: Int,
) {
    fun toDomain(): RankingUser =
        RankingUser(
            posicion = posicion,
            userId = userId,
            username = username,
            firstName = firstName,
            lastName = lastName,
            pictureId = pictureId,
            pictureUrl = pictureUrl,
            puntos = puntos,
        )
}
