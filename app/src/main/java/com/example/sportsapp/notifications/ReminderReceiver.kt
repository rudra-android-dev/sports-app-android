package com.example.sportsapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.sportsapp.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val builder =
            NotificationCompat.Builder(
                context,
                NotificationHelper.CHANNEL_ID
            )
                .setSmallIcon(
                    R.mipmap.ic_launcher
                )
                .setContentTitle(
                    "Match Reminder"
                )
                .setContentText(
                    "Your saved match is coming up!"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        manager.notify(
            1001,
            builder.build()
        )
    }
}