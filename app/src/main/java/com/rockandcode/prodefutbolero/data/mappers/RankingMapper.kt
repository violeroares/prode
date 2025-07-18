package com.rockandcode.prodefutbolero.data.mappers

import com.rockandcode.prodefutbolero.data.models.RankingRequest
import com.rockandcode.prodefutbolero.domain.prediction.models.RankingFilter

fun RankingFilter.toRequest(): RankingRequest =
    RankingRequest(
        userId = userId,
        tournamentId = tournamentId,
        dateId = dateId,
        firstName = firstName,
        lastName = lastName,
        userName = userName,
        fechaDesde = fechaDesde,
        fechaHasta = fechaHasta,
        posicion = posicion,
    )
