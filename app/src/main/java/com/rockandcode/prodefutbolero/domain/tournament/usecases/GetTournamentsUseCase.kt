package com.rockandcode.prodefutbolero.domain.tournament.usecases

import com.rockandcode.prodefutbolero.domain.tournament.models.Tournament
import com.rockandcode.prodefutbolero.domain.tournament.repository.ITournamentRepository
import kotlinx.coroutines.flow.Flow

class GetTournamentsUseCase(
    private val repo: ITournamentRepository,
) {
    operator fun invoke(): Flow<List<Tournament>> = repo.getTournaments()
}
