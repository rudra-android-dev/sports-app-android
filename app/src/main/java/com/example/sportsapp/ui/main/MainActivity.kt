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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sportsapp.data.Match
import com.example.sportsapp.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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


            SportsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoading) {
                        Text(
                            "Loading...",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else if (matches.isEmpty()) {
                        Text(
                            "No matches available",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.padding(innerPadding)) {
                            items(matches) { match ->
                                MatchItem(match)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MatchItem(match: Match) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = match.strEvent ?: "No Title",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = match.dateEvent ?: "No Date"
                )
            }
        }
    }
}
