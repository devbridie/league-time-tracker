package com.devbridie.leaguetimetracker

import com.devbridie.leaguetimetracker.export.ExportAction
import com.devbridie.leaguetimetracker.export.ICalExportAction
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import java.io.File

val logger = LoggerFactory.getLogger("League Tracker")

suspend fun main(args: Array<String>) {
    val summonerName = Configuration.summonerName
    val api = API(Configuration.apiKey, Configuration.region)

    val file = File(File("output"), "calendar.ics")
    val method: ExportAction = ICalExportAction(file, api, summonerName)

    try {
        val summoner = api.getSummoner(summonerName)
        val matchList = api.getMatchList(summoner)
        logger.info("Got the last ${matchList.matches.size} matches.")

        method.export(matchList)
    } catch (e: HttpException) {
        logger.error("Riot returned an exception! Continuing.", e)
    }
}