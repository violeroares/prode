package com.rockandcode.prodefutbolero.data.models

import com.rockandcode.prodefutbolero.domain.tournament.models.Match

data class MatchDto(
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
    val statusMatchName: String,
    val groupName: String,
    val dateName: String,
    val localPictureId: Int,
    val visitorPictureId: Int,
    val predictions: Int,
    val localPictureUrl: String,
    val visitorPictureUrl: String,
) {
    fun toDomain() =
        Match(
            matchId = this.matchId,
            groupId = this.groupId,
            localId = this.localId,
            visitorId = this.visitorId,
            statusMatchId = this.statusMatchId,
            localGoals = this.localGoals,
            visitorGoals = this.visitorGoals,
            date = this.date,
            dateId = this.dateId,
            localName = this.localName,
            visitorName = this.visitorName,
            displayOrder = this.displayOrder,
            statusMatchName = this.statusMatchName,
            groupName = this.groupName,
            dateName = this.dateName,
            localPictureId = this.localPictureId,
            visitorPictureId = this.visitorPictureId,
            predictions = this.predictions,
            localPictureUrl = this.localPictureUrl,
            visitorPictureUrl = this.visitorPictureUrl,
        )
}
