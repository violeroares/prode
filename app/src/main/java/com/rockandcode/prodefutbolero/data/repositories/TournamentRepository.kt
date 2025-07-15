package com.rockandcode.prodefutbolero.data.repositories

import android.util.Log
import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.data.models.PaginatedMatchesDto
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedMatches
import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedRanking
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate

class TournamentRepository(
    private val apiService: ApiService,
) : ITournamentRepository {
    override fun getTournaments(): Flow<List<Tournament>> =
        flow {
            val response = apiService.getTournaments()
            emit(response.map { it.toDomain() })
        }.flowOn(Dispatchers.IO)

    override suspend fun getTournamentHome(tournamentId: Int): TournamentHome {
        val response = apiService.getTournamentHome(tournamentId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body.toDomain()
            } else {
                Log.e("TournamentRepository", "Response body is null")
                throw Exception("Response body is null")
            }
        } else {
            throw Exception("Error fetching tournament home: ${response.code()}")
        }
    }

    override suspend fun getMatches(
        tournamentId: Int?,
        page: Int,
        size: Int,
        teamName: String?,
        dateId: Int?,
    ): PaginatedMatches {
        val response: PaginatedMatchesDto =
            apiService.getMatches(
                page = page,
                size = size,
                tournamentId = tournamentId,
                teamName = teamName,
                dateId = dateId,
            )

        val matches = response.matches.map { it.toDomain() }

        return PaginatedMatches(
            matches = matches,
            totalRecords = response.totalRecords,
            totalPages = response.totalPages,
            currentPage = response.currentPage,
        )
    }

    override suspend fun getDates(tournamentId: Int): List<MatchDate> = apiService.getDates(tournamentId).map { it.toDomain() }

    override suspend fun getRanking(
        tournamentId: Int,
        page: Int,
        size: Int,
        dateId: Int?,
        firstname: String?,
        lastname: String?,
        username: String?,
        posicion: Int?,
        fechaDesde: LocalDate?,
        fechaHasta: LocalDate?,
        userId: Int?,
    ): PaginatedRanking {
        val fechaDesdeStr = fechaDesde?.toString() // Ej: "2025-07-01"
        val fechaHastaStr = fechaHasta?.toString()

        val response =
            apiService.getRanking(
                page = page,
                size = size,
                tournamentId = tournamentId,
                dateId = dateId,
                firstname = firstname,
                lastname = lastname,
                username = username,
                posicion = posicion,
                fechaDesde = fechaDesdeStr,
                fechaHasta = fechaHastaStr,
                userId = userId,
            )

        val body = response.body()
        if (body != null) {
            return body.toDomain()
        } else {
            throw Exception("Cuerpo de la respuesta nulo")
        }
    }
}
