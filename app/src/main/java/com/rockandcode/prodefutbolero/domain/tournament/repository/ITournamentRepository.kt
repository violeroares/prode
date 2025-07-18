package com.rockandcode.prodefutbolero.domain.tournament.repository

import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import kotlinx.coroutines.flow.Flow

interface ITournamentRepository {
    fun getTournaments(): Flow<List<Tournament>>

    suspend fun getDates(tournamentId: String): List<MatchDate>
}
