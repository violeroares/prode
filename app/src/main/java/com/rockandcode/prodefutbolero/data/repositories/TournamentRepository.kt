package com.rockandcode.prodefutbolero.data.repositories

import android.util.Log
import com.rockandcode.prodefutbolero.data.datasources.network.ApiService
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
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
}
