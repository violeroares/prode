package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.Ranking

data class RankingDto(
    val boardId: String,
    val userId: Int,
    val tournamentId: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val points: Int?,
    val userPictureUrl: String,
) {
    fun toDomain(): Ranking =
        Ranking(
            userId = userId,
            userName = userName,
            firstName = firstName,
            lastName = lastName,
            position = position,
            points = points,
            userPictureUrl = userPictureUrl,
        )
}
