package com.example.sportsapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportsapp.viewmodel.MatchViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredMatches = viewModel.matches.value.filter {
        it.strEvent?.contains(searchQuery, ignoreCase = true) == true
    }

    LaunchedEffect(Unit) {
        if (viewModel.matches.value.isEmpty()) {
            viewModel.fetchMatches()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 20.dp)
            ) {
                Column {
                    Text(
                        text       = "Sport Scores",
                        style      = MaterialTheme.typography.labelLarge,
                        color      = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text       = "Upcoming Matches",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color      = MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(
                    onClick  = { navController.navigate("favorites") { launchSingleTop = true } },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Bookmarks,
                        contentDescription = "Favourites",
                        tint               = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            OutlinedTextField(
                value          = searchQuery,
                onValueChange  = { searchQuery = it },
                placeholder    = { Text("Search matches…") },
                leadingIcon    = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine     = true,
                shape          = RoundedCornerShape(28.dp),
                modifier       = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors         = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedBorderColor   = MaterialTheme.colorScheme.primary
                )
            )


            when {
                viewModel.isLoading.value -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text  = "Loading matches…",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                viewModel.errorMessage.value != null -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier            = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text  = "😕",
                                style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text  = viewModel.errorMessage.value ?: "Unknown error",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchMatches() }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                filteredMatches.isEmpty() && searchQuery.isNotEmpty() -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier            = Modifier.padding(24.dp)
                        ) {
                            Text(text = "🔍", style = MaterialTheme.typography.displaySmall)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text  = "No matches for \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    SwipeRefresh(
                        state     = rememberSwipeRefreshState(viewModel.isLoading.value),
                        onRefresh = { viewModel.fetchMatches() },
                        modifier  = Modifier.weight(1f)
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(filteredMatches) { match ->
                                MatchItem(
                                    match              = match,
                                    isFavorite         = viewModel.isFavorite(match),
                                    onFavoriteClick    = { viewModel.toggleFavorite(match) },
                                    onReminderScheduled = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Reminder set ✓")
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
}