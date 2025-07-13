package com.rockandcode.prodefutbolero.domain.tournament.usecases

import com.rockandcode.prodefutbolero.domain.tournament.models.TournamentHome
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository

class GetTournamentHomeUseCase(
    private val repo: ITournamentRepository,
) {
    suspend operator fun invoke(tournamentId: Int): TournamentHome = repo.getTournamentHome(tournamentId)
}
