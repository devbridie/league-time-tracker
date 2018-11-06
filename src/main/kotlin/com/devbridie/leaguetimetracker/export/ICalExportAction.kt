package com.devbridie.leaguetimetracker.export

import com.devbridie.leaguetimetracker.API
import com.devbridie.leaguetimetracker.logger
import com.devbridie.leaguetimetracker.models.api.MatchReferenceDTO
import com.devbridie.leaguetimetracker.models.api.MatchlistDTO
import com.devbridie.leaguetimetracker.models.api.PartialMatchDTO
import com.devbridie.leaguetimetracker.models.static.getChampionName
import com.devbridie.leaguetimetracker.models.static.queues
import kotlinx.coroutines.delay
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.util.MapTimeZoneCache
import java.io.File
import java.util.concurrent.TimeUnit


class ICalExportAction(val file: File, val api: API, val summonerName: String) : ExportAction {
    private fun createCalendar() =
        if (file.exists()) {
            System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache::class.java.name)
            CalendarBuilder().build(file.inputStream()).also {
                logger.info("Read ${it.getEvents().size} events from ${file.absolutePath}")
            }
        } else {
            createEmptyCalendar().also { println("Creating new calendar.") }
        }

    override suspend fun export(matchList: MatchlistDTO) {
        val calendar = createCalendar()
        try {
            matchList.matches.forEach { match ->
                if (calendar.containsMatch(match)) {
                    logger.info("Skipping game with id = ${match.gameId} because it was already in the calendar.")
                } else {
                    calendar.addGameEntry(api.getMatch(match), summonerName)
                    logger.info("Added match with id = ${match.gameId}")
                    delay(1500, TimeUnit.MILLISECONDS)
                }
            }
        } catch (e: Exception) {
            throw e
        } finally {
            file.writeText(calendar.toString())
            logger.info ("Wrote results to ${file.absolutePath}")
        }
    }
}

private fun Calendar.getEvents(): List<VEvent> = components.mapNotNull { it as? VEvent }
private fun Calendar.containsMatch(match: MatchReferenceDTO): Boolean =
    getEvents().any { it.uid.value == match.gameId.toString() }

private fun createEmptyCalendar(): Calendar {
    return Calendar().apply {
        properties.add(ProdId("-//Chain CC//League Tracker//EN"))
        properties.add(Version.VERSION_2_0)
        properties.add(CalScale.GREGORIAN)
    }
}

private fun getMyChampionId(entry: PartialMatchDTO, meName: String): Int {
    val participantId = entry.participantIdentities.find { it.player.summonerName == meName }!!.participantId
    return entry.participants.find { it.participantId == participantId }!!.championId
}

private fun Calendar.addGameEntry(entry: PartialMatchDTO, meName: String) {
    val name = getChampionName(getMyChampionId(entry, meName))
    val queue = queues.find { it.id == entry.queueId }!!
    val vEvent = VEvent(
        DateTime(entry.gameCreation),
        DateTime(entry.gameCreation + entry.gameDuration * 1000),
        "Played as $name (${queue.name} on ${queue.map})"
    )
    vEvent.properties.add(Uid(entry.gameId.toString()))
    this.components.add(vEvent)
}