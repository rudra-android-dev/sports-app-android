package com.example.sportsapp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MatchDetailScreen(match: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = match ?: "No Data",
            fontWeight = FontWeight.Bold
        )
    }
}