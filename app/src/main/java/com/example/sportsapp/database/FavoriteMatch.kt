package com.example.sportsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_matches")
data class FavoriteMatch(

    @PrimaryKey
    val eventId: String,

    val eventName: String
)