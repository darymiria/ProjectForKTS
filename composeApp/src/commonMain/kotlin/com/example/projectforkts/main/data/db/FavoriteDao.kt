package com.example.projectforkts.main.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(entity: FavoriteEntity)

    @Query("DELETE FROM Favorites WHERE id = :id")
    suspend fun removeFavorite(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM Favorites WHERE id = :id)")
    fun isFavorite(id: Long): Flow<Boolean>
}