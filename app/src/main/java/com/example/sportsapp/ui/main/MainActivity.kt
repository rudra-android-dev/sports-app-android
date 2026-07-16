package com.example.sportsapp.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.sportsapp.data.Match
import com.example.sportsapp.database.AppDatabase
import com.example.sportsapp.notifications.NotificationHelper
import com.example.sportsapp.notifications.ReminderReceiver
import com.example.sportsapp.repository.MatchRepository
import com.example.sportsapp.ui.theme.SportsAccent
import com.example.sportsapp.ui.theme.SportsAppTheme
import com.example.sportsapp.viewmodel.MatchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsAppTheme {
                val navController = rememberNavController()

                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "sports_db"
                ).build()

                val repository = remember { MatchRepository(db) }
                val viewModel  = remember { MatchViewModel(repository) }

                LaunchedEffect(Unit) {
                    NotificationHelper.createChannel(applicationContext)
                    viewModel.loadFavorites()
                }

                NavHost(
                    navController    = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        MatchListScreen(navController, viewModel)
                    }
                    composable("details") {
                        MatchDetailScreen(viewModel)
                    }
                    composable("favorites") {
                        FavoritesScreen(navController, viewModel)
                    }
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
    onReminderScheduled: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    fun scheduleReminder() {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 30_000,
            pendingIntent
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (!match.dateEvent.isNullOrEmpty()) {
                Surface(
                    color  = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    shape  = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text     = match.dateEvent,
                        style    = MaterialTheme.typography.labelSmall,
                        color    = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier            = Modifier.fillMaxWidth(),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TeamColumn(
                    badgeUrl = match.strHomeTeamBadge,
                    name     = match.strHomeTeam ?: "Home",
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier          = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment  = Alignment.Center
                ) {
                    Text(
                        text      = "VS",
                        style     = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color     = MaterialTheme.colorScheme.onPrimary,
                        fontSize  = 11.sp
                    )
                }

                TeamColumn(
                    badgeUrl = match.strAwayTeamBadge,
                    name     = match.strAwayTeam ?: "Away",
                    modifier = Modifier.weight(1f),
                    alignEnd = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scheduleReminder()
                        onReminderScheduled()
                    }
                ) {
                    Icon(
                        imageVector        = Icons.Default.Notifications,
                        contentDescription = "Set reminder",
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector        = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = if (isFavorite) "Remove from favourites" else "Add to favourites",
                        tint               = if (isFavorite) SportsAccent else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamColumn(
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
                .size(52.dp)
                .align(if (alignEnd) Alignment.End else Alignment.Start)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text      = name,
            style     = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines  = 2,
            overflow  = TextOverflow.Ellipsis,
            textAlign = if (alignEnd) TextAlign.End else TextAlign.Start,
            color     = MaterialTheme.colorScheme.onSurface
        )
    }
}