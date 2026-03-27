package com.example.projectforkts.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppStorage(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")
        private val TOKEN_KEY = stringPreferencesKey("access_token")
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .map { it[ONBOARDING_KEY] ?: false }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { it[ONBOARDING_KEY] = true }
    }

    val accessToken: Flow<String?> = dataStore.data
        .map { it[TOKEN_KEY] }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
        TokenStorage.accessToken = token
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
        TokenStorage.accessToken = null
    }

}