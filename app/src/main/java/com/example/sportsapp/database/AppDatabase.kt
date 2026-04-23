package com.example.sportsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteMatch::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteMatchDao
}