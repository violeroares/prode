package com.rockandcode.prodefutbolero.data.repositories

import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.mappers.toRequest
import com.rockandcode.prodefutbolero.data.models.Pagination
import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.domain.match.models.MatchFilter
import com.rockandcode.prodefutbolero.domain.match.repository.IMatchRepository
import com.rockandcode.prodefutbolero.domain.pagination.PageResult
import retrofit2.HttpException

class MatchRepository(
    private val apiService: ApiService,
) : IMatchRepository {
    override suspend fun getMatchesToPage(
        filter: MatchFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Match> {
        val request = filter.toRequest()

        val pagination =
            Pagination(
                filter = request,
                pageIndex = pageIndex,
                pageSize = pageSize,
                sort = sort,
            )
        val response = apiService.getMatchesToPage(pagination)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response body")

            return PageResult(
                pageSize = body.pageSize,
                pageIndex = body.pageIndex,
                totalCount = body.totalCount,
                totalPages = body.totalPages,
                hasNextPage = body.hasNextPage,
                hasPreviousPage = body.hasPreviousPage,
                result = body.result.map { it.toDomain() },
            )
        } else {
            throw HttpException(response)
        }
    }
}
