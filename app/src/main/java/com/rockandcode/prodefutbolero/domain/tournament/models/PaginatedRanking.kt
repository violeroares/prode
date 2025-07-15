package com.rockandcode.prodefutbolero.domain.tournament.models

data class PaginatedRanking(
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val rankings: List<RankingUser>,
)
