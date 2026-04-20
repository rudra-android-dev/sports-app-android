package com.example.sportsapp.ui.main

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.navigation.NavController
import android.util.Log
import com.example.sportsapp.data.Match
import com.example.sportsapp.network.RetrofitInstance
import com.example.sportsapp.viewmodel.MatchViewModel

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {

    val matches = viewModel.matches.value
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getMatches()

            if (response.isSuccessful) {
                viewModel.matches.value = response.body()?.events ?: emptyList()
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
                    viewModel.selectMatch(match)
                    navController.navigate("details")
                }
            }
        }
    }
}