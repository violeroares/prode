package com.rockandcode.prodefutbolero.domain.tournament.repository

import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedMatches
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import kotlinx.coroutines.flow.Flow

interface ITournamentRepository {
    fun getTournaments(): Flow<List<Tournament>>

    suspend fun getTournamentHome(tournamentId: Int): TournamentHome

    suspend fun getMatches(
        tournamentId: Int?,
        page: Int,
        size: Int = 10,
        teamName: String? = null,
        dateId: Int?,
    ): PaginatedMatches

    suspend fun getDates(tournamentId: Int): List<MatchDate>
}
