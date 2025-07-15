package com.rockandcode.prodefutbolero.domain.tournament.repository

import com.rockandcode.prodefutbolero.domain.tournament.models.MatchDate
import com.rockandcode.prodefutbolero.domain.tournament.models.PageResult
import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedMatches
import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedRanking
import com.rockandcode.prodefutbolero.domain.tournament.models.Ranking
import com.rockandcode.prodefutbolero.domain.tournament.models.RankingRequest
import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    suspend fun getRanking(
        tournamentId: Int,
        page: Int,
        size: Int = 50,
        dateId: Int? = null,
        firstname: String? = null,
        lastname: String? = null,
        username: String? = null,
        posicion: Int? = null,
        fechaDesde: LocalDate? = null,
        fechaHasta: LocalDate? = null,
        userId: Int? = null,
    ): PaginatedRanking

    suspend fun getRankingToPage(
        filter: RankingRequest,
        pageIndex: Int,
        pageSize: Int,
        sort: String,
    ): PageResult<Ranking>
}
