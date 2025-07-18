package com.rockandcode.prodefutbolero.data.repositories

import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TournamentRepository(
    private val apiService: ApiService,
) : ITournamentRepository {
    override fun getTournaments(): Flow<List<Tournament>> =
        flow {
            val response = apiService.getTournaments()
            emit(response.map { it.toDomain() })
        }.flowOn(Dispatchers.IO)

    override suspend fun getDates(tournamentId: String): List<MatchDate> = apiService.getDates(tournamentId).map { it.toDomain() }
}
