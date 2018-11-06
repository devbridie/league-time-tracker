package com.devbridie.leaguetimetracker.models.static

import com.devbridie.leaguetimetracker.gson
import java.io.File
import java.net.URL


private val file = File(File("output"), "champions.json").also { file ->
    if (!file.exists()) {
        val url = "http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion.json"
        URL(url).openStream().use { instream ->
            file.outputStream().use { outstream ->
                instream.copyTo(outstream)
                println("Downloaded $url to ${file.absolutePath}")
            }
        }
    }
}

private val champions = gson.fromJson<Champions>(
    file.readText(), Champions::class.java)

fun getChampionName(id: Int): String {
    return champions.data.entries.find { it.value.key == id.toString() }!!.key
}

data class Champions(
    val data: Map<String, Champion>
)

data class Champion(
    val key: String,
    val name: String
)

