package com.rockandcode.prodefutbolero.domain.tournament.repository

import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import kotlinx.coroutines.flow.Flow

interface ITournamentRepository {
    fun getTournaments(): Flow<List<Tournament>>

    suspend fun getTournamentHome(tournamentId: Int): TournamentHome
}
