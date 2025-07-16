package com.rockandcode.prodefutbolero.domain.prediction.repository

import com.rockandcode.prodefutbolero.domain.pagination.PageResult
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter
import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate

interface IPredictionRepository {
    suspend fun getHitsToPage(
        filter: HitFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Hit>

    suspend fun getAverageByUser(tournamentId: Int?): List<AverageByDate>
}
