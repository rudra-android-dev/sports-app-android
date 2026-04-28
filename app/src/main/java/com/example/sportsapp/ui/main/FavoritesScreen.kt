package com.example.sportsapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sportsapp.viewmodel.MatchViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: MatchViewModel
) {

    val favoriteMatches = viewModel.matches.value.filter { match ->
        match.idEvent != null &&
                viewModel.favorites.value.contains(match.idEvent)
    }

    if (favoriteMatches.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No favorites yet")
        }

    } else {

        LazyColumn {

            items(favoriteMatches) { match ->

                MatchItem(
                    match = match,
                    isFavorite = true,

                    onFavoriteClick = {
                        viewModel.toggleFavorite(match)
                    },

                    onReminderScheduled = { },

                    onClick = {
                        viewModel.selectMatch(match)
                        navController.navigate("details")
                    }
                )

            }

        }

    }
}