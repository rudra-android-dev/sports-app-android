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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.*

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredMatches = viewModel.matches.value.filter {
        it.strEvent?.contains(searchQuery, ignoreCase = true) == true
    }

    val matches = viewModel.matches.value

    LaunchedEffect(Unit) {
        viewModel.fetchMatches()
    }


    Column {

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search matches...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                navController.navigate("favorites")
            }
        ) {
            Text("Favorites")
        }

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
                    items(filteredMatches) { match ->
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

