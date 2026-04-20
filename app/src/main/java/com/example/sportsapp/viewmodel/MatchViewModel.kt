package com.example.sportsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sportsapp.data.Match

class MatchViewModel : ViewModel() {

    var matches = mutableStateOf<List<Match>>(emptyList())

    var selectedMatch = mutableStateOf<Match?>(null)

    fun selectMatch(match: Match) {
        selectedMatch.value = match
    }
}