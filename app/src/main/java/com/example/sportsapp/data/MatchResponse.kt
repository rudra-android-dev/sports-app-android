package com.example.sportsapp.data

data class MatchResponse(
    val events: List<Match>
)

data class Match(
    val strEvent: String?,
    val dateEvent: String?,
    val strHomeTeam: String?,
    val strAwayTeam: String?,
    val strHomeTeamBadge: String?,
    val strAwayTeamBadge: String?
)