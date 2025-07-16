package com.rockandcode.prodefutbolero.domain.pagination

data class PageResult<T>(
    val pageSize: Int,
    val pageIndex: Int,
    val totalCount: Int,
    val totalPages: Int,
    val result: List<T>,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
)
