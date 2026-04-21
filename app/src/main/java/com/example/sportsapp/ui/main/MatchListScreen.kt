package com.example.sportsapp.ui.main

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.navigation.NavController
import android.util.Log
import com.example.sportsapp.data.Match
import com.example.sportsapp.network.RetrofitInstance
import com.example.sportsapp.viewmodel.MatchViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import com.google.accompanist.swiperefresh.*

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {

    val matches = viewModel.matches.value
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchMatches()
    }

    if (isLoading) {
        Text("Loading...")
    } else if (matches.isEmpty()) {
        Text("No matches available")
    } else {
        if (viewModel.isLoading.value) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (viewModel.errorMessage.value != null) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.errorMessage.value ?: "",
                    color = Color.Red
                )
            }

        } else {

            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.isLoading.value),
                onRefresh = { viewModel.fetchMatches() }
            ) {
                LazyColumn {
                    items(viewModel.matches.value) { match ->
                        MatchItem(match) {
                            viewModel.selectMatch(match)
                            navController.navigate("details")
                        }
                    }
                }
            }

        }
    }
}