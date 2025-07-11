package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament

data class TournamentResponseDto(
    @SerializedName("TournamentId")
    val tournamentId: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PictureId")
    val pictureId: Int,
    @SerializedName("Active")
    val active: Int,
    @SerializedName("TournamentTypeId")
    val tournamentTypeId: Int,
    @SerializedName("DisplayOrder")
    val displayOrder: Int,
    @SerializedName("Teams")
    val teams: Int,
    @SerializedName("Groups")
    val groups: Int,
    @SerializedName("Dates")
    val dates: Int,
    @SerializedName("PictureUrl")
    val pictureUrl: String,
) {
    fun toDomain() =
        Tournament(
            id = tournamentId,
            name = name,
            pictureUrl = pictureUrl,
        )
}
