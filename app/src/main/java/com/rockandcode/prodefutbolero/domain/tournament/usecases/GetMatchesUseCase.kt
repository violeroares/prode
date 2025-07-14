package com.rockandcode.prodefutbolero.domain.tournament.usecases

import com.rockandcode.prodefutbolero.domain.tournament.models.PaginatedMatches
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository

class GetMatchesUseCase(
    private val repository: ITournamentRepository,
) {
    suspend operator fun invoke(
        tournamentId: Int?,
        page: Int,
        size: Int = 10,
        teamName: String? = null,
        dateId: Int? = null,
    ): PaginatedMatches = repository.getMatches(tournamentId, page, size, teamName, dateId)
}
