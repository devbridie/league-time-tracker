package com.devbridie.leaguetimetracker.models.api

data class MatchReferenceDTO(
    val lane: String,
    val gameId: Long,
    val champion: Int,
    val platformId: String,
    val season: Int,
    val queue: Int,
    val role: String,
    val timestamp: Long
)
