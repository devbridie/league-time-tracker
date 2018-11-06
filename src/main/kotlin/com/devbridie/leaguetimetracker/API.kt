package com.devbridie.leaguetimetracker

import com.devbridie.leaguetimetracker.models.api.MatchReferenceDTO
import com.devbridie.leaguetimetracker.models.api.MatchlistDTO
import com.devbridie.leaguetimetracker.models.api.PartialMatchDTO
import com.devbridie.leaguetimetracker.models.api.SummonerDTO
import com.devbridie.leaguetimetracker.models.static.Region
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private interface DeveloperAPI {
    @GET("summoner/v3/summoners/by-name/{summonerName}")
    fun getSummonerByName(@Path("summonerName") summonerName: String, @Query("api_key") apiKey: String): Deferred<SummonerDTO>

    @GET("match/v3/matchlists/by-account/{accountId}")
    fun getMatchListByAccount(@Path("accountId") accountId: Long, @Query("beginIndex") beginIndex: Int, @Query("api_key") apiKey: String): Deferred<MatchlistDTO>

    @GET("match/v3/matches/{matchId}")
    fun getMatchById(@Path("matchId") matchId: Long, @Query("api_key") apiKey: String): Deferred<PartialMatchDTO>
}

class API(val key: String, val region: Region) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://${region.host}/lol/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    private val api = retrofit.create(DeveloperAPI::class.java)

    suspend fun getMatch(match: MatchReferenceDTO) =
        api.getMatchById(match.gameId, key).await()

    suspend fun getMatchList(summoner: SummonerDTO, beginIndex: Int = 0) =
        api.getMatchListByAccount(summoner.accountId, beginIndex, key).await()

    suspend fun getSummoner(summonerName: String) = api.getSummonerByName(summonerName, key).await()
}