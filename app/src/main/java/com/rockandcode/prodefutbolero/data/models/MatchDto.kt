package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.Match

data class MatchDto(
    val matchId: Int,
    val groupId: Int?,
    val groupName: String,
    val localId: Int,
    val visitorId: Int,
    val statusId: Int,
    val predictions: Int,
    val localGoals: Int?,
    val visitorGoals: Int?,
    val date: String,
    val fechaId: Int?,
    val localName: String,
    val localPictureUrl: String,
    val visitorName: String,
    val visitorPictureUrl: String,
) {
    fun toDomain() =
        Match(
            matchId = this.matchId,
            groupId = this.groupId,
            groupName = this.groupName,
            localId = this.localId,
            visitorId = this.visitorId,
            predictions = this.predictions,
            statusMatchId = this.statusId,
            localGoals = this.localGoals,
            visitorGoals = this.visitorGoals,
            date = this.date,
            fechaId = this.fechaId,
            localName = this.localName,
            localPictureUrl = this.localPictureUrl,
            visitorName = this.visitorName,
            visitorPictureUrl = this.visitorPictureUrl,
        )
}
