package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament

data class TournamentResponseDto(
    @SerializedName("TournamentId")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("TournamentTypeName")
    val tournamentTypeName: String,
    @SerializedName("PictureUrl")
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
