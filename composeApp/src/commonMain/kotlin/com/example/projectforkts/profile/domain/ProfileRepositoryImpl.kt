package com.example.projectforkts.profile.domain

import com.example.projectforkts.main.data.network.GitHubApi
import com.example.projectforkts.main.data.network.UserResponse

class ProfileRepositoryImpl: ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = GitHubApi.getUserProfile()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun UserResponse.toDomain() = UserProfile(
        login = login,
        name = name,
        bio = bio,
        avatarUrl = avatarUrl,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )

}