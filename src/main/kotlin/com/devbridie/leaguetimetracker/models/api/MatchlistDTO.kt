package com.devbridie.leaguetimetracker.models.api


data class MatchlistDTO(
    val matches: List<MatchReferenceDTO>,
    val totalGames: Int,
    val startIndex: Int,
    val endIndex: Int
)