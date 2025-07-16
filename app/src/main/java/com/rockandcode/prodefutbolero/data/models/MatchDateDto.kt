package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate

data class MatchDateDto(
    @SerializedName("key")
    val dateId: String,
    @SerializedName("label")
    val name: String,
    @SerializedName("selected")
    val active: String,
) {
    fun toDomain() =
        MatchDate(
            id = this.dateId.toInt(),
            name = this.name,
            active = this.active == "1",
        )
}
