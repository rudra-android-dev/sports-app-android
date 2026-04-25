package com.example.sportsapp.repository

import com.example.sportsapp.network.RetrofitInstance
import com.example.sportsapp.database.AppDatabase
import com.example.sportsapp.database.FavoriteMatch
import com.example.sportsapp.data.Match

class MatchRepository(
    private val db: AppDatabase
) {

    suspend fun fetchMatches() =
        RetrofitInstance.api.getMatches()

    suspend fun getFavorites() =
        db.favoriteDao().getFavorites()

    suspend fun insertFavorite(match: Match) {

        val id = match.idEvent ?: return

        db.favoriteDao().insertFavorite(
            FavoriteMatch(
                eventId = id,
                eventName = match.strEvent ?: ""
            )
        )
    }

    suspend fun deleteFavorite(match: Match) {

        val id = match.idEvent ?: return

        db.favoriteDao().deleteFavorite(
            FavoriteMatch(
                eventId = id,
                eventName = match.strEvent ?: ""
            )
        )
    }
}