package com.devbridie.leaguetimetracker

import com.devbridie.leaguetimetracker.models.static.Region


object ConfigurationExample { // rename to Configuration
    init { TODO("Should fill in the configuration options") } // remove this line
    val summonerName = "" // Change to the summoner name to track
    val region = Region.EUW // See [models/static/Region.kt]
    val apiKey = "" // get one from https://developer.riotgames.com/
}