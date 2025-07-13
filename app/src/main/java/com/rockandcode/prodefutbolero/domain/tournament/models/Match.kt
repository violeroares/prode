package com.rockandcode.prodefutbolero.domain.tournament.models

data class Match(
    val matchId: Int,
    val groupId: Int?,
    val groupName: String,
    val localId: Int,
    val visitorId: Int,
    val statusMatchId: Int,
    val predictions: Int,
    val localGoals: Int?,
    val visitorGoals: Int?,
    val date: String,
    val fechaId: Int?,
    val localName: String,
    val localPictureUrl: String,
    val visitorName: String,
    val visitorPictureUrl: String,
)
