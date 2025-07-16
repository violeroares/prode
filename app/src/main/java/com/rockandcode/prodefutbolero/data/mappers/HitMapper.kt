package com.rockandcode.prodefutbolero.data.mappers

import com.rockandcode.prodefutbolero.data.models.HitRequest
import com.rockandcode.prodefutbolero.domain.prediction.models.HitFilter

fun HitFilter.toRequest(): HitRequest =

    HitRequest(
        predictionId = predictionId,
        boardId = boardId,
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        userName = userName,
        matchId = matchId,
        dateId = dateId,
        tournamentId = tournamentId,
        groupId = groupId,
        localId = localId,
        visitorId = visitorId,
        statusMatchId = statusMatchId,
        localGoals = localGoals,
        visitorGoals = visitorGoals,
        fechaDesde = fechaDesde,
        fechaHasta = fechaHasta,
        teamName = teamName,
        teamId = teamId,
    )
