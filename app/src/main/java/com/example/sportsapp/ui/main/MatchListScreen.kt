package com.example.sportsapp.ui.main

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.navigation.NavController
import android.util.Log
import com.example.sportsapp.data.Match
import com.example.sportsapp.network.RetrofitInstance

@Composable
fun MatchListScreen(navController: NavController) {

    var matches by remember { mutableStateOf<List<Match>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getMatches()

            if (response.isSuccessful) {
                matches = response.body()?.events ?: emptyList()
            }

        } catch (e: Exception) {
            Log.e("API_ERROR", e.message.toString())
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Text("Loading...")
    } else if (matches.isEmpty()) {
        Text("No matches available")
    } else {
        LazyColumn {
            items(matches) { match ->
                MatchItem(match) {
                    navController.navigate("details/${match.strEvent ?: ""}")
                }
            }
        }
    }
}