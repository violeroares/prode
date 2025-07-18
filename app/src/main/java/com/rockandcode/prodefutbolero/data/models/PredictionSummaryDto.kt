package com.rockandcode.prodefutbolero.data.models

import com.google.gson.annotations.SerializedName
import com.rockandcode.prodefutbolero.domain.prediction.models.PredictionSummary

data class PredictionSummaryDto(
    @SerializedName("userPoints")
    val userPoints: Int,
    @SerializedName("maxPointsSoFar")
    val maxPointsSoFar: Int,
    @SerializedName("totalMatches")
    val totalMatches: Int,
    @SerializedName("remainingMatches")
    val remainingMatches: Int,
    @SerializedName("tournamentName")
    val tournamentName: String,
    @SerializedName("dateName")
    val dateName: String,
) {
    fun toDomain(): PredictionSummary =
        PredictionSummary(
            userPoints = userPoints,
            maxPointsSoFar = maxPointsSoFar,
            totalMatches = totalMatches,
            remainingMatches = remainingMatches,
            tournamentName = tournamentName,
            dateName = dateName,
        )
}
