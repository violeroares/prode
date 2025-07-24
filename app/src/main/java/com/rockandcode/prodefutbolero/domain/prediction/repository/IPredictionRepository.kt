package com.rockandcode.prodefutbolero.domain.prediction.repository

import com.rockandcode.prodefutbolero.domain.pagination.PageResult
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter
import com.rockandcode.prodefutbolero.domain.prediction.models.Prediction
import com.rockandcode.prodefutbolero.domain.prediction.models.PredictionSummary
import com.rockandcode.prodefutbolero.domain.prediction.models.Ranking
import com.rockandcode.prodefutbolero.domain.prediction.models.RankingFilter
import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate

interface IPredictionRepository {
    suspend fun getHitsToPage(
        filter: HitFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Hit>

    suspend fun getAverageByUser(tournamentId: Int?): List<AverageByDate>

    suspend fun getPrediccionesIncompletas(
        userId: Int? = null,
        tournamentId: Int,
        dateId: Int? = null,
    ): Int

    suspend fun getPredictionSummary(
        userId: String,
        dateId: String,
    ): PredictionSummary

    suspend fun getRankingToPage(
        filter: RankingFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Ranking>

    suspend fun getPredictionsApp(
        userId: String,
        tournamentId: String,
        dateId: String?,
    ): List<Prediction>
}
