package com.example.projectforkts.profile.domain

import com.example.projectforkts.core.AppStorage

class LogoutUseCase(private val appStorage: AppStorage) {
    suspend operator fun invoke(){
        appStorage.clearToken()
    }
}