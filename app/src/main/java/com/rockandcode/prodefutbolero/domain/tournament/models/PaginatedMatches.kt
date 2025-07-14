package com.rockandcode.prodefutbolero.domain.tournament.models

data class PaginatedMatches(
    val matches: List<Match>,
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
)
