package com.rockandcode.prodefutbolero.data.mappers

import com.rockandcode.prodefutbolero.data.models.MatchRequest
import com.rockandcode.prodefutbolero.domain.match.models.MatchFilter

fun MatchFilter.toRequest(): MatchRequest =
    MatchRequest(
        visitorId = visitorId,
        tournamentId = tournamentId,
        dateId = dateId,
        fechaDesde = fechaDesde,
        fechaHasta = fechaHasta,
        visitorGoals = visitorGoals,
        statusMatchId = statusMatchId,
        localGoals = localGoals,
        teamId = teamId,
        teamName = teamName,
        groupId = groupId,
        localId = localId,
        matchId = matchId,
    )
