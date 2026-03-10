package com.example.projectforkts.login.data

import com.example.projectforkts.login.domain.LoginRepository

class LoginRepositoryImpl: LoginRepository {
    override fun login(username: String, password: String): Result<Unit> {
        return if (username == "admin" && password == "admin") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }
}