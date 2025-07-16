package com.rockandcode.prodefutbolero.domain.match.models

data class Match(
    val matchId: Int,
    val groupId: Int,
    val localId: Int,
    val visitorId: Int,
    val statusMatchId: Int,
    val localGoals: Int?,
    val visitorGoals: Int?,
    val date: String,
    val dateId: Int,
    val localName: String,
    val visitorName: String,
    val displayOrder: Int,
    val groupName: String,
    val dateName: String,
    val localPictureId: Int,
    val visitorPictureId: Int,
    val predictions: Int,
    val localPictureUrl: String,
    val visitorPictureUrl: String,
)
