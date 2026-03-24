package com.example.projectforkts.profile.domain

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
}