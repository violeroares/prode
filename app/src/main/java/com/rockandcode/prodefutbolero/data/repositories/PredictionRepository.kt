package com.rockandcode.prodefutbolero.data.repositories

import android.util.Log
import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.mappers.toRequest
import com.rockandcode.prodefutbolero.data.models.Pagination
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter
import com.rockandcode.prodefutbolero.domain.prediction.repository.IPredictionRepository
import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate
import retrofit2.HttpException
import com.rockandcode.prodefutbolero.domain.pagination.PageResult as DomainPageResult

class PredictionRepository(
    private val apiService: ApiService,
) : IPredictionRepository {
    override suspend fun getHitsToPage(
        filter: HitFilter,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): DomainPageResult<Hit> {
        val request = filter.toRequest()

        val pagination =
            Pagination(
                filter = request,
                pageIndex = pageIndex,
                pageSize = pageSize,
                sort = sort,
            )
        val response = apiService.getHitsToPage(pagination)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response body")

            return DomainPageResult(
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

    override suspend fun getAverageByUser(tournamentId: Int?): List<AverageByDate> =
        apiService.getAverageByUser(tournamentId).map {
            it.toDomain()
        }

    override suspend fun getPrediccionesIncompletas(
        userId: Int?,
        tournamentId: Int,
        dateId: Int?,
    ): Int {
        Log.d("AppStack", "Enviando user = $userId torneo = $tournamentId fecha = $dateId")
        val response =
            apiService.getPrediccionesIncompletas(
                userId = userId,
                tournamentId = tournamentId,
                dateId = dateId,
            )

        val body = response.body()
        if (body != null) {
            Log.d("AppStack", "Recibido cantidad = $body")
            return body
        } else {
            throw Exception("Error al cargar las predicciones incompletas")
        }
    }
}
