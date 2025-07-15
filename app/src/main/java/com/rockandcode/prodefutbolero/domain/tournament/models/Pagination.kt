package com.rockandcode.prodefutbolero.domain.tournament.models

data class Pagination<T>(
    val filter: T,
    val pageIndex: Int,
    val pageSize: Int,
    val sort: String,
)
