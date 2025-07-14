package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate

data class MatchDateDto(
    val dateId: Int,
    val name: String,
    val active: Boolean,
) {
    fun toDomain() =
        MatchDate(
            id = this.dateId,
            name = this.name,
            active = this.active,
        )
}
