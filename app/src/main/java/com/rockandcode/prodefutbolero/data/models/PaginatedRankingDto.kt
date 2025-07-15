package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedRanking

data class PaginatedRankingDto(
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val rankings: List<RankingResultDto>,
) {
    fun toDomain(): PaginatedRanking =
        PaginatedRanking(
            totalRecords = totalRecords,
            totalPages = totalPages,
            currentPage = currentPage,
            rankings = rankings.map { it.toDomain() },
        )
}
