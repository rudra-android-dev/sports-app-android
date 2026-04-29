package com.example.sportsapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.sportsapp.data.Match
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.example.sportsapp.viewmodel.MatchViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.room.Room
import com.example.sportsapp.database.AppDatabase
import com.example.sportsapp.repository.MatchRepository
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Notifications
import com.example.sportsapp.notifications.NotificationHelper
import com.example.sportsapp.notifications.ReminderReceiver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "sports_db"
            ).build()

            val repository = remember {
                MatchRepository(db)
            }

            val viewModel = remember {
                MatchViewModel(repository)
            }

            LaunchedEffect(Unit) {

                NotificationHelper.createChannel(
                    applicationContext
                )

                viewModel.loadFavorites()
            }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                composable("home") {
                    MatchListScreen(
                        navController,
                        viewModel
                    )
                }

                composable("details") { backStackEntry ->
                    val match = backStackEntry.arguments?.getString("match")
                    MatchDetailScreen(viewModel)
                }
                composable("favorites") {
                    FavoritesScreen(
                        navController,
                        viewModel
                    )
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
){
    val context = LocalContext.current

    fun scheduleReminder() {

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 30000,
            pendingIntent
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClick() },

        shape = RoundedCornerShape(22.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // HOME TEAM
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = match.strHomeTeamBadge,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = match.strHomeTeam ?: "Team A",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // VS
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VS",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // AWAY TEAM
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = match.strAwayTeamBadge,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = match.strAwayTeam ?: "Team B",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = match.dateEvent ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // ACTION ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {

                IconButton(
                    onClick = {
                        onFavoriteClick()
                    }
                ) {
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector =
                                Icons.Outlined.StarBorder,
                            contentDescription = null
                        )
                    }
                }

                IconButton(
                    onClick = {
                        scheduleReminder()
                        onReminderScheduled()
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Notifications,
                        contentDescription = null
                    )
                }

            }

        }
    }
}
