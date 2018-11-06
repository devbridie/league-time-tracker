package com.devbridie.leaguetimetracker.models.api


data class SummonerDTO(
    val profileIconId: Int,
    val name: String,
    val summonerLevel: Long,
    val revisionDate: Long,
    val id: Long,
    val accountId: Long
)