package com.example.sportsapp.ui.main

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.navigation.NavController
import com.example.sportsapp.viewmodel.MatchViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.*
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    val scope =
        rememberCoroutineScope()

    val filteredMatches = viewModel.matches.value.filter {
        it.strEvent?.contains(searchQuery, ignoreCase = true) == true
    }

    LaunchedEffect(Unit) {
        if (viewModel.matches.value.isEmpty()) {
            viewModel.fetchMatches()
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            Text(
                text = "Upcoming Matches",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },

                placeholder = {
                    Text("Search matches")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = {
                    navController.navigate("favorites")
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Text("View Favorites")
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
                            MatchItem(
                                match = match,

                                isFavorite =
                                    viewModel.isFavorite(match),

                                onFavoriteClick = {
                                    viewModel.toggleFavorite(match)
                                },

                                onReminderScheduled = {

                                    scope.launch {

                                        snackbarHostState.showSnackbar(
                                            "Reminder scheduled"
                                        )

                                    }

                                },

                                onClick = {
                                    viewModel.selectMatch(match)
                                    navController.navigate("details")
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}

