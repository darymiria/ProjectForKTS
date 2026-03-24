package com.example.projectforkts.profile.domain

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}