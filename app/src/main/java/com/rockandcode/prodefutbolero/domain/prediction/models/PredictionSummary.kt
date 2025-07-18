package com.rockandcode.prodefutbolero.domain.prediction.models

data class PredictionSummary(
    val userPoints: Int,
    val maxPointsSoFar: Int,
    val totalMatches: Int,
    val remainingMatches: Int,
    val tournamentName: String,
    val dateName: String,
)
