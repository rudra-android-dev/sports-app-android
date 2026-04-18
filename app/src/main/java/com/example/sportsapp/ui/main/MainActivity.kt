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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    Text(
                        text = "Loading...",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getMatches()

                if (response.isSuccessful) {
                    val data = response.body()
                    data?.events?.forEach {
                        Log.d("MATCH", "${it.strEvent} - ${it.dateEvent}")
                    }
                } else {
                    Log.e("API_ERROR", response.code().toString())
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.message.toString())
            }
        }
    }
}
