package com.example.projectforkts.main.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectforkts.main.data.db.RepoEntity

@Dao
interface RepoDao {
    @Query("SELECT * FROM repos WHERE `query` = :query")
    suspend fun getReposByQuery(query: String): List<RepoEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("DELETE FROM repos WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)
}