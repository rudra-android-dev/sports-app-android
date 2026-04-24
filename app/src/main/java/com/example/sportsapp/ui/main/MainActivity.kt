package com.example.sportsapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sportsapp.ui.theme.SportsAppTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.sportsapp.data.Match
import com.example.sportsapp.network.RetrofitInstance
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sportsapp.viewmodel.MatchViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: MatchViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                composable("home") {
                    MatchListScreen(navController, viewModel)
                }

                composable("details") { backStackEntry ->
                    val match = backStackEntry.arguments?.getString("match")
                    MatchDetailScreen(viewModel)
                }
                composable("favorites") {
                    FavoritesScreen(navController, viewModel)
                }
            }
        }
    }
}
@Composable
fun MatchItem(
    match: Match,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Left side: Home team
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = match.strHomeTeamBadge,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(match.strHomeTeam ?: "Team A")
                }

                // Center: VS
                Text(
                    text = "VS",
                    fontWeight = FontWeight.Bold
                )

                // Right side: Away team
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(match.strAwayTeam ?: "Team B")

                    Spacer(modifier = Modifier.width(8.dp))

                    AsyncImage(
                        model = match.strAwayTeamBadge,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(
                    onClick = {
                        onFavoriteClick()
                    }
                ) {
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Not Favorite"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = match.dateEvent ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
