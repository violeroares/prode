package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament

data class TournamentResponseDto(
    @SerializedName("tournamentId")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("tournamentTypeName")
    val tournamentTypeName: String,
    @SerializedName("pictureUrl")
    val pictureUrl: String,
) {
    fun toDomain() =
        Tournament(
            id = id,
            name = name,
            tournamentTypeName = tournamentTypeName,
            pictureUrl = pictureUrl,
        )
}
