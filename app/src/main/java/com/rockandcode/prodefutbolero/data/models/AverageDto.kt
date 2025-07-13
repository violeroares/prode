package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate

class AverageDto(
    val number: Int,
    // val dateId: Int,
    val name: String,
    val points: Int,
) {
    fun toDomain() =
        AverageByDate(
            number = this.number,
            // dateId = this.dateId,
            name = this.name,
            points = this.points,
        )
}
