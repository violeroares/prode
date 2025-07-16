package com.rockandcode.prodefutbolero.domain.tournament.models

import com.rockandcode.prodefutbolero.domain.match.models.Match

data class PaginatedMatches(
    val matches: List<Match>,
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
)
