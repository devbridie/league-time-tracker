package com.devbridie.leaguetimetracker.models.api


data class PartialMatchDTO(
    val gameCreation: Long,
    val gameDuration: Long,
    val queueId: Int,
    val gameId: Long,
    val participants: List<ParticipantDTO>,
    val participantIdentities: List<ParticipantIdentityDTO>
)