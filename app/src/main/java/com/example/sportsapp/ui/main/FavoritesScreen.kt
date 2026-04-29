package com.example.sportsapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            Card(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "⭐ No favorites yet",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Star matches to save them here."
                    )

                }
            }
        }

    } else {

        Column {

            Text(
                text = "Favorite Matches",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

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
}