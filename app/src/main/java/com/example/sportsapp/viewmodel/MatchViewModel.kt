package com.example.sportsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sportsapp.data.Match
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.sportsapp.repository.MatchRepository

class MatchViewModel(
    private val repository: MatchRepository
) : ViewModel() {

    var favorites = mutableStateOf<Set<String>>(emptySet())

    var matches = mutableStateOf<List<Match>>(emptyList())

    var selectedMatch = mutableStateOf<Match?>(null)

    var isLoading = mutableStateOf(false)

    var errorMessage = mutableStateOf<String?>(null)

    fun selectMatch(match: Match) {
        selectedMatch.value = match
    }

    fun toggleFavorite(
        match: Match
    ) {

        val id = match.idEvent ?: return

        viewModelScope.launch {

            if (favorites.value.contains(id)) {

                favorites.value =
                    favorites.value - id

                repository.deleteFavorite(match)

            } else {

                favorites.value =
                    favorites.value + id

                repository.insertFavorite(match)

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

                val response =
                    repository.fetchMatches()

                if (response.isSuccessful) {

                    matches.value =
                        response.body()?.events ?: emptyList()

                } else {

                    errorMessage.value =
                        "Failed: ${response.code()}"
                }

            } catch (e: Exception) {

                errorMessage.value =
                    "Network error"

            }

            finally {
                isLoading.value = false
            }
        }
    }

    fun loadFavorites() {

        viewModelScope.launch {

            val savedFavorites =
                repository.getFavorites()

            favorites.value =
                savedFavorites.map {
                    it.eventId
                }.toSet()

        }

    }
}