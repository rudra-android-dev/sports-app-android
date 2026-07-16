package com.example.sportsapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sportsapp.viewmodel.MatchViewModel

@Composable
fun MatchDetailScreen(
    viewModel: MatchViewModel,
    onBackClick: (() -> Unit)? = null
) {
    val match = viewModel.selectedMatch.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY   = 900f
                    )
                )
                .padding(bottom = 32.dp)
        ) {
            Column {
                if (onBackClick != null) {
                    IconButton(
                        onClick  = onBackClick,
                        modifier = Modifier.padding(top = 16.dp, start = 4.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint               = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(56.dp))
                }
                Text(
                    text      = match?.strEvent ?: "Match Details",
                    style     = MaterialTheme.typography.titleMedium,
                    color     = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    DetailTeamColumn(
                        badgeUrl = match?.strHomeTeamBadge,
                        name     = match?.strHomeTeam ?: "Home",
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier         = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "VS",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    // Away
                    DetailTeamColumn(
                        badgeUrl = match?.strAwayTeamBadge,
                        name     = match?.strAwayTeam ?: "Away",
                        modifier = Modifier.weight(1f),
                        alignEnd = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

            if (!match?.dateEvent.isNullOrEmpty()) {
                DetailInfoCard(
                    label = "📅  Date",
                    value = match?.dateEvent ?: ""
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!match?.strEvent.isNullOrEmpty()) {
                DetailInfoCard(
                    label = "🏆  Event",
                    value = match?.strEvent ?: ""
                )
            }
        }
    }
}

@Composable
private fun DetailTeamColumn(
    badgeUrl : String?,
    name     : String,
    modifier : Modifier = Modifier,
    alignEnd : Boolean  = false
) {
    Column(
        modifier            = modifier,
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start
    ) {
        AsyncImage(
            model              = badgeUrl,
            contentDescription = name,
            modifier           = Modifier
                .size(72.dp)
                .align(if (alignEnd) Alignment.End else Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text       = name,
            style      = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onPrimary,
            maxLines   = 2,
            overflow   = TextOverflow.Ellipsis,
            textAlign  = if (alignEnd) TextAlign.End else TextAlign.Start
        )
    }
}

@Composable
private fun DetailInfoCard(label: String, value: String) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text  = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}