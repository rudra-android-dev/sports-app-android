package com.example.sportsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sportsapp.data.Match
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.sportsapp.network.RetrofitInstance

class MatchViewModel : ViewModel() {

    var matches = mutableStateOf<List<Match>>(emptyList())

    var selectedMatch = mutableStateOf<Match?>(null)

    var isLoading = mutableStateOf(false)

    var errorMessage = mutableStateOf<String?>(null)

    fun selectMatch(match: Match) {
        selectedMatch.value = match
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
}