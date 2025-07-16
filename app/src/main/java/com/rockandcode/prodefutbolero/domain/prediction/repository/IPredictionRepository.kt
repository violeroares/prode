package com.rockandcode.prodefutbolero.domain.prediction.repository

import com.rockandcode.prodefutbolero.domain.pagination.PageResult
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter

interface IPredictionRepository {
    suspend fun getHitsToPage(
        filter: HitFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Hit>
}
