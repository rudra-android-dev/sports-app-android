package com.example.sportsapp.network

import retrofit2.Response
import retrofit2.http.GET
import com.example.sportsapp.data.MatchResponse

interface ApiService {

    @GET("api/v1/json/3/eventsnextleague.php?id=4328")
    suspend fun getMatches(): Response<MatchResponse>
}