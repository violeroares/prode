package com.rockandcode.prodefutbolero.domain.match.repository

import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.domain.match.models.MatchFilter
import com.rockandcode.prodefutbolero.domain.pagination.PageResult

interface IMatchRepository {
    suspend fun getMatchesToPage(
        filter: MatchFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Match>
}
