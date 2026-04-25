package com.example.sportsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sportsapp.data.Match
import androidx.lifecycle.viewModelScope
import com.example.sportsapp.database.AppDatabase
import com.example.sportsapp.database.FavoriteMatch
import kotlinx.coroutines.launch
import com.example.sportsapp.network.RetrofitInstance

class MatchViewModel : ViewModel() {

    var favorites = mutableStateOf<Set<String>>(emptySet())

    var matches = mutableStateOf<List<Match>>(emptyList())

    var selectedMatch = mutableStateOf<Match?>(null)

    var isLoading = mutableStateOf(false)

    var errorMessage = mutableStateOf<String?>(null)

    fun selectMatch(match: Match) {
        selectedMatch.value = match
    }

    fun toggleFavorite(
        match: Match,
        db: AppDatabase
    ) {

        val id = match.idEvent ?: return

        viewModelScope.launch {

            if (favorites.value.contains(id)) {

                favorites.value =
                    favorites.value - id

                db.favoriteDao().deleteFavorite(
                    FavoriteMatch(
                        eventId = id,
                        eventName = match.strEvent ?: ""
                    )
                )

            } else {

                favorites.value =
                    favorites.value + id

                db.favoriteDao().insertFavorite(
                    FavoriteMatch(
                        eventId = id,
                        eventName = match.strEvent ?: ""
                    )
                )

            }

        }
    }

    fun isFavorite(match: Match): Boolean {
        val id = match.idEvent ?: return false
        return favorites.value.contains(id) //To prevent null issues
    }

    fun fetchMatches() {
        viewModelScope.launch {

            isLoading.value = true
            errorMessage.value = null

            try {
                val response = RetrofitInstance.api.getMatches()

                if (response.isSuccessful) {
                    matches.value = response.body()?.events ?: emptyList()
                } else {
                    errorMessage.value = "Failed: ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage.value = "Network error"
            }

            isLoading.value = false
        }
    }

    fun loadFavorites(db: AppDatabase) {

        viewModelScope.launch {

            val savedFavorites =
                db.favoriteDao().getFavorites()

            favorites.value =
                savedFavorites.map {
                    it.eventId
                }.toSet()

        }
    }
}