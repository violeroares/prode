package com.rockandcode.prodefutbolero.data.models

data class PaginatedMatchesDto(
    val matches: List<MatchDto>,
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
)
