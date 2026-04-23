package com.example.sportsapp.database

import androidx.room.*

@Dao
interface FavoriteMatchDao {

    @Insert
    suspend fun insertFavorite(match: FavoriteMatch)

    @Delete
    suspend fun deleteFavorite(match: FavoriteMatch)

    @Query("SELECT * FROM favorite_matches")
    suspend fun getFavorites(): List<FavoriteMatch>
}