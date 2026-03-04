package com.example.projectforkts.repository

class LoginRepository {
    fun login(username: String, password: String): Result<Unit> {
        return if (username == "admin" && password == "admin") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }
}