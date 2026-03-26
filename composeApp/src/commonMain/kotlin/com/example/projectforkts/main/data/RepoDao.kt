package com.example.projectforkts.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepoDao {
    @Query("SELECT * FROM repos WHERE `query` = :query")
    suspend fun getReposByQuery(query: String): List<RepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("DELETE FROM repos WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)
}