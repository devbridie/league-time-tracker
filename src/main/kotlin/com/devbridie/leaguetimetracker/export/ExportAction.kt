package com.devbridie.leaguetimetracker.export

import com.devbridie.leaguetimetracker.models.api.MatchlistDTO


interface ExportAction {
    suspend fun export(matchList: MatchlistDTO)
}