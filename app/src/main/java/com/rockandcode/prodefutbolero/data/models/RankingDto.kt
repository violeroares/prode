package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.prediction.models.Ranking

data class RankingDto(
    @SerializedName("Posicion")
    val posicion: Int,
    @SerializedName("UserId")
    val userId: Int,
    @SerializedName("Username")
    val userName: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("Puntos")
    val puntos: Int,
    @SerializedName("UserPictureUrl")
    val userPictureUrl: String,
) {
    fun toDomain(): Ranking =
        Ranking(
            userId = userId,
            userName = userName,
            firstName = firstName,
            lastName = lastName,
            posicion = posicion,
            puntos = puntos,
            userPictureUrl = userPictureUrl,
        )
}
